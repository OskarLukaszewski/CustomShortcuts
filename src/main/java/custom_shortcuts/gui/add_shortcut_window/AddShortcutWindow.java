package custom_shortcuts.gui.add_shortcut_window;

import custom_shortcuts.utils.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class AddShortcutWindow {

	private final Stage addShortcutStage;
	private boolean isInitialized, isOpened;

	public AddShortcutWindow() {
		this.addShortcutStage = new Stage();
		this.isInitialized = false;
		this.isOpened = false;
	}

	public void open() {
		if (this.isOpened) {
			this.addShortcutStage.setIconified(false);
			this.addShortcutStage.requestFocus();
		}
		if (!this.isInitialized) {
			initializeAndOpen();
		} else {
			this.addShortcutStage.show();
			this.isOpened = true;
		}

	}

	private void initializeAndOpen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddShortcutWindow.fxml"));
			AddShortcutController addShortcutController =
					new AddShortcutController(this.addShortcutStage);
			loader.setController(addShortcutController);
			Scene scene = new Scene(loader.load());
			this.addShortcutStage.setScene(scene);
			this.addShortcutStage.setTitle("Custom Shortcuts");
			this.addShortcutStage.getIcons().add(getIcon());
			this.addShortcutStage.setMinWidth(500);
			this.addShortcutStage.setMinHeight(300);
			this.addShortcutStage.setOnCloseRequest(windowEvent -> this.isOpened = false);
			this.isInitialized = true;
			this.addShortcutStage.show();
			this.isOpened = true;
		} catch (IOException e) {
			this.isInitialized = false;
			this.isOpened = false;
			Alerts.showErrorAlert("Window cannot be opened", e.getMessage());
		}
	}
}
