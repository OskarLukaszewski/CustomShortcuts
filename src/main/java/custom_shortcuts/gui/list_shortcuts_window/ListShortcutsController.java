package custom_shortcuts.gui.list_shortcuts_window;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ListShortcutsController {

	private GridPane gridPane;
	private ArrayList<OneShortcutController> subControllers;
	private ArrayList<BorderPane> newRows;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private TitledPane titledPane;

	public ListShortcutsController() {
		this.gridPane = new GridPane();
	}

	public void displayShortcuts(ArrayList<BorderPane> newRows) {
		this.newRows = newRows;
		for (BorderPane newRow: this.newRows) {
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

	public GridPane getGridPane() {
		return this.gridPane;
	}

	public void setSubControllers(ArrayList<OneShortcutController> subControllers) {
		this.subControllers = subControllers;
	}

	public void clearSubControllers() {
		for (OneShortcutController controller: this.subControllers) {
			controller = null;
		}
		System.gc();
	}

	public void clearRows() {
		for (BorderPane row: this.newRows) {
			row = null;
		}
		System.gc();
	}
}
