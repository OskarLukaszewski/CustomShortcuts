package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.database.SqlController;
import custom_shortcuts.functionalities.autocompletion.CollectionOfAutoCompletions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.Optional;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class OneShortcutController {

	private boolean isEditOn, isDragOn, movedToTop;
	private String[] shortcut;
	private double yOffSet;
	private double initialHeight;
	private final int id;
	private final SqlController sqlController;
	private ListShortcutsController listShortcutsController;

	@FXML
	private BorderPane mainBorderPane;

	@FXML
	private TextField nameTextField, parametersTextField;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private Button topButton, bottomButton;

	@FXML
	private FontAwesomeIconView topIcon, bottomIcon;

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
	}

	public OneShortcutController(
			SqlController sqlController, String[] shortcut, int id) {
		this.sqlController = sqlController;
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

	public void addParentGridPaneListener(GridPane parentGridPane) {
		parentGridPane.widthProperty().addListener(
				(observableValue, number, t1) -> this.mainBorderPane.setPrefWidth(t1.doubleValue()));
		setSeparatorMouseFunction();
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
			Alert question = new Alert(Alert.AlertType.CONFIRMATION);
			question.setHeaderText("Confirmation");
			question.setContentText("Are you sure you want to save changes to shortcut '" + this.shortcut[0] + "'?");
			Stage stage = (Stage) question.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			Optional<ButtonType> option = question.showAndWait();
			if (option.isPresent() && ButtonType.OK.equals(option.get())) {
				String[] newShortcut = new String[] {
						this.nameTextField.getText(),
						this.parametersTextField.getText(),
						this.bodyTextArea.getText()};
				try {
					this.sqlController.updateShortcut(this.shortcut[0], newShortcut);
					CollectionOfAutoCompletions.resetAutoCompletions();
					this.shortcut = newShortcut;
				} catch (Exception e) {
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					errorAlert.setHeaderText("Operation failed");
					errorAlert.setContentText(e.getMessage());
					Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
					stage2.getIcons().add(getIcon());
					errorAlert.showAndWait();
				}
				setEditable(false);
			}
		} else {
			setEditable(true);
		}
	}

	public void bottomButtonClick() {
		if (this.isEditOn) {
			if (isSame()) {
				setEditable(false);
				return;
			}
			Alert question = new Alert(Alert.AlertType.CONFIRMATION);
			question.setHeaderText("Confirmation");
			question.setContentText("Are you sure you want to discard changes to shortcut '" + this.shortcut[0] + "'?");
			Stage stage = (Stage) question.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			Optional<ButtonType> option = question.showAndWait();
			if (option.isPresent() && ButtonType.OK.equals(option.get())) {
				this.nameTextField.setText(this.shortcut[0]);
				this.parametersTextField.setText(this.shortcut[1]);
				this.bodyTextArea.setText(this.shortcut[2]);
				setEditable(false);
			}
		} else {
			Alert question = new Alert(Alert.AlertType.CONFIRMATION);
			question.setHeaderText("Confirmation");
			question.setContentText("Are you sure you want to remove shortcut '" + this.shortcut[0] + "'?");
			Stage stage = (Stage) question.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			Optional<ButtonType> option = question.showAndWait();
			if (option.isPresent() && ButtonType.OK.equals(option.get())) {
				try {
					this.sqlController.deleteShortcut(this.shortcut[0]);
					CollectionOfAutoCompletions.resetAutoCompletions();
					this.listShortcutsController.removeRowAtIndex(this.id);
				} catch (Exception e) {
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					errorAlert.setHeaderText("Operation failed");
					errorAlert.setContentText(e.getMessage());
					Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
					stage2.getIcons().add(getIcon());
					errorAlert.showAndWait();
				}
			}
		}
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
			this.mainBorderPane.setMinHeight(Math.max(newHeight, 90));
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
}
