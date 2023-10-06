package custom_shortcuts.functionalities.services;

import custom_shortcuts.functionalities.robot.ShortcutRobot;
import custom_shortcuts.functionalities.services.tasks.SchedulePastingPictureTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class SchedulePastingPictureService extends Service<Void> {

	private final ShortcutRobot shortcutRobot;
	private Image picture;

	public SchedulePastingPictureService(ShortcutRobot shortcutRobot) {
		this.shortcutRobot = shortcutRobot;
		setOnSucceeded(workerStateEvent -> this.shortcutRobot.pastePicture(this.picture));
	}

	public void startService(Image picture) {
		if (isRunning()) {
			cancel();
		}
		this.picture = picture;
		reset();
		start();
	}

	@Override
	protected Task<Void> createTask() {
		return new SchedulePastingPictureTask();
	}
}
