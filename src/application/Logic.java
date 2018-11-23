package application;

public class Logic {
	private int score;
	private int scoreMultiplier;
	
	public Logic() {
		scoreMultiplier = 1;
	}
	
	public int addScore(int score) {
		this.score += score * scoreMultiplier;
		return this.score;
	}
	 
	public int getScoreMultiplier() {
		return scoreMultiplier;
	}
	public void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
	}
	
	public void beginScoreMultiplier(int multiplier) {
		Thread t = new Thread(() -> {
			try {
				this.setScoreMultiplier(multiplier);
				Thread.sleep(10000);
				this.setScoreMultiplier(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
}
