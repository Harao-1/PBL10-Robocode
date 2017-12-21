package group10;
import robocode.*;
import java.util.*;
import java.awt.Color;

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

	private AttackProcessingUnit APU = new AttackProcessingUnit(this, movingRobotMap);
	private AntiGravMoveUnit AGMU = new AntiGravMoveUnit(this, movingRobotMap, fixedPointerMap);

	final int FIXED_WEIGHT = 1;
	final int ROBOT_WEIGHT = 2;

	public void run() {

		setColors(Color.green, Color.red, Color.blue);

		fixedPointerMap.add(new FixedPointer(getBattleFieldWidth()/2, getBattleFieldHeight()/2, FIXED_WEIGHT));
		for(int x_i=0; x_i<=8; x_i++){
			fixedPointerMap.add(new FixedPointer(this.getBattleFieldWidth()/8*x_i, 0, FIXED_WEIGHT));
			fixedPointerMap.add(new FixedPointer(this.getBattleFieldWidth()/8*x_i, getBattleFieldHeight(), FIXED_WEIGHT));
		}
		for(int y_i=1; y_i<6; y_i++){
			fixedPointerMap.add(new FixedPointer(0, getBattleFieldHeight()/6*y_i, FIXED_WEIGHT));
			fixedPointerMap.add(new FixedPointer(getBattleFieldWidth(), getBattleFieldHeight()/6*y_i, FIXED_WEIGHT));
		}
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop

		int i=0;	// 周期を稼ぐ制御変数
		long standardTime = getTime();
			System.out.println("1st");
		while(true) {
			setTurnRadarLeftRadians(45);
			movingRobotMap.setTarget(); // これではいけない。とにかくTargetが更新されていってしまう
			execute();
//			if(getTime() - standardTime > 20){
			// Replace the next 4 lines with any behavior you would like
//			setTurnGunRight(360);
			standardTime = getTime();
			movingRobotMap.printForDebug();
/*			if(i++ == 2000){
				i=0;*/
			AGMU.AntiGravMove();
			execute();
			//APU.processing();
		//	}
//			}
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		System.out.println("Sccaned:"+ e.getName());
		MovingRobot theScannedRobot = new MovingRobot(e, this);
		theScannedRobot.setWeight(ROBOT_WEIGHT);
		movingRobotMap.updateTheData(theScannedRobot);
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
/*	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}*/
	
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