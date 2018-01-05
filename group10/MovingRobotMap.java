package group10;
import java.util.*;

public class MovingRobotMap extends HashSet<MovingRobot>{

	public void updateTheData(MovingRobot newData){

		String newName = newData.getName();

		if(!(newName.matches("group10.*"))){
			newData.setEnemy(true);
		}

		for(MovingRobot element: this){
			long timeInterval = newData.getTimeStamp() - element.getTimeStamp();
			if(element.getName().equals(newName)==true){
				if(timeInterval<6){
					double theAccelerationX, theAccelerationY;
					theAccelerationX = (newData.getVelocityX() - element.getVelocityX()) / timeInterval;
					theAccelerationY = (newData.getVelocityY() - element.getVelocityY()) / timeInterval;

					newData.setAcceleration(theAccelerationX, theAccelerationY);
				}
				this.add(newData);
				this.remove(element);
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

	public void printForDebug(){
		for(MovingRobot e: this){
			System.out.println("name:" +e.getName()+"x:"+e.getx()+"y:"+e.gety()+"vx:"+e.getVelocityX());
			System.out.println("accelX:"+e.getAccelerationX()+"accelY:"+e.getAccelerationY());
		}
	}

	public void setTarget(){
		for(MovingRobot e: this){	// Target情報を初期化
			e.setTarget(false);
		}

		MovingRobot lowerEnergyRobot = new MovingRobot();
		for(MovingRobot e: this){
			if(lowerEnergyRobot.getEnergy() >= e.getEnergy()) lowerEnergyRobot = e;
		}

		lowerEnergyRobot.setTarget(true);
	}

	public MovingRobot getTarget(){
		for(MovingRobot e: this){
			if(e.getTarget()==true) return e;
		}
		return new MovingRobot();
	}

	public double getTargetX(){
		for(MovingRobot element: this){
			if(element.getTarget()==true) return element.getx();
		}
		return 0;// ダミー
	}

	public double getTargetY(){
		for(MovingRobot element: this){
			if(element.getTarget()==true) return element.gety();
		}
		return 0;// ダミー
	}

	public double targetWillX(long theTime){
		for(MovingRobot element: this){
			if(element.getTarget()==true) return element.getWillX(theTime);
		}
		return 0;// ダミー
	}

	public double targetWillY(long theTime){
		for(MovingRobot element: this){
			if(element.getTarget()==true) return element.getWillY(theTime);
		}
		return 0;// ダミー
	}

	public void removeRobot(String theName){
		for(MovingRobot element: this){
			if(theName.equals(element.getName())==true) this.remove(element);
		}
	}

}
