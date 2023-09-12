package custom_shortcuts.gui.screenshot_window;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;

public class ScreenshotController {

	private final ScreenshotWindow screenshotWindow;

	@FXML
	private ImageView imageView;

	@FXML
	private void initialize() {
		this.imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
				boolean succeeded = setMousePosition(new double[] {mouseEvent.getScreenX(), mouseEvent.getScreenY()});
				if (succeeded) {
					close();
				}
			} else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
				close();
			}
		});
	}

	public ScreenshotController(ScreenshotWindow screenshotWindow) {
		this.screenshotWindow = screenshotWindow;
	}

	public void setImage() {
		Robot robot = new Robot();
		WritableImage image = robot.getScreenCapture(null, Screen.getPrimary().getBounds());
		this.imageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
		this.imageView.setFitHeight(Screen.getPrimary().getBounds().getHeight());
		this.imageView.setImage(image);
	}

	private boolean setMousePosition(double[] mousePosition) {
		try {
			getSqlController().updateMousePosition(mousePosition);
			return true;
		} catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Error during updating mouse position");
			errorAlert.setContentText(e.getMessage());
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
			return false;
		}
	}

	private void close() {
		this.screenshotWindow.close();
	}
}
