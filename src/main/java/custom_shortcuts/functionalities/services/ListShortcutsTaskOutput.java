package custom_shortcuts.functionalities.services;

import custom_shortcuts.gui.list_shortcuts_window.OneShortcutController;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class ListShortcutsTaskOutput {

	private final List<BorderPane> rows;
	private final List<OneShortcutController> controllers;

	public ListShortcutsTaskOutput(List<BorderPane> rows, List<OneShortcutController> controllers) {
		this.rows = rows;
		this.controllers = controllers;
	}

	public List<BorderPane> getRows() {
		return this.rows;
	}

	public List<OneShortcutController> getControllers() {
		return this.controllers;
	}
}
