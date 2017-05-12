import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;


public class FormatForTestRail extends EditorAction {

	public FormatForTestRail(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	public FormatForTestRail() {
		this(new UpHandler());
	}

	private static class UpHandler extends EditorWriteActionHandler {
		private UpHandler() {
		}

		public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
			Document document = editor.getDocument();
			if (!document.isWritable()) {
				return;
			}
			String fullText = document.getText();
			String[] allLines = fullText.split("\n");
			String variables="";
			int length = 0, from = 0;

			ArrayList<String> values = new ArrayList<>();
			ArrayList<String[]> valuesArr = new ArrayList<>();
			for(int i = 0; i < allLines.length; i++) {
				if(allLines[i].matches("\\s*Примеры:\\s*")) {
					if(i+2 < allLines.length) {
						from = length+allLines[i].length();
						variables = allLines[i+1];
						for(int j = i+2; j < allLines.length; j++)
							if(!allLines[i].matches("\\s*"))
								values.add(allLines[j]);
//						length += values.length() + variables.length() + 2;
						break;
					}
				}
				length += allLines[i].length()+1;
			}
			String[] variablesArr = variables.split("\\s*\\|\\|\\|:\\s*|\\s*\\|:\\s*|\\s*\\|\\s*");
			for (String value : values) valuesArr.add(value.split("\\s*\\|\\|\\s*|\\s*\\|\\s*"));
			StringBuilder variablesBuilder = new StringBuilder("|||:");
			StringBuilder valuesBuilder = new StringBuilder("||");
			for(int i = 0;i<variablesArr.length-1;i++)
				if(!variablesArr[i].isEmpty()) {
					variablesBuilder.append(variablesArr[i]).append("|:");
				}
			variablesBuilder.append(variablesArr[variablesArr.length - 1]);
			for (int i = 0; i < valuesArr.size()-1; i++) {
				for(int j = 0;j<valuesArr.get(i).length-1;j++)
					if(!valuesArr.get(i)[j].isEmpty()) {
						valuesBuilder.append(valuesArr.get(i)[j]).append("|");
					}
				valuesBuilder.append(valuesArr.get(i)[valuesArr.get(i).length - 1]).append("\n").append("||");
			}

			for(int j = 0;j<valuesArr.get(valuesArr.size()-1).length-1;j++)
				if(!valuesArr.get(valuesArr.size()-1)[j].isEmpty()) {
					valuesBuilder.append(valuesArr.get(valuesArr.size()-1)[j]).append("|");
				}
			valuesBuilder.append(valuesArr.get(valuesArr.size()-1)[valuesArr.get(valuesArr.size()-1).length - 1]).append("\n");
			if(!values.isEmpty() && !variables.isEmpty()) {
				document.replaceString(from, fullText.length(), "");
				String s1 = variablesBuilder.toString();
				String s2 = valuesBuilder.toString();
				document.insertString(from, "\n" + s1 + "\n" + s2);
			}
		}
	}
}