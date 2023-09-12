package custom_shortcuts.functionalities.services;

import custom_shortcuts.functionalities.services.tasks.MoveRectangleHoldClockTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getMainController;

public class MoveRectangleHoldClockService extends Service<Void> {

	public MoveRectangleHoldClockService() {
		setOnSucceeded(workerStateEvent -> getMainController().setDraggable(true));
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
