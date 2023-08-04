package custom_shortcuts.functionalities.autompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutocomplete {

	private final SqlController sqlController;
	private TextField textField;

	public ShortcutAutocomplete(SqlController sqlController) {
		this.sqlController = sqlController;
	}

	public void showSuggestions() {
		TextFields.bindAutoCompletion(
				this.textField, "1", "2");
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}
}
