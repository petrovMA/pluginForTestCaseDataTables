import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

import java.io.File;

public class ObjectsAction extends EditorAction {

	public ObjectsAction(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	public ObjectsAction() {
		this(new UpHandler());
	}

	private static class UpHandler extends EditorWriteActionHandler {
		private UpHandler() {
		}

		public void executeWriteAction(Editor editor, DataContext dataContext) {
			Document document = editor.getDocument();

			if (!document.isWritable()) {
				return;
			}
			SelectionModel selectionModel = editor.getSelectionModel();

			// get the range of the selected characters
			TextRange charsRange = new TextRange(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd());
			// get the range of the selected lines (block of code)
			TextRange linesRange = new TextRange(document.getLineNumber(charsRange.getStartOffset()), document.getLineNumber(charsRange.getEndOffset()));
			// range of the duplicated string
			TextRange linesBlock = new TextRange(document.getLineStartOffset(linesRange.getStartOffset()), document.getLineEndOffset(linesRange.getEndOffset()));

			// insert new duplicated string into the document
//			document.insertString(linesBlock.getStartOffset(), Property.readPartText(new File(Property.getStorage() + "\\objects.xml")));
		}
	}
}