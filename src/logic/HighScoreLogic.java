package logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HighScoreLogic {
	
	private static Path path = Paths.get("highscore.txt");
	private static int highscore;
	
	public static void writeHighScore(int score) throws IOException {
		
		String fileContent = new String(); 
		if (score > getHighScore()) {
			fileContent = Integer.toString(score);
		} else return;
		
		Path path = Paths.get("highscore.txt");
		Files.write(path, fileContent.getBytes());
	}
	
	public static void resetHighScore()throws IOException {
		String fileContent = new String("0"); 
		
		Path path = Paths.get("highscore.txt");
		Files.write(path, fileContent.getBytes());
	}
	
	private static void readHighScore() throws IOException {
		List<String> lines = Files.readAllLines(path);
		try {
			highscore = Integer.parseInt(lines.get(0));
		} catch (NumberFormatException e) {
			highscore = 0;
		}
	}
	
	public static int getHighScore() {
		try {
			readHighScore();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return highscore;
	}
}
