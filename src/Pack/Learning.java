package Pack;

import lejos.hardware.lcd.LCD;
import java.util.Arrays;

public class Learning {

	static float alpha = 0.2f;
	static float gamma = 0.99f;
	static float[][] Q;
	static int previousState = 2;
	static int previousAction = 2;

	public int getAction(int state, int previousReward) {
		int maxQAction = 0;
		float maxQ = Q[state][0];
		for (int i = 0; i < Q[state].length; i++) {
			LCD.drawString("action " + i + ": " + Q[state][i], 1, i + 5);
			LCD.refresh();
			if (Q[state][i] > maxQ) {
				maxQ = Q[state][i];
				maxQAction = i;
			}
		}
		float previousQ = Q[previousState][previousAction];
		Q[previousState][previousAction] = (1 - alpha) * previousQ + alpha * (previousReward + gamma * maxQ);
		previousState = state;
		previousAction = maxQAction;
		return maxQAction;
	}

	public void initQ(int numOfStates, int numOfActions){
		Q = new float[numOfStates][numOfActions];
		for (int i = 0; i < numOfStates; i++) {
			Arrays.fill(Q[i], 1.0f);
		}
	}

}
