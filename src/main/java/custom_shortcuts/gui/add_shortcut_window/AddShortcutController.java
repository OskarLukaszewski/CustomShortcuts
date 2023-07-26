package custom_shortcuts.gui.add_shortcut_window;

import custom_shortcuts.database.SqlController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class AddShortcutController {

	private final SqlController sqlController;
	private final Stage addShortcutStage;

	@FXML
	private TextField nameTextField, parametersTextField;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private Button addButton, closeButton;

	public AddShortcutController(Stage addShortcutStage, SqlController sqlController) {
		this.addShortcutStage = addShortcutStage;
		this.sqlController = sqlController;
	}

	public void addButtonClick() {
		if (this.bodyTextArea.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Incorrect input");
			alert.setContentText("The body of a shortcut cannot be empty.");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			alert.showAndWait();
			return;
		} else {
			try {
				this.sqlController.insertShortcut(new String[] {
						this.nameTextField.getText(),
						this.parametersTextField.getText(),
						this.bodyTextArea.getText()});
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Incorrect input");
				alert.setContentText(e.getMessage());
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(getIcon());
				alert.showAndWait();
				return;
			}
		}
		hideAndRestet();
	}

	public void closeButtonClick() {
		hideAndRestet();
	}

	private void hideAndRestet() {
		this.nameTextField.setText("");
		this.parametersTextField.setText("");
		this.bodyTextArea.setText("");
		this.addShortcutStage.hide();
	}
}
