package Pack;
import Pack.Learning;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class New_Project {

	static RegulatedMotor leftMotor = Motor.A; 
	static RegulatedMotor rightMotor = Motor.B;
	static EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
	static long t0; //—§‚¿ã‚ª‚éŠJnŠÔ‚ğ•Û‘¶
	static long standTime = 0; //—§‚Á‚Ä‚éŠÔ‚ğ•Û‘¶
	static boolean isMoving = false;
	static int rateSectionSpace = 5;
	static int angleStateSpace = 5;
	static Learning learning = new Learning();




	public static void main(String[] args) {

		learning.initQ(100,3);
		gyroSensor.reset();
		int i = 0;
		while(i<2000){
			LCD.drawString("standTime : " + standTime , 1, 0);
			LCD.refresh();
			float[] angleRate = getAngleRate();			

			if(Math.abs(angleRate[0]) > 30 || Math.abs(angleRate[0]) < 10){
				stopMove();
				finishMove();
			}
			else{
				int currentState = getEnvironment(angleRate[0],angleRate[1]);
				int previousReward = reward((int)Math.abs(angleRate[0]));
				int action = learning.getAction(currentState,previousReward);
				motorControl(action);
			}
			Delay.msDelay(10);
			i++;
		}
		System.exit(0);

	}

	public static float[] getAngleRate(){
		SensorMode gyro = gyroSensor.getMode(2);
		float value[] = new float[gyro.sampleSize()];
		gyro.fetchSample(value, 0);
		LCD.drawString("Angle : " + value[0] , 1, 1);
		LCD.drawString("Rate : " + value[1] , 1, 2);
		LCD.refresh();
		return value;
	}

	public static void Move(int speed){
		if(isMoving == false){
			t0 = System.currentTimeMillis();
			isMoving = true;
		}
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		if(speed > 0){
			leftMotor.forward();
			rightMotor.forward();
		}
		else{
			leftMotor.backward();
			rightMotor.backward();
		}
	}

	public static void finishMove(){
		if(isMoving == true){
			standTime = System.currentTimeMillis() - t0;
			isMoving = false;
		}
	}

	public static void stopMove(){
		leftMotor.stop();
		rightMotor.stop();
	}

	public static int getEnvironment(float angle, float rate){
		int rateSection = (int)((rate + 50) / rateSectionSpace);
		if(rateSection >= 100/rateSectionSpace){
			rateSection = 100/rateSectionSpace - 1;
		}
		else if(rateSection < 0){
			rateSection = 0;
		}
		int angleSection = (int)((angle - 10) / angleStateSpace);
		if(angleSection >= 20/angleStateSpace){
			angleSection = 20/angleStateSpace - 1;
		}
		else if(angleSection < 0){
			angleSection = 0;
		}
		return rateSection * 5 + angleSection;
	}

	public static void motorControl(int action){
		switch(action){
		case 0:
			Move(300);
			break;
		case 1:
			Move(-300);
			break;
		case 2:
			stopMove();
			break;
		}
	}
	public static int reward(int angle){
		int reward = 5 - Math.abs(angle - 20);
		return reward;
	}


}



