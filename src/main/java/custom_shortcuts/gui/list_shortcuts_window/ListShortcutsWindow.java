package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.database.SqlController;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ListShortcutsWindow {

	private final SqlController sqlController;
	private final Stage listShortcutsStage;
	private final ListShortcutsController listShortcutsController;
	private boolean isOpened;

	public ListShortcutsWindow(SqlController sqlController) {
		this.sqlController = sqlController;
		this.listShortcutsStage = new Stage();
		this.listShortcutsController = new ListShortcutsController();
		this.isOpened = false;
	}

	public void open() {
		if (this.isOpened) {
			this.listShortcutsStage.requestFocus();
		}
		else {
			initializeAndOpen();
		}

	}

	public void setFocus() {
		this.listShortcutsController.setFocus();
	}

	public void hideRow(int index) {
		this.listShortcutsController.removeRowAtIndex(index);
	}

	private void initializeAndOpen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListOfShortcuts.fxml"));
			loader.setController(this.listShortcutsController);
			Scene scene = new Scene(loader.load());
			this.listShortcutsStage.setScene(scene);
			this.listShortcutsStage.setTitle("Custom Shortcuts");
			this.listShortcutsStage.getIcons().add(getIcon());
			this.listShortcutsStage.setResizable(false);
			this.listShortcutsStage.setOnCloseRequest(windowEvent -> this.isOpened = false);

			GridPane gridPane = new GridPane();
			ArrayList<String[]> shortcuts = this.sqlController.getAllShortcuts();
			for (int i=0; i<shortcuts.size(); i++) {
				FXMLLoader loaderRow = new FXMLLoader(getClass().getResource("OneShortcut.fxml"));
				OneShortcutController oneShortcutController = new OneShortcutController(
						this.sqlController, this, shortcuts.get(i), i);
				loaderRow.setController(oneShortcutController);
				GridPane newRow = loaderRow.load();
				GridPane.setConstraints(newRow, 0, i);
				gridPane.getChildren().add(newRow);
			}
			this.listShortcutsController.addGridPane(gridPane);

			this.listShortcutsStage.show();
			this.listShortcutsController.setFocus();
			this.isOpened = true;
		} catch (Exception e) {
			this.isOpened = false;
			e.printStackTrace();
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Window cannot be opened");
			errorAlert.setContentText(e.getMessage());
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(getIcon());
			errorAlert.showAndWait();
		}
	}
}
