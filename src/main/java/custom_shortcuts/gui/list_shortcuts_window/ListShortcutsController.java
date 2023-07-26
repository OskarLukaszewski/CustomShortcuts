package custom_shortcuts.gui.list_shortcuts_window;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class ListShortcutsController {

	private GridPane gridPane;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private TitledPane titledPane;

	public void addGridPane(GridPane gridPane) {
		this.scrollPane.setContent(gridPane);
		this.gridPane = gridPane;
	}

	public void removeRowAtIndex(int index) {
		this.gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
	}

	public void setFocus() {
		this.titledPane.requestFocus();
	}
}
