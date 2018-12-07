package logic;

import java.util.ArrayList;
import java.util.Random;
import application.Constants;
import item.*;

public class RandomLogic {
	
	public static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	
	public int randomPosition(ArrayList<Integer> available) {
		return new Random().nextInt(available.size());
	}
	
	public int randomItemsAtOnce(int speed) {
		int itemsAtOnce = 1;
		int random = randomProb[new Random().nextInt(randomProb.length)];
		
		if (speed < Constants.MAXINTERVAL) {
			if (random <= 5) {
				itemsAtOnce = 1;
			} else {
				itemsAtOnce = 2;
			}
		} else if (speed < Constants.MAXINTERVAL - Constants.INTERVALSTEP*4) {
			if (random <= 4) {
				itemsAtOnce = 1;
			} else if (random >= 7) {
				itemsAtOnce = 3;
			} else {
				itemsAtOnce = 2;
			}
		}
		return itemsAtOnce;
	}
	
	public Item randomItem(int speed, int position, boolean fever) {
		int random = randomProb[new Random().nextInt(randomProb.length)];
		if (speed >= Constants.MAXINTERVAL - Constants.INTERVALSTEP*2) {
			return new NormalEnemy();
		} else {		
			if (random <= 6) {
				return new NormalEnemy();
			} else {
				int item = randomProb[new Random().nextInt(randomProb.length)];
				if (item <= 5) {
					return new StrongEnemy();
				}
				else if (item == 6) {
					return new Bomb();
				}
				else if (item == 7 && !fever) {
					return new FeverStar();
				}
				else {
					return new Dynamite(position);
				}
			}
		}
	}
}
