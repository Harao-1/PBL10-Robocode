package group10;
import robocode.*;
import java.io.*;

public class MessagePack implements Serializable{

	private ScannedRobotEvent theEvent;
	private double providerX;
	private double providerY;
	private double providerHeading;


	public MessagePack(ScannedRobotEvent e, AdvancedRobot theProvider){
		theEvent = e;
		providerX = theProvider.getX();
		providerY = theProvider.getY();
		providerHeading = theProvider.getHeadingRadians();

	}

	public ScannedRobotEvent getEvent(){
		return theEvent;
	}

	public double getX(){
		return providerX;
	}

	public double getY(){
		return providerY;
	}

	public double getHeading(){
		return providerHeading;
	}
}
