package flowchart;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/** Sätter en pulserande skugga på valt objekt
 * 
 * @author Mattias Larsson
 *
 */
public class PulsatingShadow extends DropShadow {
	public PulsatingShadow() {
		this.setRadius(5);
		this.setColor(Color.RED);
		Timeline tL = new Timeline();
		tL.setCycleCount(Timeline.INDEFINITE);
		tL.setAutoReverse(true);
		tL.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), 
						new KeyValue(this.radiusProperty(), 10, Interpolator.EASE_BOTH)));
		tL.play();
	}
}
