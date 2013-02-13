package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CircleController {

	@FXML
	private Circle circle;
	
	private int showTime = 1000;
	
//	public void show(int delay, final String color, final int radius, final BoardController bc) {
//		new Timer().schedule(new TimerTask() {
//			
//            @Override
//            public void run() {
//            	circle.setRadius(radius);
//            	circle.setStyle("-fx-fill: " + color);
//            	circle.setVisible(true);
//            	new Timer().schedule(new TimerTask() {
//        			
//                    @Override
//                    public void run() {
//                    	bc.removeNode(circle);
//                    }
//                }, showTime);
//            }
//            	
//        }, delay);
//	}
	
	public void show(int delay, final String color, final int radius, final BoardController bc) {
		new Timer().schedule(new TimerTask() {
			
            @Override
            public void run() {
            	circle.setRadius(radius);
            	circle.setStyle("-fx-fill: " + color);
            	FadeTransition ft = new FadeTransition(Duration.millis(showTime/2), circle);
            	ft.setFromValue(0.0);
            	ft.setToValue(0.4);
            	ft.setAutoReverse(true);
            	ft.setCycleCount(2);
            	ft.play();
            	new Timer().schedule(new TimerTask() {
        			
                    @Override
                    public void run() {
                    	bc.removeNode(circle);
                    }
                }, showTime);
            }
            	
        }, delay);
	}

}
