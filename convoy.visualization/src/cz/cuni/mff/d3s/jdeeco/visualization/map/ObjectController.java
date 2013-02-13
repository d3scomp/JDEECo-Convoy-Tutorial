package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ObjectController {
	
	public static int animationSpeed = 300;
	
	@FXML
	private Button button;
	
	private BoardController bc;
	private List<Position> remainingPositions;
	private Position currentPosition;
	private String color;
	
	public void setBoardController(BoardController bc) {
		this.bc = bc;
	}
	
	public void setLabel(String label) {
		button.setText(label);
	}
	
	public void setColor(String color) {
		this.color = color;
		button.setStyle("-fx-base: "+color+"; -fx-font-weight: bold; -fx-font-size: 12px;");
	}
	
	public void setPositions(final List<Position> positions, final boolean animate) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				if (positions.size() >= 1) {
					Position first = positions.remove(0);
					if (!first.equals(currentPosition)) {
						currentPosition = first;
						Position translated = translate(currentPosition);
						if (animate) {
							MovementAnimation.play(animationSpeed, translated, button);
						} else {
							button.setLayoutX(translated.x);
							button.setLayoutY(translated.y);
						}
						remainingPositions = positions;
					}
				}	
			}
		});
	}
	
	private Position translate(Position position) {
		return new Position(position.x * bc.getCellSize(), position.y * bc.getCellSize());
	}
	
	@FXML
	private void onClick(MouseEvent me) {
		if (remainingPositions != null && remainingPositions.size() > 0) {
			List<Position> result = new LinkedList<Position>();
			for (Position position : remainingPositions)
				result.add(translate(position));
			bc.showPositions(result, color);
		}
	}

}
