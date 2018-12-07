package component;
import application.Main;
import application.Constants;
import item.Item;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import logic.FillOccupiedBlockException;

public class Block extends StackPane {
	
	private Item currentItem;
	private final int index;
	private Thread thread;
	
	public Block(int index) {
		this.index = index;
		
		setPrefWidth(Constants.BLOCKSIZE);
		setPrefHeight(Constants.BLOCKSIZE);
	}
	
	public void setCurrentItem(Item i) throws FillOccupiedBlockException {
		if (!this.isEmpty()) throw new FillOccupiedBlockException();
		this.currentItem = i;
		this.clearNode();
		Main.blockPane.drawGC(index, currentItem.getImage());
		this.getChildren().add(i);
	}
	public void setCurrentItemWithTimer(Item i, int duration) throws FillOccupiedBlockException {
		if (!this.isEmpty()) throw new FillOccupiedBlockException();
		setCurrentItem(i);
		this.thread = new Thread(() -> {
			try {
				Thread.sleep(duration);
				Platform.runLater(()->{
					this.clearNode();
					Main.gameController.addAvailable(this.index);
				});
			} catch (Exception e) {
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
		Main.blockPane.clearGC(index);
		if (!isEmpty()) {
			this.getChildren().remove(0);
		}
	}
	public int getIndex() {
		return index;
	}
}
