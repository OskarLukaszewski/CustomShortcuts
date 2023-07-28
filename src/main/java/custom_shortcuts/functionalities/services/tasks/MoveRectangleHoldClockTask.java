package custom_shortcuts.functionalities.services.tasks;

import javafx.concurrent.Task;

public class MoveRectangleHoldClockTask extends Task<Void> {
	@Override
	protected Void call() throws Exception {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 1000) {
			if (isCancelled()) {
				throw  new InterruptedException();
			}
			Thread.sleep(10);
		}
		return null;
	}
}
