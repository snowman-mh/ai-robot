package Pack;

import java.util.Arrays;

//import lejos.hardware.Sound;import lejos.hardware.lcd.LCD;
//import lejos.utility.Delay;

public class Learning {

	static float alpha = 0.2f;
	static float gamma = 0.99f;
	static float[][] Q;
	static int previousState = 2;
	static int previousAction = 2;

	public int getAction(int state, int previousReward){

		int maxQAction = 0;
		float maxQ = Q[state][0];
		for(int i = 1; i < Q[state].length; i++){
			if(Q[state][i] >= maxQ){
				maxQ = Q[state][i];
				maxQAction = i;
			}
		}
		int action = (int)(Math.random()*2);
		//QílÇÃçXêV
		float previousQ = Q[previousState][previousAction];
		Q[previousState][previousAction] = (1-alpha)*previousQ + alpha * (previousReward + gamma * previousQ);
		previousState = state;
		previousAction = action;
		return maxQAction;
	}

	public void initQ(int numOfStates,int numOfActions){
		Q = new float[numOfStates][numOfActions];
		for(int i = 0; i < numOfStates; i++){
			Arrays.fill(Q[i], 1.0f);
		}
	}
}
