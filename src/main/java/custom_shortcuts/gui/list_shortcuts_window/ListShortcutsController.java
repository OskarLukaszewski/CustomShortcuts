package custom_shortcuts.gui.list_shortcuts_window;

import custom_shortcuts.database.SqlController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ListShortcutsController {

	private GridPane gridPane;
	private final SqlController sqlController;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private TitledPane titledPane;

	public ListShortcutsController(SqlController sqlController) {
		this.sqlController = sqlController;
	}

	public void displayShortcuts() throws Exception {
		this.gridPane = new GridPane();
		ArrayList<String[]> shortcuts = this.sqlController.getAllShortcuts();
		for (int i=0; i<shortcuts.size(); i++) {
			FXMLLoader loaderRow = new FXMLLoader(getClass().getResource("OneShortcut.fxml"));
			OneShortcutController oneShortcutController = new OneShortcutController(
					this.sqlController, this, shortcuts.get(i), i);
			loaderRow.setController(oneShortcutController);
			GridPane newRow = loaderRow.load();
			GridPane.setConstraints(newRow, 0, i);
			this.gridPane.getChildren().add(newRow);
		}
		this.scrollPane.setContent(this.gridPane);
	}

	public void removeRowAtIndex(int index) {
		this.gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
	}

	public void setFocus() {
		this.titledPane.requestFocus();
	}
}
