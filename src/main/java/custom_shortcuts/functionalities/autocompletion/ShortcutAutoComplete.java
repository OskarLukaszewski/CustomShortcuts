package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutoComplete {

	private final TextField textField;
	private final SqlController sqlController;
	private int maxWidth = 0;
	private AutoCompletionBinding<String> auto;

	public ShortcutAutoComplete(SqlController sqlController, TextField textField) {
		CollectionOfAutoCompletions.add(this);
		this.sqlController = sqlController;
		this.textField = textField;
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.sqlController.getSuggestedShortcuts(""));
		this.auto.setDelay(50);
		this.auto.setVisibleRowCount(5);
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
		this.auto.setMaxWidth((this.maxWidth));
	}

	public void reset() {
		this.auto.dispose();
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.sqlController.getSuggestedShortcuts(""));
		this.auto.setDelay(50);
		this.auto.setVisibleRowCount(5);
		if (this.maxWidth != 0) {
			this.auto.setMaxWidth(this.maxWidth);
		}
	}
}
