package custom_shortcuts.functionalities.robot;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;

public class ShortcutRobotInput {

	private final double[] mousePosition;
	private final String body;

	public ShortcutRobotInput(String rawInput) throws Exception {
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
		String[] shortcut = getSqlController().getShortcut(shortcutName);

		String[] parameters;
		if (!shortcut[1].equals("")) {
			parameters = shortcut[1].split(";");
		} else {
			parameters = new String[0];
		}

		String body = shortcut[2];
		for (int i = 0; i < Math.min(inputParameters.length, parameters.length); i++) {
			body = body.replace(parameters[i], inputParameters[i]);
		}

		return body;
	}

	private double[] retrieveMousePosition() throws Exception {
		return getSqlController().getMousePosition();
	}
}
