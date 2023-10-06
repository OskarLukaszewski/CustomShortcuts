package custom_shortcuts.functionalities.robot;

import custom_shortcuts.functionalities.services.SchedulePastingPictureService;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.robot.Robot;

public class ShortcutRobot {

	private final Robot robot;
	private final SchedulePastingPictureService schedulePastingPictureService;

	public ShortcutRobot() {
		this.robot = new Robot();
		this.schedulePastingPictureService = new SchedulePastingPictureService(this);
	}

	public void enterShortcut(ShortcutRobotInput shortcutRobotInput) {
		double oldPositionX = this.robot.getMouseX();
		double oldPositionY = this.robot.getMouseY();
		click(shortcutRobotInput.getMousePosition());
		pasteBody(shortcutRobotInput.getBody());
		if (shortcutRobotInput.doesIncludePicture()) {
			this.schedulePastingPictureService.startService(shortcutRobotInput.getPicture());
		}
		returnMouse(oldPositionX, oldPositionY);
	}

	private void click(double[] mousePosition) {
		this.robot.mouseMove(mousePosition[0], mousePosition[1]);
		this.robot.mouseClick(MouseButton.PRIMARY);
	}

	private void pasteBody(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
		this.robot.keyPress(KeyCode.CONTROL);
		this.robot.keyPress(KeyCode.V);
		this.robot.keyRelease(KeyCode.V);
		this.robot.keyRelease(KeyCode.CONTROL);
	}

	public void pastePicture(Image picture) {
		ClipboardContent content = new ClipboardContent();
		content.putImage(picture);
		Clipboard.getSystemClipboard().setContent(content);
		this.robot.keyPress(KeyCode.CONTROL);
		this.robot.keyPress(KeyCode.V);
		this.robot.keyRelease(KeyCode.V);
		this.robot.keyRelease(KeyCode.CONTROL);
	}

	private void returnMouse(double positionX, double positionY) {
		this.robot.mouseMove(positionX, positionY);
	}
}
