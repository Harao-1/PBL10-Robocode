package group10;
import robocode.*;
import java.util.*;

public class MovingRobotMap extends HashSet<MovingRobot>{

	public void updateTheData(MovingRobot newData){
		for(MovingRobot element: this){
			if(element.getName().equals(newData.getName())==true){
				double theAccelerationX, theAccelerationY;
				long timeInterval = newData.getTimeStamp() - element.getTimeStamp();
				theAccelerationX = (newData.getVelocityX() - element.getVelocityX()) / timeInterval;
				theAccelerationY = (newData.getVelocityY() - element.getVelocityY()) / timeInterval;

				newData.setAcceleration(theAccelerationX, theAccelerationY);
				this.add(newData);
				return;
			}
		}

		this.add(newData);
	}

	public MovingRobot getInfo(String theName){
		for(MovingRobot element: this){
			if(element.getName().equals(theName)==true){
				return element;
			}
		}

		MovingRobot nullRobot = new MovingRobot();
		return nullRobot;
	}
}
