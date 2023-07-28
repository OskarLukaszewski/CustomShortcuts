package custom_shortcuts.gui.main_window;

import custom_shortcuts.animations.HideShowAnimation;
import custom_shortcuts.database.SqlController;
import custom_shortcuts.functionalities.ShortcutRobot;
import custom_shortcuts.gui.add_shortcut_window.AddShortcutWindow;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsWindow;
import custom_shortcuts.gui.screenshot_window.ScreenshotWindow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class MainController {

	private final Stage mainStage;
	private final HideShowAnimation hideShowAnimation;
	private final AddShortcutWindow addShortcutWindow;
	private final ScreenshotWindow screenshotWindow;
	private final ListShortcutsWindow listShortcutsWindow;
	private final ShortcutRobot shortcutRobot;
	private double yOffset = 0;
	private final SqlController sqlController;

	@FXML
	private Button hideButton, closeButton, addButton, listButton, locationButton;

	@FXML
	private TextField shortcutTextField;

	@FXML
	private ToolBar toolbar;

	@FXML
	private GridPane mainGridPane;

	@FXML
	private FontAwesomeIconView hideIcon;

	@FXML
	private Rectangle moveRectangle;

	@FXML
	private void initialize() {
		this.moveRectangle.setOnMousePressed(mouseEvent -> {
			this.moveRectangle.setFill(Color.WHITE);
			this.yOffset = mouseEvent.getSceneY();
		});
		this.moveRectangle.setOnMouseDragged(mouseEvent -> this.mainStage.setY(mouseEvent.getScreenY() - this.yOffset));
		this.moveRectangle.setOnMouseReleased(mouseEvent -> this.moveRectangle.setFill(Color.web("#dddddd")));
		this.shortcutTextField.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER)) {
				enterShortcut();
			}
		});
		this.hideShowAnimation.setHideIcon(this.hideIcon);
	}

	public MainController(Stage stage) {
		this.mainStage = stage;
		this.hideShowAnimation = new HideShowAnimation(this.mainStage);
		File dataFolder = createDataFolder();
		this.sqlController = new SqlController("jdbc:sqlite:" + dataFolder.getPath() + "\\shortcuts.db");
		this.addShortcutWindow = new AddShortcutWindow(this.sqlController);
		this.screenshotWindow = new ScreenshotWindow(this.sqlController);
		this.listShortcutsWindow = new ListShortcutsWindow(this.sqlController);
		this.shortcutRobot = new ShortcutRobot();
	}

	public void hideButtonClick() {
		this.hideShowAnimation.play();
	}

	public void closeButtonClick() {
		this.sqlController.close();
		Platform.exit();
	}

	public void addButtonClick() {
		this.addShortcutWindow.open();
	}

	public void listButtonClick() {
		this.listShortcutsWindow.open();
	}

	public void locationButtonClick() {
		this.screenshotWindow.open();
	}

	private void enterShortcut() {
		String[] input = this.shortcutTextField.getText().split(" ");
		try {
			double[] mousePosition = this.sqlController.getMousePosition();
			String[] shortcut = this.sqlController.getShortcut(input[0]);
			String[] parameters = shortcut[1].split(" ");
			String body = shortcut[2];
			for (int i = 1; i < input.length; i++) {
				body = body.replace(parameters[i-1], input[i]);
			}
			this.shortcutRobot.enterShortcut(mousePosition, body);
			this.shortcutTextField.setText("");
		} catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Database error");
			errorAlert.setContentText("Couldn't construct message from database.");
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
		}
	}

	private File createDataFolder() {
		File dataFile = new File(System.getenv("APPDATA") + "\\CustomShortcuts");
		if(!dataFile.exists()) {
			if (!dataFile.mkdir()) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setHeaderText("Application cannot be opened.");
				errorAlert.setContentText("Failed to create a data folder at" + dataFile.getPath() + ".");
				Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
				stage2.getIcons().add(getIcon());
				errorAlert.showAndWait();
				Platform.exit();
			}
		}
		return dataFile;
	}
}