package custom_shortcuts.database;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getIcon;

public class DataFolder extends File {

	private final File pictureFolder;

	public DataFolder() {

		super(System.getenv("APPDATA") + "\\CustomShortcuts");
		if(!this.exists()) {
			if (!this.mkdir()) {
				stopApplication();
			}
		}

		this.pictureFolder = new File(this.getPath() + "\\Pictures");
		if (!this.pictureFolder.exists()) {
			if (!this.pictureFolder.mkdir()) {
				stopApplication();
			}
		}
	}

	public TemporaryPicture createTemporaryPicture(String picturePath, String uniqueName) throws DataFolderException {
		File originalFile = new File(picturePath);
		if (!originalFile.exists()) {
			throw new DataFolderException("No picture with the given file path exists.");
		}
		String originalName = originalFile.getName();
		String extension = originalName.substring(originalName.lastIndexOf('.'));
		TemporaryPicture copiedFile =
				new TemporaryPicture(this.pictureFolder.getPath() + "\\" + uniqueName + ".tmp", extension);
		boolean potentialFutureFileExists =
				Files.exists(Path.of(this.pictureFolder.getPath() + "\\" + uniqueName + extension));
		if ((copiedFile.exists() && !copiedFile.delete()) || potentialFutureFileExists) {
			throw new DataFolderException("A picture with that name is already saved.");
		}
		try {
			Files.copy(originalFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new DataFolderException("The provided picture couldn't be added to the data folder.");
		}
		return copiedFile;
	}

	public void deletePicture(String name) throws DataFolderException {
		File picture = new File(this.pictureFolder + "\\" + name);
		if (!picture.exists()) {
			return;
		}
		if (!picture.delete()) {
			throw new DataFolderException("The picture couldn't be deleted from the data folder.");
		}
	}

	public String changePicture(String sourcePath, String name) throws DataFolderException {
		String extension = sourcePath.substring(sourcePath.lastIndexOf('.'));
		String nameWithoutExtension = name.substring(0, name.lastIndexOf('.'));
		String newName = nameWithoutExtension + extension;
		try {
			Files.copy(Path.of(sourcePath), Path.of(this.pictureFolder + "\\" + newName),
					StandardCopyOption.REPLACE_EXISTING);
			if (!name.equals(newName)) {
				deletePicture(name);
			}
			return newName;
		} catch (IOException e) {
			throw new DataFolderException("The picture couldn't be changed.");
		}
	}

	public Path getPathToPicture(String name) {
		Path path = Path.of(this.pictureFolder + "\\" + name);
		if (Files.exists(path)) {
			return path;
		} else {
			return null;
		}
	}

	private void stopApplication() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setHeaderText("Application cannot be opened.");
		errorAlert.setContentText("Failed to create a data folder at" + this.getPath() + ".");
		Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
		stage2.getIcons().add(getIcon());
		errorAlert.showAndWait();
		Platform.exit();
	}
}
