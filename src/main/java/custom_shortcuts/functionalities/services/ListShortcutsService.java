package custom_shortcuts.functionalities.services;

import custom_shortcuts.database.SqlController;
import custom_shortcuts.functionalities.services.tasks.ListShortcutsTask;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsWindow;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class ListShortcutsService extends Service<ArrayList<BorderPane>> {

	private final SqlController sqlController;
	private final ListShortcutsWindow listShortcutsWindow;
	private final Stage mainStage;

	public ListShortcutsService(SqlController sqlController, ListShortcutsWindow listShortcutsWindow, Stage mainStage) {
		this.sqlController = sqlController;
		this.listShortcutsWindow = listShortcutsWindow;
		this.mainStage = mainStage;

		setOnSucceeded(workerStateEvent -> {
			this.mainStage.getScene().setCursor(Cursor.DEFAULT);
			this.listShortcutsWindow.InitializeAndOpen(getValue());
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
	}

	public void startService() {
		if (isRunning()) {
			cancel();
		}
		reset();
		start();
	}


	@Override
	protected Task<ArrayList<BorderPane>> createTask() {
		return new ListShortcutsTask(this.sqlController, this.listShortcutsWindow.getController(), this.mainStage);
	}
}
