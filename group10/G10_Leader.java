package group10;
import robocode.*;
import java.util.*;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * G10_Leader - a robot by (your name here)
 */
public class G10_Leader extends TeamRobot
{
	/**
	 * run: G10_Leader's default behavior
	 */

	private Set<FixedPointer> fixedPointerMap = new HashSet<FixedPointer>();
	private MovingRobotMap movingRobotMap = new MovingRobotMap();

	public void run() {
		FixedPointer fp = new FixedPointer(getBattleFieldWidth()/2, getBattleFieldHeight()/2, 1);
		fixedPointerMap.add(fp);
		for(int x_i=0; x_i<=8; x_i++){
			fixedPointerMap.add(new FixedPointer(this.getBattleFieldWidth()/8*x_i, (double)0, (double)1));
			fixedPointerMap.add(new FixedPointer(this.getBattleFieldWidth()/8*x_i, getBattleFieldHeight(), (double)1));
		}
		for(int y_i=1; y_i<6; y_i++){
			fixedPointerMap.add(new FixedPointer(0, getBattleFieldHeight()/6*y_i, 1));
			fixedPointerMap.add(new FixedPointer(getBattleFieldWidth(), getBattleFieldHeight()/6*y_i, 1));
		}
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop

		while(true) {
			// Replace the next 4 lines with any behavior you would like
			movingRobotMap.setTarget(); // これではいけない。とにかくTargetが更新されていってしまう
			ahead(100);
			turnGunRight(360);
			back(100);
			turnGunRight(360);
			setTurnRadarLeftRadians(45);
			movingRobotMap.printForDebug();
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		System.out.println("Sccaned:"+ e.getName());
		MovingRobot theScannedRobot = new MovingRobot(e, this);
		movingRobotMap.updateTheData(theScannedRobot);
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	

	public void onRobotDeath(RobotDeathEvent e){
		movingRobotMap.removeRobot(e.getName());
	}
}
