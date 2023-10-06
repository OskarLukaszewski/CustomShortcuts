package custom_shortcuts.gui.add_shortcut_window;

import custom_shortcuts.database.DataFolderException;
import custom_shortcuts.database.SqlControllerException;
import custom_shortcuts.database.TemporaryPicture;
import custom_shortcuts.functionalities.autocompletion.CollectionOfAutoCompletions;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.SQLException;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getDataFolder;
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
		TemporaryPicture temporaryPicture = null;
		boolean shortcutAdded = false;
		try {
			if (this.includePicture) {
				temporaryPicture = getDataFolder()
						.createTemporaryPicture(this.filePathTextField.getText(), this.nameTextField.getText());
			}
			getSqlController().insertShortcut(new String[]{
					this.nameTextField.getText(),
					this.parametersTextField.getText(),
					this.bodyTextArea.getText()}
			);
			shortcutAdded = true;
			CollectionOfAutoCompletions.resetAutoCompletions();
			if (this.includePicture && temporaryPicture != null) {
				if (temporaryPicture.permanentlyAddPicture()) {
					getSqlController().addPictureToShortcut(this.nameTextField.getText(),
							temporaryPicture.getCurrentName());
				} else {
					if (!temporaryPicture.delete()) {
						temporaryPicture.deleteOnExit();
					}
					showAlert("Incorrect file path",
							"The shortcut has been created, but no picture has been added.");
					return;
				}
			}
		} catch (SqlControllerException | DataFolderException e) {
			if (temporaryPicture != null && !shortcutAdded) {
				if (!temporaryPicture.delete()) {
					temporaryPicture.deleteOnExit();
				}
			} else if (this.includePicture && shortcutAdded) {
				try {
					getSqlController().deleteShortcut(this.nameTextField.getText());
				} catch (SqlControllerException | DataFolderException ex) {
					throw new RuntimeException(ex);
				}
			}
			showAlert("Incorrect input", e.getMessage());
			return;
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
		this.filePathTextField.setText("");
		this.includePictureCheckBox.setSelected(false);
		this.addShortcutStage.hide();
	}

	private void setIncludePictureElement(boolean includePicture) {
		this.includePicture = includePicture;
		this.filePathTextField.setDisable(!includePicture);
		this.filePathTextField.setEditable(includePicture);
		this.filePathButton.setDisable(!includePicture);
	}

	private void showAlert(String header, String body) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(body);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(getIcon());
		alert.showAndWait();
	}
}
