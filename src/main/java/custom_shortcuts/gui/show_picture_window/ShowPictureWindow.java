package custom_shortcuts.gui.show_picture_window;

import custom_shortcuts.utils.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class ShowPictureWindow {

	private final Stage showPictureStage;
	private final ShowPictureController controller;
	private boolean isOpened;

	public ShowPictureWindow() {
		this.showPictureStage = new Stage();
		this.controller = new ShowPictureController();
		this.isOpened = false;
	}

	public void open(Image picture) {
		if (this.isOpened) {
			this.controller.setPicture(picture);
			this.showPictureStage.requestFocus();
			this.showPictureStage.sizeToScene();
		} else {
			initializeAndOpen(picture);
		}
	}

	private void initializeAndOpen(Image picture) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowPicture.fxml"));
			loader.setController(this.controller);
			Scene scene = new Scene(loader.load());
			this.controller.setPicture(picture);
			this.showPictureStage.setTitle("Custom Shortcuts");
			this.showPictureStage.getIcons().add(getIcon());
			this.showPictureStage.setResizable(false);
			this.showPictureStage.setOnCloseRequest(windowEvent -> this.isOpened = false);
			this.showPictureStage.setScene(scene);
			this.showPictureStage.sizeToScene();
			this.showPictureStage.show();
			this.isOpened = true;
		} catch (IOException e) {
			this.isOpened = false;
			Alerts.showErrorAlert("Window cannot be opened", e.getMessage());
		}
	}
}
