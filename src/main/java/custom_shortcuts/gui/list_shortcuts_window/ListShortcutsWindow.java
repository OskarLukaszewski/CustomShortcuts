package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.database.SqlController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class ListShortcutsWindow {

	private final Stage listShortcutsStage;
	private final ListShortcutsController listShortcutsController;
	private boolean isOpened;

	public ListShortcutsWindow(SqlController sqlController) {
		this.listShortcutsStage = new Stage();
		this.listShortcutsController = new ListShortcutsController();
		this.isOpened = false;
	}

	public void open(ArrayList<BorderPane> newRows) {
		if (this.isOpened) {
			this.listShortcutsStage.requestFocus();
		}
		else {
			initializeAndOpen(newRows);
		}
	}

	public ListShortcutsController getController() {
		return this.listShortcutsController;
	}

	private void initializeAndOpen(ArrayList<BorderPane> newRows) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListOfShortcuts.fxml"));
			loader.setController(this.listShortcutsController);
			Scene scene = new Scene(loader.load());
			this.listShortcutsStage.setScene(scene);
			this.listShortcutsStage.setTitle("Custom Shortcuts");
			this.listShortcutsStage.getIcons().add(getIcon());
			this.listShortcutsStage.setMinWidth(632);
			this.listShortcutsStage.setOnCloseRequest(windowEvent -> this.isOpened = false);
			this.listShortcutsController.displayShortcuts(newRows);
			this.listShortcutsStage.show();
			this.listShortcutsController.setFocus();
			this.isOpened = true;
		} catch (Exception e) {
			this.isOpened = false;
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Window cannot be opened");
			errorAlert.setContentText(e.getMessage());
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
		}
	}
}
