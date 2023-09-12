package custom_shortcuts.functionalities.services.tasks;

import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsController;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsWindow;
import custom_shortcuts.gui.list_shortcuts_window.OneShortcutController;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;

public class ListShortcutsTask extends Task<List<BorderPane>> {

	private final ListShortcutsController listShortcutsController;

	public ListShortcutsTask(ListShortcutsController listShortcutsController) {
		this.listShortcutsController = listShortcutsController;
	}
	@Override
	protected List<BorderPane> call() throws Exception {
		List<String[]> shortcuts = getSqlController().getAllShortcuts().stream()
				.sorted(Comparator.comparing(arr -> arr[0]))
				.collect(Collectors.toList());
		List<BorderPane> newRows = new ArrayList<>();
		List<OneShortcutController> controllers = new ArrayList<>();
		for (int i=0; i<shortcuts.size(); i++) {
			FXMLLoader loaderRow = new FXMLLoader(ListShortcutsWindow.class.getResource("OneShortcut.fxml"));
			OneShortcutController oneShortcutController = new OneShortcutController(
					getSqlController(), this.listShortcutsController, shortcuts.get(i), i+2);
			controllers.add(oneShortcutController);
			loaderRow.setController(oneShortcutController);
			BorderPane newRow = loaderRow.load();
			GridPane.setConstraints(newRow, 0, i+2);
			newRows.add(newRow);
		}
		this.listShortcutsController.setSubControllers(controllers);
		return newRows;
	}
}
