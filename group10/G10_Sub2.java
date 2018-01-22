package group10;
import robocode.*;
import java.util.*;
import java.awt.Color;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * G10_Leader - a robot by (your name here)
 */
public class G10_Sub2 extends TeamRobot
{
	/**
	 * run: G10_Leader's default behavior
	 */
	final int FIXED_WEIGHT = 5000;
	final int ROBOT_WEIGHT = 10000;
	final double power = 2.0;

	private Set<FixedPointer> fixedPointerMap = new HashSet<FixedPointer>();
	private MovingRobotMap movingRobotMap = new MovingRobotMap();

	private AttackProcessingUnit APU = new AttackProcessingUnit(this, movingRobotMap, power);
	private AntiGravMoveUnit AGMU = new AntiGravMoveUnit(this, movingRobotMap, fixedPointerMap);

	private boolean flg;

	public void run() {

		setColors(Color.green, Color.red, Color.blue);

		fixedPointerMap.add(new FixedPointer(getBattleFieldWidth()/2, getBattleFieldHeight()/2, FIXED_WEIGHT));
		
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		
		setAdjustGunForRobotTurn(true);//ロボットの回転に合わせて砲塔を動かさない
		APU.setField(getBattleFieldWidth(), getBattleFieldHeight());

		
		while(true) {
			setTurnRadarLeftRadians(45);
			movingRobotMap.setTarget(); // これではいけない。とにかくTargetが更新されていってしまう
			// Replace the next 4 lines with any behavior you would like
			flg = APU.processing();
			execute();
			if(flg) fire(power);
			AGMU.AntiGravMove();
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//System.out.println("Sccaned:"+ e.getName());
		MovingRobot theScannedRobot = new MovingRobot(e, this);
		theScannedRobot.setWeight(ROBOT_WEIGHT);
		movingRobotMap.updateTheData(theScannedRobot);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
/*	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}*/

	public void onRobotDeath(RobotDeathEvent e){
		movingRobotMap.removeRobot(e.getName());
	}
}