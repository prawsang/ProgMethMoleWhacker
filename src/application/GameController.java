package application;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import logic.*;

public class GameController {
	
	// Logic
	private boolean running = false;
	private int speed;
	private ArrayList<Integer> available;
	
	// Effects
	private GraphicsContext bombEffects;
	private GraphicsContext feverEffects;
	private GraphicsContext hitEffects;
	
	//PowerUps
	private int bombs;
	private static Bomb bomb;
	private boolean fever;
	private boolean streak;
	
	// Sounds
	private MediaPlayer bgm = new MediaPlayer(new Media(Resources.SONG));
	private AudioClip useBombSound = new AudioClip(Resources.BOMB_SOUND);
	private AudioClip dieSound = new AudioClip(Resources.DIE);
	private AudioClip hitSound = new AudioClip(Resources.HIT_SOUND);
	private AudioClip collectItem  = new AudioClip(Resources.COLLECT);
	
	public GameController() {
		this.bombEffects = Main.bombEffects.getGraphicsContext2D();
		this.feverEffects = Main.feverEffects.getGraphicsContext2D();
		this.hitEffects = Main.hitEffects.getGraphicsContext2D();
		
		bomb = new Bomb();
		this.bombs = 0;
		this.fever = false;
		
		this.speed = Constants.MAX_INTERVAL;
		available = new ArrayList<Integer>();
		
		// Add blocks to block pane
		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i);
			
			block.setOnMousePressed(new BlockEventHandler(block));
			block.setOnMouseReleased((e) -> {
				hitEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			});
			
			Main.blockPane.getTiles().getChildren().add(block);
		}
		
	}
	
	// Press enter to use bomb (if available)
	public void setUpEnterEventHandler(Scene scene) {
		scene.setOnKeyReleased(new KeyEventHandler());
	}
	
	private class KeyEventHandler implements EventHandler<KeyEvent>{
		public void handle(KeyEvent e) {
			// Bomb
			if (e.getCode().equals(KeyCode.ENTER)){
				if (bombs > 0) {
					bomb.usePowerUp();
					Main.bombPane.drawBombPane(getBombs());
					
					useBombSound.play();
					
					Image boom = new Image(Resources.BIG_BOOM);
					bombEffects.drawImage(
							boom, 
							(Constants.WIDTH - boom.getWidth()/2)/2, 
							(Constants.HEIGHT - boom.getHeight()/2)/2,
							boom.getWidth()/2,
							boom.getHeight()/2
						);
					startEffectsTimer();
				}
			}
			// Streak
			if (streak) {
				if (e.getCode().equals(KeyCode.SPACE)){
					Main.streakScreen.addCount();
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
			if (block.isEmpty() || !running) {
				return;
			}
			Item item = this.block.getCurrentItem();
			if (item instanceof Enemy) {
				// Take damage
				Enemy e = (Enemy) item;
				if (!e.takeDamage() || fever) {
					Main.scorePane.setScoreLabelText(ScoreLogic.addScore(100));
					block.clearBlock();
					available.add(block.getIndex());
					// Die sound
					dieSound.play();
				}
				// Hit sound
				hitSound.play();
				
				// Hit effects
				Image hit = new Image(Resources.HIT);
				hitEffects.drawImage(
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
					collectItem.play();
					if (item instanceof Bomb) {
						if (getBombs() < 3) {
							c.collect();
							Main.bombPane.drawBombPane(getBombs());
						} else {
							return;
						}
					}
				} else {
					PowerUp p = (PowerUp) item;
					if (item instanceof Streak) {
						if (streak) return;
					}
					if (item instanceof FeverStar) {
						if (fever) return;
					}
					p.usePowerUp();
				}
				if (block.hasRunningTimer()) {
					block.stopTimer();
				}
				block.clearBlock();
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
					if (this.speed > Constants.MIN_INTERVAL) {
						this.speed -= Constants.INTERVAL_STEP;
					}
					if (this.speed == Constants.MIN_INTERVAL) break;
					if (!this.running) break;
				} catch (Exception e) {
				}
			}
		});
		speedThread.start();
		
		// Random
		Thread randomThread = new Thread(() ->  {
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
		randomThread.start();
		
		// BGM
		bgm.setOnEndOfMedia(new Runnable() {
			public void run() {
				bgm.seek(Duration.ZERO);
		    }
		});
		bgm.play();
		
	}
	
	// Reset Values
	public void resetValues() {
		clearBoard();
		this.bombEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.feverEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.speed = Constants.MAX_INTERVAL;
		Main.scorePane.setScoreLabelText(ScoreLogic.resetScore());
		this.fever = false;
		this.streak = false;
		this.bombs = 0;
		Main.bombPane.drawBombPane(0);
	}
	
	// Game Over
	private void gameOver() {
		this.running = false;
		this.fever = false;
		this.streak = false;
		bgm.seek(Duration.ZERO);
		bgm.stop();
		Main.showGameOver(ScoreLogic.getScore());
		HighScoreLogic.writeHighScore(ScoreLogic.getScore());
		Main.highScoreLabel.setHighScoreText(HighScoreLogic.getHighScore());
	}
	
	// Random
	private void randomGame() {
		if (available.size() <= 0) {
			return;
		}
		
		int itemsAtOnce = RandomLogic.randomItemsAtOnce(this.speed);
		if (itemsAtOnce > available.size()) {
			itemsAtOnce = available.size();
		}
		
		for (int i = 0; i < itemsAtOnce; i++) {
			int position = RandomLogic.randomPosition(available);
			Block block = (Block) Main.blockPane.getTiles().getChildren().get(available.get(position));
			available.remove(position);
			
			Item item = RandomLogic.randomItem(this.speed, block.getIndex(), this.fever);
			try {
				if (!(item instanceof Dynamite) && item instanceof PowerUp) {
					block.setCurrentItemWithTimer(item, 3000);
				} else {
					block.setCurrentItem(item);
				}
			} catch (FillOccupiedBlockException e) {
			}
		}
	}
	// Add/Remove available
	public void addAvailable(int index) {
		if (available.size() < Constants.MAX_BLOCK) {
			available.add(index);
		}
	}
	public void removeAvailable(int index) {
		if (available.size() > index) {
			available.remove(index);
		}
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
					ScoreLogic.addScore(100);
				}
				block.clearBlock();
				available.add(block.getIndex());
			}
		}
		Main.scorePane.setScoreLabelText(ScoreLogic.getScore());
	}
	public void startFever() {
		ScoreLogic.setScoreMultiplier(3);
		this.fever = true;
		this.feverEffects.drawImage(new Image(Resources.FEVER), 0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		collectItem.play();
		
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(10000);
				ScoreLogic.setScoreMultiplier(1);
				this.fever = false;
				this.feverEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			} catch (Exception e) {
			}
		});
		t.start();
	}
	public void killAdjacentEnemies(int position) {
		int[] kill;
		switch (position%3) {
			case 0: 
				kill = new int[]{position-3, position+1, position+3}; 
				break;
			case 1: 
				kill = new int[]{position-3, position-1, position+1, position+3}; 
				break;
			case 2: 
				kill = new int[]{position-3, position-1, position+3}; 
				break;
			default: 
				kill = new int[] {};
		}
		for (int k : kill) {
			if (k >=0 && k<12) {
				Block block =  (Block) Main.blockPane.getTiles().getChildren().get(k);
				if (!block.isEmpty()) {
					block.clearBlock();
					if (block.getCurrentItem() instanceof Enemy) {
						ScoreLogic.addScore(100);
					} else {
						if (block.hasRunningTimer()) {
							block.stopTimer();
						}
					}
					available.add(k);
					bombEffects.drawImage(
							new Image(Resources.BOOM), 
							Main.blockPane.getX(k) + 53, 
							Main.blockPane.getY(k) + 110, 
							Constants.BLOCK_SIZE, 
							Constants.BLOCK_SIZE
						);
				}
			}
		}
		bombEffects.drawImage(
				new Image(Resources.BOOM), 
				Main.blockPane.getX(position) + 53, 
				Main.blockPane.getY(position) + 110, 
				Constants.BLOCK_SIZE, 
				Constants.BLOCK_SIZE
			);
		startEffectsTimer();
	}
	
	// Streak
	public void startStreak() {
		streak = true;
		Main.showStreakScreen();
		collectItem.play();
		
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(5 * 1000);
				Platform.runLater(() -> {	
					Main.scorePane.setScoreLabelText(ScoreLogic.addScore(Main.streakScreen.getCount() * 100));
					Main.hideStreakScreen();
					streak = false;
				});
			} catch (InterruptedException e) {
			}
		});
		t.start();
	}

	// Bomb Effects
	private void startEffectsTimer() {
		Thread t = new Thread(()->  {
			try {
				Thread.sleep(500);
				bombEffects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			} catch (InterruptedException e) {
			}
		});
		t.start();
	}
}

