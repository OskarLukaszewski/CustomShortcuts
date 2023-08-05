package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ShortcutAutocomplete {

	private SuggestionProvider suggestionProvider;
	private final TextField textField;
	private final SqlController sqlController;
	private AutoCompletionBinding<String> auto;
	private String currentText;

	public ShortcutAutocomplete(SqlController sqlController, TextField textField) {
		this.sqlController = sqlController;
		this.textField = textField;
		this.suggestionProvider = new SuggestionProvider(this.sqlController);
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.suggestionProvider.call(new SuggestionProviderRequest(this.textField.getText())));
		this.auto.setDelay(50);
		this.auto.setMaxWidth(180);
		this.auto.setVisibleRowCount(5);
		this.currentText = this.textField.getText();
	}

	public void updateSuggestions() {
		if (!this.textField.getText().equals(this.currentText)) {
			this.suggestionProvider.call(new SuggestionProviderRequest(this.textField.getText()));
			this.currentText = this.textField.getText();
		}
	}

	public void reset() {
		this.auto.dispose();
		this.suggestionProvider = new SuggestionProvider(this.sqlController);
		this.auto = TextFields.bindAutoCompletion(
				this.textField,
				this.suggestionProvider.call(new SuggestionProviderRequest(this.textField.getText())));
		this.auto.setDelay(50);
		this.auto.setMaxWidth(180);
		this.auto.setVisibleRowCount(5);
	}
}
