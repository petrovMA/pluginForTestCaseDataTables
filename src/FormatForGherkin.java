import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class FormatForGherkin extends EditorAction {

	public FormatForGherkin(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	public FormatForGherkin() {
		this(new UpHandler());
	}

	private static class UpHandler extends EditorWriteActionHandler {
		private UpHandler() {
		}

		public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
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
			StringBuilder variablesBuilder = new StringBuilder("");
			StringBuilder valuesBuilder = new StringBuilder("");
			for(String s : variablesArr)
				variablesBuilder.append(s).append("|");
//			for (String aVariablesArr : variablesArr)
//				if (!aVariablesArr.isEmpty()) {
//					variablesBuilder.append(aVariablesArr).append("|");
//				}
			for (String[] arr : valuesArr) {
				for (String anArr : arr)
						valuesBuilder.append(anArr).append("|");
				valuesBuilder.append("\n");
			}
			if(!values.isEmpty() && !variables.isEmpty()) {
				document.replaceString(from, fullText.length(), "");
				String s1 = variablesBuilder.toString();
				String s2 = valuesBuilder.toString();
				document.insertString(from, "\n" + s1 + "\n" + s2);
			}
		}
	}
}