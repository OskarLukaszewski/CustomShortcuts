package custom_shortcuts.functionalities.services;

import custom_shortcuts.functionalities.services.tasks.MoveRectangleHoldClockTask;
import custom_shortcuts.gui.main_window.MainController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MoveRectangleHoldClockService extends Service<Void> {

	private final MainController mainController;

	public MoveRectangleHoldClockService(MainController mainController) {
		this.mainController = mainController;
		setOnSucceeded(workerStateEvent -> this.mainController.setDraggable(true));
	}

	public void startService() {
		if (isRunning()) {
			cancel();
		}
		reset();
		start();
	}

	public void stopService() {
		if (isRunning()) {
			cancel();
		}
	}

	@Override
	protected Task<Void> createTask() {
		return new MoveRectangleHoldClockTask();
	}
}
