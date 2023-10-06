package custom_shortcuts.gui.show_picture_window;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShowPictureController {

	@FXML
	private ImageView imageView;

	public void setPicture(Image picture) {
		this.imageView.setFitHeight(picture.getHeight());
		this.imageView.setFitWidth(picture.getWidth());
		this.imageView.setImage(picture);
	}
}
