package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class Container implements Initializable {
	
	@FXML
	protected GridPane container;
	
	@FXML
	protected Label lblId;
	
	protected String id;
	
	public DoubleProperty getLayoutXProperty() {
		return container.layoutXProperty();
	}
	
	public DoubleProperty getLayoutYProperty() {
		return container.layoutYProperty();
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateLabelId();
		container.setTranslateX(-30.0);
		container.setTranslateY(-15.0);
	}
	
	public Double getXPosition() {
		return container.layoutXProperty().get();
	}

	public Double getYPosition() {
		return container.layoutYProperty().get();
	}
	
	protected abstract void updateLabelId();
}
