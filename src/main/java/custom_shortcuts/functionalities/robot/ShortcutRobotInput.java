package custom_shortcuts.functionalities.robot;

import custom_shortcuts.database.SqlControllerException;
import javafx.scene.image.Image;

import java.io.File;

import static custom_shortcuts.gui.main_window.CustomShortcuts.getSqlController;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getDataFolder;

public class ShortcutRobotInput {

	private final double[] mousePosition;
	private final String body;
	private final boolean includesPicture;
	private final Image picture;

	public ShortcutRobotInput(String rawInput) throws SqlControllerException {
		this.mousePosition = retrieveMousePosition();
		String name = getShortcutName(rawInput);
		String[] shortcut = getSqlController().getShortcut(name);
		this.body = createBody(rawInput, shortcut);
		this.includesPicture = shortcut[3].equals("true");
		if (this.includesPicture) {
			this.picture = new Image("file:" + new File(getDataFolder().getPathToPicture(shortcut[4]).toString()));
		} else {
			this.picture = null;
		}
	}

	public double[] getMousePosition() {
		return this.mousePosition;
	}

	public String getBody() {
		return this.body;
	}

	public boolean doesIncludePicture() {
		return this.includesPicture;
	}

	public Image getPicture() {
		return this.picture;
	}

	private String createBody(String rawInput, String[] shortcut) {
		int indexOfFirstSpace = rawInput.indexOf(' ');
		String[] inputParameters;
		if (indexOfFirstSpace != -1 && indexOfFirstSpace + 1 < rawInput.length()) {
			String rawInputParameters = rawInput.substring(indexOfFirstSpace + 1);
			inputParameters = rawInputParameters.split(";");
		} else {
			inputParameters = new String[0];
		}

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

	private String getShortcutName(String rawInput) {
		return rawInput.split(" ")[0];
	}

	private double[] retrieveMousePosition() throws SqlControllerException {
		return getSqlController().getMousePosition();
	}
}
