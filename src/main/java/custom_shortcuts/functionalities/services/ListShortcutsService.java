package custom_shortcuts.functionalities.services;

import custom_shortcuts.functionalities.services.tasks.ListShortcutsTask;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsController;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsWindow;
import custom_shortcuts.gui.list_shortcuts_window.OneShortcutController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getMainStage;

public class ListShortcutsService extends Service<ListShortcutsTaskOutput> {

	private final ListShortcutsWindow listShortcutsWindow;
	private final Stage mainStage;

	public ListShortcutsService(ListShortcutsWindow listShortcutsWindow) {
		this.listShortcutsWindow = listShortcutsWindow;
		this.mainStage = getMainStage();

		setOnSucceeded(workerStateEvent -> {
			this.mainStage.getScene().setCursor(Cursor.DEFAULT);
			ListShortcutsTaskOutput output = getValue();
			ListShortcutsController listShortcutsController = this.listShortcutsWindow.getController();
			for (OneShortcutController oneShortcutController: output.getControllers()) {
				oneShortcutController.setListShortcutsController(listShortcutsController);
			}
			listShortcutsController.setSubControllers(output.getControllers());
			this.listShortcutsWindow.InitializeAndOpen(output.getRows());
		});

		setOnFailed(workerStateEvent -> {
			this.mainStage.getScene().setCursor(Cursor.DEFAULT);
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Window cannot be opened");
			errorAlert.setContentText(getMessage());
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
		});

		setOnCancelled(workerStateEvent -> this.mainStage.getScene().setCursor(Cursor.DEFAULT));
	}

	public void startService() {
		if (isRunning()) {
			cancel();
		}
		this.mainStage.getScene().setCursor(Cursor.WAIT);
		reset();
		start();
	}


	@Override
	protected Task<ListShortcutsTaskOutput> createTask() {
		return new ListShortcutsTask();
	}
}
