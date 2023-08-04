package custom_shortcuts.functionalities.autocompletion;

import custom_shortcuts.database.SqlController;
import javafx.util.Callback;
import java.util.List;

public class SuggestionProvider implements Callback<SuggestionProviderRequest, List<String>> {

	private final SqlController sqlController;

	public SuggestionProvider(SqlController sqlController) {
		this.sqlController = sqlController;
	}

	@Override
	public List<String> call(SuggestionProviderRequest suggestionProviderRequest) {
		return this.sqlController.getSuggestedShortcuts(suggestionProviderRequest.getUserText());
	}
}
