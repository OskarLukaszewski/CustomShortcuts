package custom_shortcuts.gui.add_shortcut_window;

import custom_shortcuts.functionalities.autocompletion.CollectionOfAutoCompletions;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;

public class AddShortcutController {

	private final Stage addShortcutStage;
	private boolean includePicture;

	@FXML
	private TextField nameTextField, parametersTextField, filePathTextField;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private Button addButton, closeButton, filePathButton;

	@FXML
	private CheckBox includePictureCheckBox;

	@FXML
	private void initialize() {
		this.includePictureCheckBox.selectedProperty()
				.addListener((observableValue, aBoolean, t1) -> setIncludePictureElement(t1));
	}

	public AddShortcutController(Stage addShortcutStage) {
		this.addShortcutStage = addShortcutStage;
	}

	public void filePathButtonClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a picture");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png"),
				new FileChooser.ExtensionFilter("ALL FILES", "*.*")
		);
		File file = fileChooser.showOpenDialog(this.addShortcutStage.getScene().getWindow());
		if (file != null) {
			this.filePathTextField.setText(file.getPath());
		}
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
				getSqlController().insertShortcut(new String[] {
						this.nameTextField.getText(),
						this.parametersTextField.getText(),
						this.bodyTextArea.getText()});
				CollectionOfAutoCompletions.resetAutoCompletions();
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
		hideAndReset();
	}

	public void closeButtonClick() {
		hideAndReset();
	}

	private void hideAndReset() {
		this.nameTextField.setText("");
		this.parametersTextField.setText("");
		this.bodyTextArea.setText("");
		this.addShortcutStage.hide();
	}

	private void setIncludePictureElement(boolean includePicture) {
		this.includePicture = includePicture;
		this.filePathTextField.setDisable(!includePicture);
		this.filePathTextField.setEditable(includePicture);
		this.filePathButton.setDisable(!includePicture);
	}
}
