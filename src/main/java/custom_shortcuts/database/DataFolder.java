package custom_shortcuts.database;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.File;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class DataFolder extends File {

	public DataFolder() {

		super(System.getenv("APPDATA") + "\\CustomShortcuts");

		if(!this.exists()) {
			if (!this.mkdir()) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setHeaderText("Application cannot be opened.");
				errorAlert.setContentText("Failed to create a data folder at" + this.getPath() + ".");
				Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
				stage2.getIcons().add(getIcon());
				errorAlert.showAndWait();
				Platform.exit();
			}
		}
	}
}
