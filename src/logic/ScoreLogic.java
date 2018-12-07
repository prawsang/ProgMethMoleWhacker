package logic;

public class ScoreLogic {
	private static int score;
	private static int scoreMultiplier = 1;
	
	public static int addScore(int added) {
		score += added * scoreMultiplier;
		return score;
	}
	public static int getScore() {
		return score;
	}
	public static int getScoreMultiplier() {
		return scoreMultiplier;
	}
	public static void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
	}
	public static int resetScore() {
		score = 0;
		return score;
	}
}
