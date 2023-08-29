package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutocomplete {

	private final TextField textField;
	private final SqlController sqlController;
	private AutoCompletionBinding<String> auto;

	public ShortcutAutocomplete(SqlController sqlController, TextField textField) {
		this.sqlController = sqlController;
		this.textField = textField;
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.sqlController.getSuggestedShortcuts(""));
		this.auto.setDelay(50);
		this.auto.setMaxWidth(180);
		this.auto.setVisibleRowCount(5);
	}

	public void reset() {
		this.auto.dispose();
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.sqlController.getSuggestedShortcuts(""));
		this.auto.setDelay(50);
		this.auto.setMaxWidth(180);
		this.auto.setVisibleRowCount(5);
	}
}
