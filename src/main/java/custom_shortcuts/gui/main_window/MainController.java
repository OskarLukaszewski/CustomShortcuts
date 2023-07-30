package custom_shortcuts.gui.main_window;

import custom_shortcuts.animations.HideShowAnimation;
import custom_shortcuts.database.SqlController;
import custom_shortcuts.functionalities.ShortcutRobot;
import custom_shortcuts.functionalities.services.ListShortcutsService;
import custom_shortcuts.functionalities.services.MoveRectangleHoldClockService;
import custom_shortcuts.gui.add_shortcut_window.AddShortcutWindow;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsWindow;
import custom_shortcuts.gui.screenshot_window.ScreenshotWindow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class MainController {

	private final Stage mainStage;
	private final HideShowAnimation hideShowAnimation;
	private final AddShortcutWindow addShortcutWindow;
	private final ScreenshotWindow screenshotWindow;
	private final ListShortcutsService listShortcutsService;
	private final ShortcutRobot shortcutRobot;
	private final MoveRectangleHoldClockService moveRectangleHoldClockService;
	private double yOffset, xOffset;
	private final SqlController sqlController;
	private boolean fullyDraggable, movedAwayFromEdge;

	@FXML
	private Button hideButton, closeButton, addButton, listButton, locationButton;

	@FXML
	private TextField shortcutTextField;

	@FXML
	private ToolBar toolbar;

	@FXML
	private GridPane mainGridPane;

	@FXML
	private FontAwesomeIconView hideIcon, moveRectangleIcon;

	@FXML
	private Rectangle moveRectangle;

	@FXML
	private void initialize() {
		this.moveRectangle.setOnMousePressed(mouseEvent -> {
			this.moveRectangle.setFill(Color.WHITE);
			this.yOffset = mouseEvent.getSceneY();
			if (this.fullyDraggable) {
				this.xOffset = mouseEvent.getSceneX();
			} else {
				this.moveRectangleHoldClockService.startService();
			}
		});
		this.moveRectangle.setOnMouseDragged(mouseEvent -> {
			this.mainStage.setY(mouseEvent.getScreenY() - this.yOffset);
			if (this.fullyDraggable) {
				this.mainStage.setX(mouseEvent.getScreenX() - this.xOffset);
				if (this.movedAwayFromEdge &&
						this.mainStage.getX() + this.mainStage.getWidth() >=
						Screen.getPrimary().getBounds().getWidth()) {
					setDraggable(false);
				}
				if (!this.movedAwayFromEdge && Screen.getPrimary().getBounds().getWidth() - this.mainStage.getX() >
						this.mainStage.getWidth() + 10) {
					this.movedAwayFromEdge = true;
				}
			} else {
				this.moveRectangleHoldClockService.startService();
			}
		});
		this.moveRectangle.setOnMouseReleased(mouseEvent -> {
			this.moveRectangle.setFill(Color.web("#dddddd"));
			if (!this.fullyDraggable) {
				this.moveRectangleHoldClockService.stopService();
			}
		});
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
		this.screenshotWindow = new ScreenshotWindow(this.sqlController, this.mainStage);
		this.listShortcutsService = new ListShortcutsService(this.sqlController, new ListShortcutsWindow(this.sqlController));
		this.shortcutRobot = new ShortcutRobot();
		this.fullyDraggable = false;
		this.movedAwayFromEdge = false;
		this.moveRectangleHoldClockService = new MoveRectangleHoldClockService(this);
		this.yOffset = 0;
		this.xOffset = 0;
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
		this.listShortcutsService.startService();
	}

	public void locationButtonClick() {
		this.mainStage.setAlwaysOnTop(false);
		this.screenshotWindow.open();
	}

	public void setDraggable(boolean fullyDraggable) {
		this.fullyDraggable = fullyDraggable;
		this.hideShowAnimation.setFullyDraggable(fullyDraggable);
		if (fullyDraggable) {
			this.moveRectangleIcon.setIcon(FontAwesomeIcon.ARROWS);
		} else {
			this.moveRectangleIcon.setIcon(FontAwesomeIcon.ARROWS_V);
			this.movedAwayFromEdge = false;
			this.mainStage.setX(Screen.getPrimary().getBounds().getWidth() - this.mainStage.getWidth());
		}
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