package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutocomplete {

	private final SqlController sqlController;
	private final SuggestionProvider suggestionProvider;
	private TextField textField;
	private String currentText;

	public ShortcutAutocomplete(SqlController sqlController, TextField textField) {
		this.sqlController = sqlController;
		this.textField = textField;
		this.suggestionProvider = new SuggestionProvider(this.sqlController);
		AutoCompletionBinding auto = TextFields.bindAutoCompletion(
				this.textField,
				this.suggestionProvider.call(new SuggestionProviderRequest(this.textField.getText())));
		auto.setDelay(50);
		auto.setMaxWidth(180);
		auto.setVisibleRowCount(5);
		this.currentText = this.textField.getText();
	}

	public void updateSuggestions() {
		if (!this.textField.getText().equals(this.currentText)) {
			this.suggestionProvider.call(new SuggestionProviderRequest(this.textField.getText()));
			this.currentText = this.textField.getText();
		}
	}
}
