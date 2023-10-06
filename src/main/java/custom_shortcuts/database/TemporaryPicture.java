package custom_shortcuts.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemporaryPicture extends File {

	private final String originalExtension;
	private String currentName;

	public TemporaryPicture(String pathname, String originalExtension) {
		super(pathname);
		this.originalExtension = originalExtension;
		this.currentName = this.getName();
	}

	public boolean permanentlyAddPicture() {
		String newName = this.getName()
				.substring(0, this.getName().lastIndexOf(".tmp"))
				+ this.originalExtension;
		try {
			Files.move(this.toPath(), this.toPath().resolveSibling(newName));
			this.currentName = newName;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public String getCurrentName() {
		return this.currentName;
	}
}
