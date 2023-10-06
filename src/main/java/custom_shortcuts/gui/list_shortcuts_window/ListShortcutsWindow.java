package custom_shortcuts.gui.list_shortcuts_window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getMainStage;

public class ListShortcutsWindow {

	private final Stage listShortcutsStage;
	private ListShortcutsController listShortcutsController;
	private boolean isOpened;

	public ListShortcutsWindow() {
		this.listShortcutsStage = new Stage();
		this.listShortcutsController = new ListShortcutsController();
		this.isOpened = false;
	}

	public void InitializeAndOpen(List<BorderPane> newRows) {
		if (this.isOpened) {
			this.listShortcutsStage.setIconified(false);
			this.listShortcutsStage.requestFocus();
		}
		else {
			initializeAndOpen(newRows);
		}
	}

	public void focus() {
		this.listShortcutsStage.setIconified(false);
		this.listShortcutsStage.requestFocus();
	}

	public ListShortcutsController getController() {
		return this.listShortcutsController;
	}

	public boolean isOpen() {
		return this.isOpened;
	}

	private void initializeAndOpen(List<BorderPane> newRows) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListOfShortcuts.fxml"));
			loader.setController(this.listShortcutsController);
			Scene scene = new Scene(loader.load());
			this.listShortcutsStage.setScene(scene);
			this.listShortcutsStage.setTitle("Custom Shortcuts");
			this.listShortcutsStage.getIcons().add(getIcon());
			this.listShortcutsStage.setMinWidth(632);
			this.listShortcutsStage.setOnCloseRequest(windowEvent -> onClose());
			this.listShortcutsStage.maximizedProperty().addListener((observableValue, aBoolean, t1) ->
					getMainStage().setIconified(t1));
			this.listShortcutsController.loadSearchTab();
			this.listShortcutsController.displayShortcuts(newRows);
			this.listShortcutsStage.show();
			this.listShortcutsController.setFocus();
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

	private void onClose() {
		this.listShortcutsStage.hide();
		this.isOpened = false;
		this.listShortcutsController.clearRows();
		this.listShortcutsController.clearSubControllers();
		this.listShortcutsController = new ListShortcutsController();
		System.gc();
	}
}
