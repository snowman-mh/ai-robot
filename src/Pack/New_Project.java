package Pack;

import Pack.Learning;
import lejos.hardware.lcd.LCD;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class New_Project {

	static RegulatedMotor motor = Motor.D;
	static EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S4);
	static long t0;
	static long standTime = 0;
	static boolean isMoving = false;
	static int rateSectionSpace = 5;
	static int angleStateSpace = 5;
	static Learning learning = new Learning();
	static int numOfAction = 3;
	static int numOfState = 200;
	static int middleAngle = 90;
	static int moveAngle = 40;
	static int delayMs = 10;
	static int preangle = 90;

	public static void main(String[] args) {
		learning.initQ(numOfState, numOfAction);
		gyroSensor.reset();
		int i = 0;
		while (Button.ESCAPE.isUp()) {
			LCD.drawString("standTime : " + standTime, 1, 0);
			LCD.refresh();
			float[] angleRate = getAngleRate();

			if (Math.abs(angleRate[0]) > middleAngle + moveAngle/2 || Math.abs(angleRate[0]) < middleAngle - moveAngle/2) {
				stopMove();
				finishMove();
			} else {
				if (isMoving = false) {
					Delay.msDelay(500);
				}
				int currentState = getEnvironment(angleRate[0], angleRate[1]);
				int previousReward = reward((int)Math.abs(angleRate[0]));
				int action = learning.getAction(currentState, previousReward);
				LCD.drawString("action : " + i, 1, 4);
				LCD.refresh();
				motorControl(action);
			}
			i++;
		}
		System.exit(0);
	}

	public static float[] getAngleRate() {
		SensorMode gyro = gyroSensor.getMode(2);
		float value[] = new float[gyro.sampleSize()];
		gyro.fetchSample(value, 0);
		LCD.drawString("Angle : " + value[0], 1, 1);
		LCD.drawString("Rate : " + value[1], 1, 2);
		LCD.refresh();
		return value;
	}

	public static void Move(int speed) {
		if (isMoving == false) {
			t0 = System.currentTimeMillis();
			isMoving = true;
		}
		if (speed > 0) {
			motor.setSpeed(speed);
			motor.forward();
		} else {
			motor.setSpeed(-speed);
			motor.backward();
		}
	}

	public static void finishMove() {
		if (isMoving == true) {
			standTime = System.currentTimeMillis() - t0;
			isMoving = false;
		}
	}

	public static void stopMove() {
		motor.stop();
	}

	public static int getEnvironment(float angle, float rate) {
		int rateSection = (int)((rate + 50) / rateSectionSpace);
		if (rateSection >= 100 / rateSectionSpace) {
			rateSection = 100 / rateSectionSpace - 1;
		} else if (rateSection < 0) {
			rateSection = 0;
		}
		int angleSection = (int)((angle - (middleAngle - moveAngle / 2)) / angleStateSpace);
		if (angleSection >= moveAngle / angleStateSpace) {
			angleSection = moveAngle / angleStateSpace - 1;
		} else if (angleSection < 0) {
			angleSection = 0;
		}
		return rateSection * 5 + angleSection;
	}

	public static void motorControl(int action) {
		switch (action) {
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

	public static int reward(int angle) {
		int reward = - (Math.abs(preangle - middleAngle)) - (Math.abs(Math.abs(angle) - middleAngle));
		preangle = Math.abs(angle);
		LCD.drawString("reward : " + reward, 1, 3);
		LCD.refresh();
		return reward;
	}

}
