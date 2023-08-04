package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutocomplete {

	private final SuggestionProvider suggestionProvider;
	private final TextField textField;
	private String currentText;

	public ShortcutAutocomplete(SqlController sqlController, TextField textField) {
		this.textField = textField;
		this.suggestionProvider = new SuggestionProvider(sqlController);
		AutoCompletionBinding<String> auto = TextFields.bindAutoCompletion(
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
