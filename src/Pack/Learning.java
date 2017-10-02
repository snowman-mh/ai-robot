package Pack;

//import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
//import lejos.utility.Delay;

public class Learning {
	
	public static float LEARN_RATE = 0.32f;
	public static float EXPLORE_RATE = 0.15f;
	static int ct = 0;
	
	int ls = -1; // last state
	int la = -1; // last action
	int MaxA = 0; // action with highest q=value
	
	float[][] Q; // table of Q values
	
	float r = 0; // last reward
	
	QTools qT;
	
	// Creates the Q-values table
	public Learning(int actions, int states, QTools qT){
		Q = new float[actions][states];  //Table of Q-values for action-states
		this.qT = qT;
		qT.setRandomVal(Q);		
	}
	
	public int getAction(int[] states){		
		int st = qT.getState(states);  //Environment -> ID state (New state)		
		if(ls >= 0){ 
			++ct;
			r = qT.reward(ls, la);
					
			if(r == 1){  //In case that the reward is greater that zero
				if(LEARN_RATE > 0 && ct > 30){
					LEARN_RATE = LEARN_RATE - 0.001f; 
				}
				if(ct <= 30) {
					LEARN_RATE = LEARN_RATE + 0.001f;
				}
				if(EXPLORE_RATE > 0){
					EXPLORE_RATE = EXPLORE_RATE -0.001f;
				}
				
			}	
			
			MaxA = getMaxAction(st);
			Q[la][ls] = Q[la][ls] + LEARN_RATE*(r + Q[MaxA][st] - Q[la][ls]);
			
		}
		
		ls = st;
		
		float rand = (float) Math.random();
		if(rand > EXPLORE_RATE){
			la = MaxA;
		}else{
			la = (byte)(Math.random()*11); // 6 is the number of actions
		}
		
		writeMess("State & Action", "Reward = ", ls, la, r);
		
		return la;
				
	}
	
	// Find the action with the largest Q-Value
	public int getMaxAction(int st){
		float max = -1000;
		int action = 0;
		
		for(byte i=0; i<Q.length; ++i){
			
			if(Q[i][st] > max){
				max = Q[i][st];
				action = i;
			}
		}
		return action;
	}
	
	public void writeMess(String mess1, String mess2, int val1, int val2, float val3){

		LCD.clear();
		LCD.drawString(mess1, 2, 1); LCD.drawInt(val1, 4, 2); LCD.drawInt(val2, 12, 2);
		
		LCD.drawString(mess2, 2, 4); LCD.drawInt((int)(val3*100), 11, 4);
		
		LCD.drawString("L.R = ", 1, 6); LCD.drawInt((int)(LEARN_RATE*100), 6, 6);
		LCD.drawString("E.R = ", 10, 6); LCD.drawInt((int)(EXPLORE_RATE*100), 15, 6);
		
	}	
}
