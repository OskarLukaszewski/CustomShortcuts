package custom_shortcuts.gui.list_shortcuts_window;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.util.List;

public class ListShortcutsController {

	private GridPane gridPane;
	private BorderPane searchTabBorderPane;
	private List<OneShortcutController> subControllers;
	private SearchTabController searchTabController;
	private List<BorderPane> newRows;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private TitledPane titledPane;

	@FXML
	private void initialize() {
		this.titledPane.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
			final KeyCombination keyComb = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
			if (keyComb.match(keyEvent) &&
					this.gridPane.getChildren().stream().noneMatch(node -> GridPane.getRowIndex(node) == 0)) {
				this.gridPane.getChildren().add(this.searchTabBorderPane);
				this.searchTabController.requestFocus();
			}
		});
	}

	public ListShortcutsController() {
		this.gridPane = new GridPane();
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		this.gridPane.getColumnConstraints().add(columnConstraints);
	}

	public void displayShortcuts(List<BorderPane> newRows) {
		this.newRows = newRows;
		this.gridPane.getChildren().addAll(newRows);
		this.scrollPane.setContent(this.gridPane);
	}

	public void removeRowAtIndex(int index) {
		this.gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
	}

	public void returnTopRowToOriginalPosition() {
		this.subControllers.stream()
				.filter(OneShortcutController::isMovedToTop)
				.findFirst()
				.ifPresent(OneShortcutController::moveRowToOriginalPosition);
	}

	public void moveRowToTopByShortcutName(String shortcutName) {
		this.subControllers.stream()
				.filter(oneShortcutController -> oneShortcutController.getShortcutName().equals(shortcutName))
				.findFirst()
				.ifPresent(OneShortcutController::moveRowToTop);
	}

	public void setFocus() {
		this.titledPane.requestFocus();
	}

	public void setSubControllers(List<OneShortcutController> subControllers) {
		this.subControllers = subControllers;
	}

	public void clearSubControllers() {
		for (OneShortcutController controller: this.subControllers) {
			controller = null;
		}
		this.searchTabController.clearAutoCompletion();
		this.searchTabController = null;
		System.gc();
	}

	public void clearRows() {
		for (BorderPane row: this.newRows) {
			row = null;
		}
		this.searchTabBorderPane = null;
		this.gridPane = null;
		System.gc();
	}

	public void loadSearchTab() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchTab.fxml"));
		this.searchTabController = new SearchTabController(this);
		loader.setController(this.searchTabController);
		this.searchTabBorderPane = loader.load();
	}
}
