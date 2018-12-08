package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StreakView extends VBox {
	
	private Label countLabel;
	private int count = 0;
	
	public StreakView() {
		setSpacing(15);
		setAlignment(Pos.CENTER);
		
		Label label = new Label("Press Spacebar!!!");
		countLabel = new Label("0");
		
		label.setStyle(style(15));
		label.setTextFill(Color.WHITE);
		countLabel.setStyle(style(40));
		countLabel.setTextFill(Color.WHITE);
		
		getChildren().addAll(label, countLabel);
	}
	
	public void reset() {
		count = 0;
		countLabel.setText("0");
	}
	
	public int getCount() {
		return count;
	}
	public void addCount() {
		count++;
		countLabel.setText(Integer.toString(count));
	}
	
	public String style(int fontSize) {
		return "-fx-font-family: 'Titan One', cursive; -fx-font-size: " + fontSize + "px; ";
	}
}
