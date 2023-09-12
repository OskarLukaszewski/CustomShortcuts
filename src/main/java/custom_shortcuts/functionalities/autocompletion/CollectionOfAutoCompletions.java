package custom_shortcuts.functionalities.autocompletion;

import java.util.ArrayList;
import java.util.List;

public class CollectionOfAutoCompletions {

	private static final List<ShortcutAutoComplete> listOfAutoCompletions = new ArrayList<>();

	public static void add(ShortcutAutoComplete shortcutAutocomplete) {
		listOfAutoCompletions.add(shortcutAutocomplete);
	}

	public static void resetAutoCompletions() {
		for (ShortcutAutoComplete shortcutAutocomplete: listOfAutoCompletions) {
			shortcutAutocomplete.reset();
		}
	}

	public static void remove(ShortcutAutoComplete shortcutAutoComplete) {
		listOfAutoCompletions.remove(shortcutAutoComplete);
	}
}
