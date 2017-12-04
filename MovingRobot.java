package group10;
import robocode.*;

public class MovingRobot extends AntiGrav{
	private double velocityX;
	private double velocityY;
	private double accelerationX;
	private double accelerationY;
	private double energy;
	private String name;
	private boolean isTarget = false;
	private boolean isEnemy = false;

	private long timeStamp;

	public MovingRobot(ScannedRobotEvent e, Robot informationProvider){
		double scannedRobotRadians = Math.toRadians(informationProvider.getRadarHeading()) + e.getBearingRadians();

		x = informationProvider.getX() + e.getDistance() * Math.sin(scannedRobotRadians);
		y = informationProvider.getY() + e.getDistance() * Math.cos(scannedRobotRadians);
		velocityX = e.getVelocity() * Math.sin(e.getHeadingRadians());
		velocityY = e.getVelocity() * Math.cos(e.getHeadingRadians());

		energy = e.getEnergy();
		name = e.getName();

		timeStamp = e.getTime();
	}
	
	public MovingRobot(){
		name = null;
	}


	public void setWeight(double theWeight){
		weight = theWeight;
	}

	public void setAcceleration(double accelerationX, double accelerationY){
		accelerationX = accelerationX;
		accelerationY = accelerationY;
	}

	public void setTarget(){
		isTarget = true;
	}
	
	public void setEnemy(){
		isEnemy = true;
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

}
