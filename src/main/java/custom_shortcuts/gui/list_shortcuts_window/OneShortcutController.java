package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.database.DataFolderException;
import custom_shortcuts.database.SqlControllerException;
import custom_shortcuts.database.TemporaryPicture;
import custom_shortcuts.functionalities.autocompletion.CollectionOfAutoCompletions;
import custom_shortcuts.utils.Alerts;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Path;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getDataFolder;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;

public class OneShortcutController {

	private boolean isEditOn, isDragOn, movedToTop, includesPicture;
	private String[] shortcut;
	private double yOffSet;
	private double initialHeight;
	private final int id;
	private int minHeight;
	private ListShortcutsController listShortcutsController;

	@FXML
	private BorderPane mainBorderPane, pictureBorderPane;

	@FXML
	private GridPane bottomGridPane;

	@FXML
	private Button changePictureButton, showPictureButton;

	@FXML
	private TextField nameTextField, parametersTextField, picturePathTextField;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private ToggleButton pictureToggleButton;

	@FXML
	private FontAwesomeIconView topIcon, bottomIcon, pictureFirstIcon;

	@FXML
	private Pane separator;

	@FXML
	private void initialize() {
		this.nameTextField.setText(this.shortcut[0]);
		this.parametersTextField.setText(this.shortcut[1]);
		this.bodyTextArea.setText(this.shortcut[2]);
		setOnFocus(this.nameTextField);
		setOnFocus(this.parametersTextField);
		setOnFocus(this.bodyTextArea);
		setSeparatorMouseFunction();
		setIncludesPicture(this.shortcut[3].equals("true"));
		GridPane.setRowIndex(this.pictureBorderPane, 0);
		this.pictureToggleButton.selectedProperty()
				.addListener((observableValue, aBoolean, t1) -> setPictureToggle(t1));
		this.pictureToggleButton.setSelected(false);
	}

	public OneShortcutController(
			String[] shortcut, int id) {
		this.isEditOn = false;
		this.isDragOn = false;
		this.shortcut = shortcut;
		this.id = id;
		this.yOffSet = 0;
		this.initialHeight = 0;
	}

	public void setListShortcutsController(
			ListShortcutsController listShortcutsController) {
		this.listShortcutsController = listShortcutsController;
	}

	public int getId() {
		return this.id;
	}

	public String getShortcutName() {
		return this.shortcut[0];
	}

	public boolean isMovedToTop() {
		return this.movedToTop;
	}

	public void moveRowToOriginalPosition() {
		GridPane.setConstraints(this.mainBorderPane, 0, this.id);
		this.movedToTop = false;
	}

	public void moveRowToTop() {
		GridPane.setConstraints(this.mainBorderPane, 0, 1);
		this.movedToTop = true;
	}

	public void topButtonClick() {
		if (this.isEditOn) {
			if (isSame()) {
				setEditable(false);
				return;
			}
			boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
					"Are you sure you want to save changes to shortcut '" + this.shortcut[0] + "'?");
			if (confirmed) {
				String[] newShortcut = new String[] {
						this.nameTextField.getText(),
						this.parametersTextField.getText(),
						this.bodyTextArea.getText(),
						this.shortcut[3],
						this.shortcut[4]
				};
				try {
					getSqlController().updateShortcut(this.shortcut[0], newShortcut);
					CollectionOfAutoCompletions.resetAutoCompletions();
					this.shortcut = newShortcut;
				} catch (SqlControllerException e) {
					Alerts.showErrorAlert("Operation failed", e.getMessage());
				}
				setEditable(false);
			}
		} else {
			setEditable(true);
		}
	}

	public void middleButtonClick() {
		if (this.isEditOn) {
			if (isSame()) {
				setEditable(false);
				return;
			}
			boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
					"Are you sure you want to discard changes to shortcut '" + this.shortcut[0] + "'?");
			if (confirmed) {
				this.nameTextField.setText(this.shortcut[0]);
				this.parametersTextField.setText(this.shortcut[1]);
				this.bodyTextArea.setText(this.shortcut[2]);
				setEditable(false);
			}
		} else {
			boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
					"Are you sure you want to remove shortcut '" + this.shortcut[0] + "'?");
			if (confirmed) {
				try {
					getSqlController().deleteShortcut(this.shortcut[0]);
					CollectionOfAutoCompletions.resetAutoCompletions();
					if (this.isMovedToTop()) {
						this.listShortcutsController.removeRowAtIndex(1);
					} else {
						this.listShortcutsController.removeRowAtIndex(this.id);
					}
				} catch (SqlControllerException e) {
					Alerts.showErrorAlert("Operation failed", e.getMessage());
				} catch (DataFolderException e) {
					Alerts.showInformationAlert("Operation failed", e.getMessage());
					CollectionOfAutoCompletions.resetAutoCompletions();
					if (this.isMovedToTop()) {
						this.listShortcutsController.removeRowAtIndex(1);
					} else {
						this.listShortcutsController.removeRowAtIndex(this.id);
					}
				}
			}
		}
	}

	public void firstPictureButtonClick() {
		if (!this.includesPicture) {
			boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
					"Are you sure you want to add a picture to shortcut '" + this.shortcut[0] + "'?");
			if (confirmed) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select a picture");
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png"),
						new FileChooser.ExtensionFilter("ALL FILES", "*.*")
				);
				File file = fileChooser.showOpenDialog(this.mainBorderPane.getScene().getWindow());
				if (file != null) {
					try {
						TemporaryPicture temporaryPicture = getDataFolder()
								.createTemporaryPicture(file.getPath(), this.shortcut[0]);
						if (temporaryPicture.permanentlyAddPicture()) {
							getSqlController().addPictureToShortcut(this.shortcut[0], temporaryPicture.getCurrentName());
						}
						this.shortcut[3] = "true";
						this.shortcut[4] = temporaryPicture.getCurrentName();
						setIncludesPicture(true);
					} catch (DataFolderException | SqlControllerException e) {
						Alerts.showErrorAlert("Operation failed", e.getMessage());
					}
				}
			}
		} else {
			boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
					"Are you sure you want to delete the picture from shortcut '" + this.shortcut[0] + "'?");
			if (confirmed) {
				try {
					getDataFolder().deletePicture(this.shortcut[4]);
					getSqlController().removePictureFromShortcut(this.shortcut[0]);
					this.shortcut[3] = "false";
					this.shortcut[4] = null;
					setIncludesPicture(false);
				} catch (SqlControllerException | DataFolderException e) {
					Alerts.showErrorAlert("Operation failed", e.getMessage());
				}
			}
		}
	}

	public void changePictureButtonClick() {
		boolean confirmed = Alerts.showConfirmationAlert("Confirmation",
				"Are you sure you want to change the picture in shortcut '" + this.shortcut[0] + "'?");
		if (confirmed) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select a picture");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png"),
					new FileChooser.ExtensionFilter("ALL FILES", "*.*")
			);
			File file = fileChooser.showOpenDialog(this.mainBorderPane.getScene().getWindow());
			if (file != null) {
				try {
					String newName = getDataFolder().changePicture(file.getPath(), this.shortcut[4]);
					getSqlController().changePictureInShortcut(this.shortcut[0], newName);
					this.shortcut[4] = newName;
					this.picturePathTextField.setText(getPathFromName(newName));
				} catch (SqlControllerException | DataFolderException e) {
					Alerts.showErrorAlert("Operation failed", e.getMessage());
				}
			}
		}
	}

	public void showPictureButtonClick() {
		Image picture = new Image("file:" + getDataFolder().getPathToPicture(this.shortcut[4]).toString());
		this.listShortcutsController.showPicture(picture);
	}

	private void setEditable(boolean editable) {
		this.isEditOn = editable;
		this.nameTextField.setEditable(editable);
		this.parametersTextField.setEditable(editable);
		this.bodyTextArea.setEditable(editable);
		if (editable) {
			this.topIcon.setIcon(FontAwesomeIcon.SAVE);
			this.bottomIcon.setIcon(FontAwesomeIcon.CLOSE);
		} else {
			this.topIcon.setIcon(FontAwesomeIcon.EDIT);
			this.bottomIcon.setIcon(FontAwesomeIcon.TRASH_ALT);
		}
	}

	private boolean isSame() {
		if (!this.nameTextField.getText().equals(this.shortcut[0])) {
			return false;
		}
		if (!this.parametersTextField.getText().equals(this.shortcut[1])) {
			return false;
		}
		return this.bodyTextArea.getText().equals(this.shortcut[2]);
	}

	private void setOnFocus(Node node) {
		node.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue && !this.isEditOn) {
				this.listShortcutsController.setFocus();
			}
		});
	}

	private void setSeparatorMouseFunction() {
		this.separator.setOnMousePressed(mouseEvent -> {
			this.yOffSet = mouseEvent.getScreenY();
			this.initialHeight = this.mainBorderPane.getHeight();
			this.separator.getScene().setCursor(Cursor.CLOSED_HAND);
			this.isDragOn = true;
		});
		this.separator.setOnMouseDragged(mouseEvent -> {
			double newHeight = this.initialHeight + mouseEvent.getScreenY() - this.yOffSet;
			this.mainBorderPane.setMinHeight(Math.max(newHeight, this.minHeight));
		});
		this.separator.setOnMouseReleased(mouseEvent -> {
			double mousePosition = mouseEvent.getSceneY();
			double separatorPosition = this.separator.localToScene(this.separator.getBoundsInLocal()).getCenterY()+5;
			if (Math.abs(mousePosition - separatorPosition) <= 10) {
				this.separator.getScene().setCursor(Cursor.OPEN_HAND);
			} else {
				this.separator.getScene().setCursor(Cursor.DEFAULT);
			}
			this.isDragOn = false;
		});
		this.separator.setOnMouseEntered(mouseEvent -> {
			if (!this.isDragOn) {
				this.separator.getScene().setCursor(Cursor.OPEN_HAND);
			}
		});
		this.separator.setOnMouseExited(mouseEvent -> {
			if (!this.isDragOn) {
				this.separator.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	private void setIncludesPicture(boolean includesPicture) {
		this.includesPicture = includesPicture;
		if (includesPicture) {
			this.picturePathTextField.setText(getPathFromName(this.shortcut[4]));
			this.pictureFirstIcon.setIcon(FontAwesomeIcon.TRASH_ALT);
			this.changePictureButton.setDisable(false);
			this.showPictureButton.setDisable(false);
		} else {
			this.picturePathTextField.setText("");
			this.pictureFirstIcon.setIcon(FontAwesomeIcon.PLUS_CIRCLE);
			this.changePictureButton.setDisable(true);
			this.showPictureButton.setDisable(true);
		}
	}

	private String getPathFromName(String name) {
		Path path = getDataFolder().getPathToPicture(name);
		if (path != null) {
			return path.toString();
		} else {
			return "File Not Found";
		}
	}

	private void setPictureToggle(boolean pictureElementToggled) {
		if (pictureElementToggled) {
			this.bottomGridPane.getChildren().add(this.pictureBorderPane);
			this.minHeight = 125;
			this.mainBorderPane.setMinHeight(this.mainBorderPane.getMinHeight() + 35);
		} else {
			this.bottomGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 0);
			this.minHeight = 90;
			this.mainBorderPane.setMinHeight(this.mainBorderPane.getMinHeight() - 35);
		}
	}
}
