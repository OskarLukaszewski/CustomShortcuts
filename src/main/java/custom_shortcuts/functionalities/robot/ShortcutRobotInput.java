package custom_shortcuts.functionalities.robot;

import custom_shortcuts.database.SqlController;

public class ShortcutRobotInput {

	private final SqlController sqlController;
	private final double[] mousePosition;
	private final String body;

	public ShortcutRobotInput(SqlController sqlController, String rawInput) throws Exception {
		this.sqlController = sqlController;
		this.mousePosition = retrieveMousePosition();
		this.body = createBody(rawInput);
	}

	public double[] getMousePosition() {
		return this.mousePosition;
	}

	public String getBody() {
		return this.body;
	}

	private String createBody(String rawInput) throws Exception {
		String[] input = rawInput.split(" ");
		String[] shortcut = this.sqlController.getShortcut(input[0]);
		String[] parameters = shortcut[1].split(" ");
		String body = shortcut[2];
		for (int i = 1; i < input.length; i++) {
			body = body.replace(parameters[i-1], input[i]);
		}
		return body;
	}

	private double[] retrieveMousePosition() throws Exception {
		return this.sqlController.getMousePosition();
	}
}
