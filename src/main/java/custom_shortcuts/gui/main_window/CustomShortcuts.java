package custom_shortcuts.gui.main_window;

import custom_shortcuts.database.DataFolder;
import custom_shortcuts.database.SqlController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.io.InputStream;

public class CustomShortcuts extends Application {

	private static Image icon = null;
	private static MainController mainController;
	private static Stage mainStage;

	@Override
	public void start(Stage stage) throws IOException {
		mainStage = stage;
		mainController = new MainController(stage);
		FXMLLoader fxmlLoader = new FXMLLoader(CustomShortcuts.class.getResource("CustomShortcuts.fxml"));
		fxmlLoader.setController(mainController);
		Scene scene = new Scene(fxmlLoader.load());
		stage.setX(Screen.getPrimary().getBounds().getWidth()-230);
		stage.setY(Screen.getPrimary().getBounds().getHeight()/3);
		stage.setAlwaysOnTop(true);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Custom Shortcuts");
		InputStream iconPath = CustomShortcuts.class.getResourceAsStream("icons/icon.png");
		if (iconPath != null) {
			Image ico = new Image(iconPath);
			icon = ico;
			stage.getIcons().add(ico);
		}
		stage.setOnCloseRequest(windowEvent -> Platform.exit());
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	public static Image getIcon() {
		return icon;
	}

	public static MainController getMainController() {
		return mainController;
	}

	public static Stage getMainStage() {
		return mainStage;
	}

	public static SqlController getSqlController() {
		return mainController.getSqlController();
	}

	public static DataFolder getDataFolder() {
		return mainController.getDataFolder();
	}

}