package cz.cuni.mff.d3s.jdeeco.visualization.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualizer extends Application {

	private BoardController bc;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Map");
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"Board.fxml"));
		Scene scene = (Scene) fxmlLoader.load();
		bc = (BoardController) fxmlLoader.getController();
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}

	@Override
	public void stop() {
		bc.dispose();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
