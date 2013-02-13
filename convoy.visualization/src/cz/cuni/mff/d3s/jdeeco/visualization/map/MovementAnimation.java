package cz.cuni.mff.d3s.jdeeco.visualization.map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation {
	public static void play(final double duration, final Position e, final Node n) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Timeline tl = new Timeline();
				KeyValue xkv = new KeyValue(n.layoutXProperty(), e.x);
				KeyValue ykv = new KeyValue(n.layoutYProperty(), e.y);
				KeyFrame kf = new KeyFrame(Duration.millis(duration), xkv, ykv);
				tl.getKeyFrames().add(kf);
				tl.play();
			}
		});

	}
}
