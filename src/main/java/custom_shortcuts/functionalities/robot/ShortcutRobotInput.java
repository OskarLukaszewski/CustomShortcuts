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
		int indexOfFirstSpace = rawInput.indexOf(' ');
		String[] inputParameters;
		if (indexOfFirstSpace != -1 && indexOfFirstSpace + 1 < rawInput.length()) {
			String rawInputParameters = rawInput.substring(indexOfFirstSpace + 1);
			inputParameters = rawInputParameters.split(";");
		} else {
			inputParameters = new String[0];
		}

		String shortcutName = rawInput.split(" ")[0];
		String[] shortcut = this.sqlController.getShortcut(shortcutName);
		String[] parameters = shortcut[1].split(";");

		String body = shortcut[2];
		for (int i = 0; i < Math.min(inputParameters.length, parameters.length); i++) {
			body = body.replace(parameters[i], inputParameters[i]);
		}

		return body;
	}

	private double[] retrieveMousePosition() throws Exception {
		return this.sqlController.getMousePosition();
	}
}
