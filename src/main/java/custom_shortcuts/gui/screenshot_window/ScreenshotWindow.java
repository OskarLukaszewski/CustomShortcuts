package custom_shortcuts.gui.screenshot_window;

import custom_shortcuts.database.SqlController;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ScreenshotWindow {

	private final Stage screenshotStage;
	private final ScreenshotController controller;
	private boolean isOpened;

	public ScreenshotWindow(SqlController sqlController) {
		this.screenshotStage = new Stage();
		this.screenshotStage.initStyle(StageStyle.UNDECORATED);
		this.screenshotStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
				close();
			}
		});
		this.controller = new ScreenshotController(sqlController, this);
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
			this.controller.setImage();
			this.screenshotStage.setFullScreen(true);
			this.screenshotStage.setResizable(false);
			this.screenshotStage.setScene(scene);
			this.screenshotStage.setTitle("Custom Shortcuts");
			this.screenshotStage.getIcons().add(getIcon());
			this.screenshotStage.setOnCloseRequest(windowEvent -> this.isOpened = false);
			this.screenshotStage.show();
			this.isOpened = true;
		} catch (IOException e) {
			this.isOpened = false;
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Window cannot be opened");
			errorAlert.setContentText(e.getMessage());
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
		}
	}

	public void close() {
		this.screenshotStage.close();
		this.isOpened = false;
	}
}
