package custom_shortcuts.functionalities.services;

import custom_shortcuts.functionalities.services.tasks.ResizeListOfShortcutsClockTask;
import custom_shortcuts.gui.list_shortcuts_window.ListShortcutsController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ResizeListOfShortcutsClockService extends Service<Void> {

	private double targetWidth;
	private final ListShortcutsController listShortcutsController;

	public ResizeListOfShortcutsClockService(ListShortcutsController listShortcutsController) {
		this.listShortcutsController = listShortcutsController;
		setOnSucceeded(workerStateEvent -> this.listShortcutsController.resizeNow(this.targetWidth));
	}

	public void startService(double targetWidth) {
		if (isRunning()) {
			cancel();
		}
		this.targetWidth = targetWidth;
		try {
			reset();
		} catch (IllegalStateException e) {
			return;
		}
		start();
	}

	@Override
	protected Task<Void> createTask() {
		return new ResizeListOfShortcutsClockTask();
	}
}
