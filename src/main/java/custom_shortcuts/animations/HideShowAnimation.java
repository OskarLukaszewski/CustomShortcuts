package custom_shortcuts.animations;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HideShowAnimation {

	private final Stage mainStage;
	private FontAwesomeIconView hideIcon;
	private boolean isPlaying, isHidden, fullyDraggable;

	public HideShowAnimation(Stage stage) {
		this.isPlaying = false;
		this.isHidden = false;
		this.fullyDraggable = false;
		this.mainStage = stage;
	}

	public void play() {
		if (this.isPlaying) {
			return;
		}
		if (isHidden) {
			playHideShowAnimation(new Double[] {230d, this.mainStage.getX() - 200});
		} else {
			playHideShowAnimation(new Double[] {30d, this.mainStage.getX() + 200});
		}
	}

	public void setFullyDraggable(boolean fullyDraggable) {
		this.fullyDraggable = fullyDraggable;
	}

	private void playHideShowAnimation(Double[] endValues) {
		this.isPlaying = true;
		Stage stage = this.mainStage;

		Timeline timelineWidth = new Timeline();
		WritableValue<Double> writableWidth = new WritableValue<>() {
			@Override
			public Double getValue() {
				return stage.getWidth();
			}

			@Override
			public void setValue(Double value) {
				stage.setWidth(value);
			}
		};
		KeyValue kvWidth = new KeyValue(writableWidth, endValues[0]);
		KeyFrame kfWidth = new KeyFrame(Duration.millis(1500), kvWidth);
		timelineWidth.getKeyFrames().addAll(kfWidth);
		timelineWidth.setOnFinished(actionEvent -> {
			this.isHidden = !this.isHidden;
			if (this.isHidden) {
				this.hideIcon.setIcon(FontAwesomeIcon.LONG_ARROW_LEFT);
			} else {
				this.hideIcon.setIcon(FontAwesomeIcon.LONG_ARROW_RIGHT);
			}
			this.isPlaying = false;
		});

		Timeline timeLineX = new Timeline();
		WritableValue<Double> writableX = new WritableValue<>() {
			@Override
			public Double getValue() {
				return stage.getX();
			}

			@Override
			public void setValue(Double value) {
				stage.setX(value);
			}
		};
		KeyValue kvX = new KeyValue(writableX, endValues[1]);
		KeyFrame kfX = new KeyFrame(Duration.millis(1500), kvX);
		timeLineX.getKeyFrames().addAll(kfX);

		timelineWidth.play();
		if (!this.fullyDraggable) {
			timeLineX.play();
		}
	}

	public void setHideIcon(FontAwesomeIconView hideIcon) {
		this.hideIcon = hideIcon;
	}
}
