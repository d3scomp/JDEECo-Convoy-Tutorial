package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class BoardController implements Initializable {

	private int cellSize = 50;
	private int boardSize = 15;
	private int objectSize = 40;
	private int delay = 200;

	@FXML
	private GridPane board;
	@FXML
	private AnchorPane pane;

	public BoardController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Board board = Board.getInstance();
		board.setBoardController(this);
		updateBoard();
	}

	public void dispose() {
		Board.getInstance().stop();
	}

	public void setCellSize(int size) {
		cellSize = size;
		updateBoard();
	}

	public void setBoardSize(int size) {
		boardSize = size;
		updateBoard();
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getCellSize() {
		return cellSize;
	}
	
	public void removeNode(final Node node) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pane.getChildren().remove(node);
			}
		});
	}

	public ObjectController addObject(BoardObject bo) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"Object.fxml"));
		final Button vObject = (Button) fxmlLoader.load();
		ObjectController oc = (ObjectController) fxmlLoader.getController();
		oc.setBoardController(this);
		oc.setLabel(bo.label);
		oc.setColor(bo.color);
		oc.setPositions(bo.positions, false);
		updateObjectProperties(vObject);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pane.getChildren().add(vObject);
			}
		});
		return oc;
	}
	
	public void showPositions(List<Position> positions, String color) {
		if (positions != null) {
			for (int i = 0; i < positions.size(); i++) {
				showPostion(positions.get(i), color, i*delay);
			}
		}
	}
	
	private void showPostion(Position position, String color, int delay) {
		try {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"Circle.fxml"));
		final Circle vObject = (Circle) fxmlLoader.load();
		CircleController oc = (CircleController) fxmlLoader.getController();
		oc.show(delay, color, objectSize / 2, this);
		vObject.setLayoutX(position.x);
		vObject.setLayoutY(position.y);
		setNodeOffset(vObject, cellSize / 2);		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pane.getChildren().add(0, vObject);
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateObjectProperties(Button vObject) {
		vObject.setMinHeight(objectSize);
		vObject.setMinWidth(objectSize);
		vObject.setMaxHeight(objectSize);
		vObject.setMaxWidth(objectSize);
		setNodeOffset(vObject, cellSize / 2 - objectSize / 2);
	}
	
	private void setNodeOffset(Node vObject, double offset) {
		vObject.setTranslateX(offset);
		vObject.setTranslateY(offset);
	}

	private void updateBoard() {
		ObservableList<RowConstraints> rc = board.getRowConstraints();
		ObservableList<ColumnConstraints> cc = board.getColumnConstraints();
		rc.clear();
		cc.clear();
		for (int i = 0; i < boardSize; i++) {
			rc.add(new RowConstraints(cellSize));
			cc.add(new ColumnConstraints(cellSize));
		}
	}
}
