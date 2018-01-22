package group10;
import robocode.*;

public class MovingRobot extends AntiGrav{
	private double velocityX;
	private double velocityY;
	private double accelerationX=0;
	private double accelerationY=0;
	private double energy;
	private String name;
	private boolean isTarget = false;
	private boolean isEnemy = false;

	private long timeStamp;

	public MovingRobot(ScannedRobotEvent e, AdvancedRobot informationProvider){
		double scannedRobotRadians = informationProvider.getHeadingRadians() + e.getBearingRadians();

		x = informationProvider.getX() + e.getDistance() * Math.sin(scannedRobotRadians);
		y = informationProvider.getY() + e.getDistance() * Math.cos(scannedRobotRadians);
		velocityX = e.getVelocity() * Math.sin(e.getHeadingRadians());
		velocityY = e.getVelocity() * Math.cos(e.getHeadingRadians());

		energy = e.getEnergy();
		name = e.getName();

		timeStamp = e.getTime();
	}

	public MovingRobot(String theMessage){
		String[] theSp = theMessage.split(",", 0);
		//0.名前、1.時間、2.エネルギー、3.向き、4.距離、5.速度、6.(自分の)傾き、7.X座標、8.Y座標
		int[] theSplit = new int[9];
		theSplit[1] = Integer.parseInt(theSp[1]);
		theSplit[2] = Integer.parseInt(theSp[2]);
		theSplit[3] = Integer.parseInt(theSp[3]);
		theSplit[4] = Integer.parseInt(theSp[4]);
		theSplit[5] = Integer.parseInt(theSp[5]);
		theSplit[6] = Integer.parseInt(theSp[6]);
		theSplit[7] = Integer.parseInt(theSp[7]);
		theSplit[8] = Integer.parseInt(theSp[8]);
		double scannedRobotRadians = theSplit[6] + theSplit[3];

		x = theSplit[7] + theSplit[4] * Math.sin(scannedRobotRadians);
		y = theSplit[8] + theSplit[4] * Math.cos(scannedRobotRadians);
		velocityX = theSplit[5] * Math.sin(theSplit[3]);
		velocityY = theSplit[5] * Math.cos(theSplit[3]);

		energy = theSplit[2];
		name = theSp[0];
		timeStamp = theSplit[1];
		System.out.println("String: y is" + this.gety());
		System.out.println("String: Name is" + this.getName());
	}
	
	public MovingRobot(){		// ダミーロボットとして利用
		name = null;
		energy = 500;
	}


	public void setWeight(double theWeight){
		weight = theWeight;
	}

	public void setAcceleration(double inAccelerationX, double inAccelerationY){
		accelerationX = inAccelerationX;
		accelerationY = inAccelerationY;
	}

	public void setTarget(boolean t){
		isTarget = t;
	}
	
	public void setEnemy(boolean t){
		isEnemy = t;
	}

	public String getName(){
		return name;
	}

	public double getWillX(long theTime){
		long interval = theTime - timeStamp;

		return x + velocityX * interval + accelerationX * Math.pow(interval, 2) / 2;
	}

	public double getWillY(long theTime){
		long interval = theTime - timeStamp;

		return y + velocityY * interval + accelerationY * Math.pow(interval, 2) / 2;
	}

	public double getVelocityX(){
		return velocityX;
	}

	public double getVelocityY(){
		return velocityY;
	}

	public long getTimeStamp(){
		return timeStamp;
	}

	public double getEnergy(){
		return energy;
	}
	
	public boolean getTarget(){
		return isTarget;
	}

	public double getAccelerationX(){
		return accelerationX;
	}

	public double getAccelerationY(){
		return accelerationY;
	}

}
