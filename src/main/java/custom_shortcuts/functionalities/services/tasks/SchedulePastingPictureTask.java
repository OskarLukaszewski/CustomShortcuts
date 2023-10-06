package custom_shortcuts.functionalities.services.tasks;

import javafx.concurrent.Task;

public class SchedulePastingPictureTask extends Task<Void> {

	@Override
	protected Void call() throws Exception {
		Thread.sleep(10);
		return null;
	}
}
