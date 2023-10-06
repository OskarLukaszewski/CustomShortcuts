package custom_shortcuts.gui.screenshot_window;

import custom_shortcuts.utils.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getMainStage;

public class ScreenshotWindow {

	private final Stage screenshotStage, mainStage;
	private final ScreenshotController controller;
	private boolean isOpened;

	public ScreenshotWindow() {
		this.screenshotStage = new Stage();
		this.mainStage = getMainStage();
		this.screenshotStage.initStyle(StageStyle.UNDECORATED);
		this.screenshotStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
				close();
			}
		});
		this.controller = new ScreenshotController(this);
		this.isOpened = false;
	}

	public void open() {
		if (this.isOpened) {
			this.screenshotStage.requestFocus();
		}
		else {
			initializeAndOpen();
		}

	}

	private void initializeAndOpen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScreenshotWindow.fxml"));
			loader.setController(this.controller);
			Scene scene = new Scene(loader.load());
			this.mainStage.hide();
			this.controller.setImage();
			this.screenshotStage.setWidth(Screen.getPrimary().getBounds().getWidth());
			this.screenshotStage.setHeight(Screen.getPrimary().getBounds().getHeight());
			this.screenshotStage.setResizable(false);
			this.screenshotStage.setScene(scene);
			this.screenshotStage.setTitle("Custom Shortcuts");
			this.screenshotStage.getIcons().add(getIcon());
			this.screenshotStage.getScene().setCursor(Cursor.CROSSHAIR);
			this.screenshotStage.setOnCloseRequest(windowEvent -> {
				this.isOpened = false;
				this.mainStage.show();
			});
			this.screenshotStage.show();
			this.isOpened = true;
		} catch (IOException e) {
			this.mainStage.show();
			this.isOpened = false;
			Alerts.showErrorAlert("Window cannot be opened", e.getMessage());
		}
	}

	public void close() {
		this.screenshotStage.close();
		this.isOpened = false;
		this.mainStage.show();
	}
}
