package custom_shortcuts.functionalities;

import javafx.scene.input.*;
import javafx.scene.robot.Robot;

public class ShortcutRobot {

	private final Robot robot;

	public ShortcutRobot() {
		this.robot = new Robot();
	}

	public void enterShortcut(double[] mousePosition, String text) {
		double oldPositionX = this.robot.getMouseX();
		double oldPositionY = this.robot.getMouseY();
		click(mousePosition);
		paste(text);
		enter();
		returnMouse(oldPositionX, oldPositionY);
	}

	private void click(double[] mousePosition) {
		this.robot.mouseMove(mousePosition[0], mousePosition[1]);
		this.robot.mouseClick(MouseButton.PRIMARY);
	}

	private void paste(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
		this.robot.keyPress(KeyCode.CONTROL);
		this.robot.keyPress(KeyCode.V);
		this.robot.keyRelease(KeyCode.V);
		this.robot.keyRelease(KeyCode.CONTROL);
	}

	private void enter() {
		this.robot.keyPress(KeyCode.ENTER);
		this.robot.keyRelease(KeyCode.ENTER);
	}

	private void returnMouse(double positionX, double positionY) {
		this.robot.mouseMove(positionX, positionY);
	}
}
