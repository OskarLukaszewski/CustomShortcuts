package custom_shortcuts.functionalities.autocompletion;

import org.controlsfx.control.textfield.AutoCompletionBinding;

public class SuggestionProviderRequest implements AutoCompletionBinding.ISuggestionRequest {

	private final String prompt;

	public SuggestionProviderRequest(String prompt) {
		this.prompt = prompt;
	}
	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public String getUserText() {
		return prompt;
	}
}
