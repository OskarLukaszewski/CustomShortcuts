package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.functionalities.autocompletion.CollectionOfAutoCompletions;
import custom_shortcuts.functionalities.autocompletion.ShortcutAutoComplete;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SearchTabController {

	private final ListShortcutsController listShortcutsController;
	private ShortcutAutoComplete shortcutAutoComplete;

	@FXML
	private BorderPane borderPane;

	@FXML
	private TextField textField;

	@FXML
	private Button button;

	@FXML
	private void initialize() {
		GridPane.setConstraints(this.borderPane, 0, 0);
		this.shortcutAutoComplete =
				new ShortcutAutoComplete(this.textField);
		this.listShortcutsController.getGridPane().widthProperty().addListener(
				(observableValue, number, t1) -> this.borderPane.setPrefWidth(t1.doubleValue()));
	}

	public SearchTabController(ListShortcutsController listShortcutsController) {
		this.listShortcutsController = listShortcutsController;
	}

	public void buttonPress() {
		this.listShortcutsController.removeRowAtIndex(0);
		this.listShortcutsController.returnTopRowToOriginalPosition();
		this.textField.setText("");
	}

	public void textFieldEnter() {
		this.listShortcutsController.returnTopRowToOriginalPosition();
		this.listShortcutsController.moveRowToTopByShortcutName(this.textField.getText());
		this.textField.setText("");
	}

	public void requestFocus() {
		this.textField.requestFocus();
	}

	public void clearAutoCompletion() {
		CollectionOfAutoCompletions.remove(this.shortcutAutoComplete);
	}
}
