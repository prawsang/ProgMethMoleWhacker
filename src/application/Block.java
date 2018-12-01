package application;
import item.Item;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class Block extends StackPane {
	
	private Item currentItem;
	private final int index;
	private Thread thread;
	
	public Block(int index) {
		this.index = index;
		
		setPrefWidth(Main.BLOCKSIZE);
		setPrefHeight(Main.BLOCKSIZE);
//		setStyle("-fx-background-color: #ddd; -fx-opacity: 0.5;");
	}
	
	public void setCurrentItem(Item i) {
		this.currentItem = i;
		this.clearNode();
		this.getChildren().add(i);
	}
	public void setCurrentItemWithTimer(Item i, int duration) {
		setCurrentItem(i);
		this.thread = new Thread(() -> {
			try {
				Thread.sleep(duration);
				Platform.runLater(()->{
					this.clearNode();
					GameController.available.add(this.index);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		this.thread.start();
	}
	public boolean hasRunningTimer() {
		return this.thread != null;
	}
	public void stopTimer() {
		if (this.thread != null) this.thread.interrupt();
	}
	
	public Item getCurrentItem() {
		return this.currentItem;
	}
	public boolean isEmpty() {
		return this.getChildren().size() == 0;
	}
	
	public void clearNode() {
		this.getChildren().clear();
	}
	public int getIndex() {
		return index;
	}
}
