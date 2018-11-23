package application;

import java.util.ArrayList;
import java.util.Random;

import application.*;
import item.*;
import javafx.application.Platform;

public class GameManager {

	private static ArrayList<Integer> available;
	
	public GameManager() {
		available = new ArrayList<Integer>();
		
		for (int i = 0; i < 16; i++) {
			available.add(i);
		}
	}
	
	public void startGameLoop() {
		Thread t = new Thread(() ->  {
			while(true) {
				try {
					Thread.sleep(1000);
					Platform.runLater(() -> {
						randomItem();
					});
					if (available.size() == 0) {
						System.out.println("You Lose");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t.start();
	}
	
	public void randomItem() {
		if (!available.isEmpty()) {
			int randomIndex = new Random().nextInt(available.size());
			Block block = (Block) Main.blockPane.getChildren().get(available.get(randomIndex));
			block.setCurrentNode(new NormalEnemy());
			
			available.remove(randomIndex);
		}
	}
	
	public static void addAvailable(int i) {
		available.add(i);
	}
}
