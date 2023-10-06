module custom_shortcuts {

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.controlsfx.controls;
	requires de.jensd.fx.glyphs.fontawesome;
	requires java.sql;

	exports custom_shortcuts.gui.main_window;
	opens custom_shortcuts.gui.main_window to javafx.fxml;
	exports custom_shortcuts.gui.add_shortcut_window;
	opens custom_shortcuts.gui.add_shortcut_window to javafx.fxml;
	exports custom_shortcuts.gui.list_shortcuts_window;
	opens custom_shortcuts.gui.list_shortcuts_window to javafx.fxml;
	exports custom_shortcuts.gui.screenshot_window;
	opens custom_shortcuts.gui.screenshot_window to javafx.fxml;
	exports custom_shortcuts.database;
	exports custom_shortcuts.gui.show_picture_window;
	opens custom_shortcuts.gui.show_picture_window to javafx.fxml;
}