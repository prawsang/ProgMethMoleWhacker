package application;

public class Resources {
	
	// IMAGES
	public static final String BACKGROUND = ClassLoader.getSystemResource("images/background.png").toString();
	public static final String FRAME = ClassLoader.getSystemResource("images/frame.png").toString();
	public static final String SCORE = ClassLoader.getSystemResource("images/score_background.png").toString();
	public static final String FEVER = ClassLoader.getSystemResource("images/fever.png").toString();
	
	public static final String NORMALENEMY = ClassLoader.getSystemResource("images/enemy.png").toString();
	public static final String STRONGENEMY = ClassLoader.getSystemResource("images/strong_enemy.png").toString();
	public static final String FEVERSTAR = ClassLoader.getSystemResource("images/fever_star.png").toString();
	public static final String STREAKSTAR = ClassLoader.getSystemResource("images/streak_star.png").toString();
	public static final String BOMB = ClassLoader.getSystemResource("images/bomb.png").toString();
	public static final String DYNAMITE = ClassLoader.getSystemResource("images/dynamite.png").toString();
	
	public static final String BOMBS0 = ClassLoader.getSystemResource("images/bombs_0.png").toString();
	public static final String BOMBS1 = ClassLoader.getSystemResource("images/bombs_1.png").toString();
	public static final String BOMBS2 = ClassLoader.getSystemResource("images/bombs_2.png").toString();
	public static final String BOMBS3 = ClassLoader.getSystemResource("images/bombs_3.png").toString();
	
	public static final String BIGBOOM = ClassLoader.getSystemResource("images/big_boom.png").toString();
	public static final String BOOM = ClassLoader.getSystemResource("images/boom.png").toString();
	public static final String HIT = ClassLoader.getSystemResource("images/hit.png").toString();
	
	// SOUNDS
	public static final String SONG = ClassLoader.getSystemResource("audio/song.m4a").toString();
	public static final String BOMBSOUND = ClassLoader.getSystemResource("audio/bombSound.m4a").toString();
	public static final String GETHIT = ClassLoader.getSystemResource("audio/getHit.m4a").toString();
	public static final String COLLECTBOMB = ClassLoader.getSystemResource("audio/collectBomb.m4a").toString();
	public static final String HITSOUND = ClassLoader.getSystemResource("audio/hit.m4a").toString();
}
