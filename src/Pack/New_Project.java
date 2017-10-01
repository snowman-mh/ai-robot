package Pack;
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
	static long t0; //óßÇøè„Ç™ÇÈäJénéûä‘Çï€ë∂
	static long standTime = 0; //óßÇ¡ÇƒÇÈéûä‘Çï€ë∂
	static boolean isMoving = false;



	public static void main(String[] args) {
	

		gyroSensor.reset();
		int i = 0;
		while(i<60){
			LCD.drawString("standTime : " + standTime , 1, 0);
			LCD.refresh();
			float[] angleRate = getAngleRate();
			if(Math.abs(angleRate[0]) > 30 || Math.abs(angleRate[0]) < 10){
				stopMove();
			}
			else{
				Move(-100);
			}
			Delay.msDelay(500);
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
		public static void stopMove(){
			if(isMoving == true){
			  standTime = System.currentTimeMillis() - t0;
			  isMoving = false;
			}
			leftMotor.stop();
			rightMotor.stop();
		}
}



