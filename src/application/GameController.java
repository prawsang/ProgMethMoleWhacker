package application;

import java.io.IOException;
import java.util.ArrayList;

import component.Block;

import item.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import logic.*;

public class GameController {
	
	// Logic
	private ScoreLogic scoreLogic;
	private RandomLogic randomLogic;
	private boolean running = false;
	private int speed;
	private ArrayList<Integer> available;
	
	// Effects
	private GraphicsContext effects;
	private GraphicsContext feverEffects;
	
	//PowerUps
	private int bombs;
	private static Bomb bomb;
	private boolean fever;
	
	// BGM
	private Thread bgSoundThread;
	private AudioClip sound = new AudioClip(Resources.SONG);
	
	public GameController() {
		
		this.effects = Main.effects.getGraphicsContext2D();
		this.feverEffects = Main.feverEffects.getGraphicsContext2D();
		
		bomb = new Bomb();
		this.bombs = 0;
		this.fever = false;
		
		this.scoreLogic = new ScoreLogic();
		this.randomLogic = new RandomLogic();
		this.speed = Constants.MININTERVAL;
		available = new ArrayList<Integer>();

		// Add blocks to block pane
		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i);
			
			block.setOnMousePressed(new BlockEventHandler(block));
			block.setOnMouseReleased((e) -> {
				effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			});
			
			Main.blockPane.getTiles().getChildren().add(block);
		}
		
	}
	
	// Press enter to use bomb (if available)
	public void setUpEnterEventHandler(Scene scene) {
		scene.setOnKeyReleased(new EnterEventHandler());
	}
	
	private class EnterEventHandler implements EventHandler<KeyEvent>{
		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER)){
				if (bombs > 0) {
					bomb.usePowerUp();
					Main.bombPane.drawBombPane(getBombs());
					
					AudioClip useBombSound = new AudioClip(Resources.BOMBSOUND);
					useBombSound.play();
					
					Image boom = new Image(Resources.BIGBOOM);
					effects.drawImage(
							boom, 
							(Constants.WIDTH - boom.getWidth()/2)/2, 
							(Constants.HEIGHT - boom.getHeight()/2)/2,
							boom.getWidth()/2,
							boom.getHeight()/2
						);
					startEffectsTimer();
				}
			}
		}
	}
	
	// Handle block clicks
	private class BlockEventHandler implements EventHandler<MouseEvent>{
		private Block block;
		public BlockEventHandler(Block block) {
			this.block = block;
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			if (block.isEmpty() || !running) return;
			Item item = this.block.getCurrentItem();
			if (item instanceof Enemy) {
				// Take damage
				Enemy e = (Enemy) item;
				if (!e.takeDamage() || fever) {
					Main.scorePane.label.setText(Integer.toString(scoreLogic.addScore(100)));
					block.clearNode();
					available.add(block.getIndex());
					// Die sound
					AudioClip getHitSound = new AudioClip(Resources.GETHIT);
					getHitSound.play();
				}
				// Hit sound
				AudioClip hitSound = new AudioClip(Resources.HITSOUND);
				hitSound.play();
				
				// Hit effects
				Image hit = new Image(Resources.HIT);
				effects.drawImage(
						hit, 
						arg0.getSceneX()- hit.getWidth()/4, 
						arg0.getSceneY()-hit.getHeight()/4, 
						hit.getWidth()/2, 
						hit.getHeight()/2
					);
			}
			if (item instanceof PowerUp) {
				// Collect or use power up
				if (item instanceof Collectible) {
					Collectible c = (Collectible) item;
					AudioClip collectItem  = new AudioClip(Resources.COLLECTBOMB);
					collectItem.play();
					if (item instanceof Bomb) {
						if (getBombs() < 3) {
							c.collect();
							Main.bombPane.drawBombPane(getBombs());
						} else return;
					}
				} else {
					PowerUp p = (PowerUp) item;
					p.usePowerUp();
				}
				if (block.hasRunningTimer()) block.stopTimer();
				block.clearNode();
				available.add(block.getIndex());
			}
		}
	}
	
	// Main game loop
	public void startGameLoop() {
		this.running = true;
		// Increases Speed
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(10000);
					if (this.speed > Constants.MININTERVAL) {
						this.speed -= Constants.INTERVALSTEP;
					}
					if (this.speed == Constants.MININTERVAL) break;
					if (!this.running) break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		speedThread.start();
		
		// Random
		Thread t = new Thread(() ->  {
			while(true) {
				try {
					Thread.sleep(this.speed);
					Platform.runLater(() -> {
						this.randomGame();
					});
					if (!this.running) break;
					if (available.size() == 0) {
						Platform.runLater(() -> {
							gameOver();
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t.start();
		
		bgSoundThread = new Thread(() -> {
				try {
					while(true) {
						sound.play();
						Thread.sleep(480000);
					
					}
				} catch (InterruptedException e){
					sound.stop();
					e.printStackTrace();
				}
			
		} );
		bgSoundThread.start();
	}
	
	// Reset Values
	public void resetValues() {
		clearBoard();
		this.effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.feverEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.speed = Constants.MAXINTERVAL;
		Main.scorePane.label.setText(Integer.toString(this.scoreLogic.resetScore()));
		this.fever = false;
		this.bombs = 0;
		Main.bombPane.drawBombPane(0);
	}
	
	// Game Over
	private void gameOver() {
		this.running = false;
		this.fever = false;
		bgSoundThread.interrupt();
		Main.showGameOver(scoreLogic.getScore());
		try {
			HighScoreLogic.writeHighScore(scoreLogic.getScore());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.highScoreLabel.setHighScoreText(HighScoreLogic.getHighScore());
	}
	
	// Random
	private void randomGame() {
		if (available.size() <= 0) return;
		
		int itemsAtOnce = randomLogic.randomItemsAtOnce(this.speed);
		if (itemsAtOnce > available.size()) itemsAtOnce = available.size();
		
		for (int i = 0; i < itemsAtOnce; i++) {
			int position = randomLogic.randomPosition(available);
			Block block = (Block) Main.blockPane.getTiles().getChildren().get(available.get(position));
			available.remove(position);
			
			int randomItem = randomLogic.randomItem(available, this.speed, this.fever);
			Item item;
			switch (randomItem) {
				case 0: item = new NormalEnemy(); break;
				case 1: item = new StrongEnemy(); break;
				case 2: item = new Bomb(); break;
				case 3: item = new FeverStar(); break;
				case 4: item = new Dynamite(block.getIndex()); break;
				default: item = new NormalEnemy();
			}
			try {
				if (item instanceof Bomb || item instanceof FeverStar) {
					block.setCurrentItemWithTimer(item, 3000);
				} else {
					block.setCurrentItem(item);
				}
			} catch (FillOccupiedBlockException e) {
				e.printStackTrace();
			}
		}
	}
	// Add/Remove available
	public void addAvailable(int index) {
		if (available.size() < Constants.MAXBLOCK) available.add(index);
	}
	public void removeAvailable(int index) {
		if (available.size() > index) available.remove(index);
	}
	
	// Power Up Storage
	public void addBomb() {
		if (this.bombs < 3) {
			this.bombs++;
		}
	}
	public void deleteBomb() {
		if (this.bombs > 0) {
			this.bombs--;
		}
	}
	public int getBombs() {
		return this.bombs;
	}
	
	// Power Ups
	public void clearBoard() {
		for (Node node : Main.blockPane.getTiles().getChildren()) {
			Block block = (Block) node;
			if (!block.isEmpty()) {
				if (block.getCurrentItem() instanceof Enemy) {
					this.scoreLogic.addScore(100);
				}
				block.clearNode();
				available.add(block.getIndex());
			}
		}
		Main.scorePane.label.setText(Integer.toString(this.scoreLogic.getScore()));
	}
	public void startFever() {
		this.scoreLogic.setScoreMultiplier(3);
		this.fever = true;
		this.feverEffects.drawImage(new Image(Resources.FEVER), 0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		AudioClip collectItem  = new AudioClip(Resources.COLLECTBOMB);
		collectItem.play();
		
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(10000);
				this.scoreLogic.setScoreMultiplier(1);
				this.fever = false;
				this.feverEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	public void killAdjacentEnemies(int position) {
		int[] kill;
		switch (position%3) {
			case 0: kill = new int[]{position-3, position+1, position+3}; break;
			case 1: kill = new int[]{position-3, position-1, position+1, position+3}; break;
			case 2: kill = new int[]{position-3, position-1, position+3}; break;
			default: kill = new int[] {};
		}
		for (int k : kill) {
			if (k >=0 && k<12) {
				Block block =  (Block) Main.blockPane.getTiles().getChildren().get(k);
				if (!block.isEmpty()) {
					block.clearNode();
					if (block.getCurrentItem() instanceof Enemy) {
						this.scoreLogic.addScore(100);
					} else {
						if (block.hasRunningTimer()) block.stopTimer();
					}
					available.add(k);
					effects.drawImage(
							new Image(Resources.BOOM), 
							Main.blockPane.getX(k) + 53, 
							Main.blockPane.getY(k) + 110, 
							Constants.BLOCKSIZE, 
							Constants.BLOCKSIZE
						);
				}
			}
		}
		effects.drawImage(
				new Image(Resources.BOOM), 
				Main.blockPane.getX(position) + 53, 
				Main.blockPane.getY(position) + 110, 
				Constants.BLOCKSIZE, 
				Constants.BLOCKSIZE
			);
		startEffectsTimer();
	}
	
	// Bomb Effects
	private void startEffectsTimer() {
		Thread t = new Thread(()->  {
			try {
				Thread.sleep(500);
				effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
}

