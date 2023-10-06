package custom_shortcuts.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class Alerts {

	public static void showErrorAlert(String header, String body) {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setHeaderText(header);
		errorAlert.setContentText(body);
		Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(getIcon());
		errorAlert.showAndWait();
	}

	public static void showInformationAlert(String header, String body) {
		Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
		informationAlert.setHeaderText(header);
		informationAlert.setContentText(body);
		Stage stage = (Stage) informationAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(getIcon());
		informationAlert.showAndWait();
	}

	public static boolean showConfirmationAlert(String header, String body) {
		Alert questionAlert = new Alert(Alert.AlertType.CONFIRMATION);
		questionAlert.setHeaderText(header);
		questionAlert.setContentText(body);
		Stage stage = (Stage) questionAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(getIcon());
		Optional<ButtonType> option = questionAlert.showAndWait();
		return option.filter(ButtonType.OK::equals).isPresent();
	}
}
