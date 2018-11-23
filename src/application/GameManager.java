package application;

import java.util.ArrayList;
import java.util.Random;

import application.*;
import item.*;
import javafx.application.Platform;
import javafx.scene.Node;

public class GameManager {

	private static ArrayList<Integer> available;
	private int speed;
	private static int scoreMultiplier;
	
	private static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	
	public GameManager() {
		available = new ArrayList<Integer>();
		this.speed = 1000;
		scoreMultiplier = 1;
		
		for (int i = 0; i < 16; i++) {
			available.add(i);
		}
	}
	
	public void startGameLoop() {
		
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(15000);
					if (this.speed > 700) {
						this.speed -= 50;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		speedThread.start();
		
		Thread t = new Thread(() ->  {
			while(true) {
				try {
					Thread.sleep(this.speed);
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
			int itemsAtOnce = 1;
			int random = randomProb[new Random().nextInt(randomProb.length)];
			
			if (this.speed < 1000) {
				if (random <= 5) {
					itemsAtOnce = 1;
				} else {
					itemsAtOnce = 2;
				}
			} else if (this.speed < 800) {
				if (random <= 4) {
					itemsAtOnce = 1;
				} else if (random >= 7) {
					itemsAtOnce = 3;
				} else {
					itemsAtOnce = 2;
				}
			}
			
			random = randomProb[new Random().nextInt(randomProb.length)];
			for (int i = 0; i < itemsAtOnce; i++) {
				int randomPosIndex = new Random().nextInt(available.size());
				Block block = (Block) Main.blockPane.getChildren().get(available.get(randomPosIndex));
				
				if (speed >= 900) {
					block.setCurrentNode(new NormalEnemy());
				} else {	
					if (random <= 6) {
						block.setCurrentNode(new NormalEnemy());
					} else {
						block.setCurrentNode(new StrongEnemy());
					}
				}
				
				
				available.remove(randomPosIndex);
			}
		}
	}
	
	public static void addAvailable(int i) {
		available.add(i);
	}
	
	// Power Ups 
	public static int getScoreMultiplier() {
		return scoreMultiplier;
	}
	public static void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
	}
	
	public void clearBoard() {
		for (Node node : Main.blockPane.getChildren()) {
			Block block = (Block) node;
			block.clearNode();
		}
		for (int i = 0; i < 16; i++) {
			available.add(i);
		}
	}
	public void beginScoreMultiplier(int multiplier) {
		Thread t = new Thread(() -> {
			try {
				scoreMultiplier = multiplier;
				Thread.sleep(10000);
				scoreMultiplier = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
}
