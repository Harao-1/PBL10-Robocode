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

	final int FIXED_WEIGHT = 1;
	private double xforce, yforce;

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

		AntiGravMove();
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
				AntiGravMove();
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
		theScannedRobot.setWeight(2);
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

	private void AntiGravCalc() {

	final int FIXED_EXPONENT = 2;
    	double force;
    	double ang;
    	
		xforce = 0;
		yforce = 0;
		//ロボからの反重力
    	for(AntiGrav p: movingRobotMap) {
    		force = p.weight / Math.pow(getDistance(getX(), getY(), p.x, p.y), 2);	//a=W/R^2
    		ang = Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x);	//力の向き
    		xforce += force * Math.sin(ang);
    		yforce += force * Math.cos(ang);
    		//System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
    	}
    	//固定点からの反重力
    	for(AntiGrav p: fixedPointerMap) {
    		force = p.weight / Math.pow(getDistance(getX(), getY(), p.x, p.y), FIXED_EXPONENT);	//a=W/R^3
    		ang = Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x);	//力の向き
    		xforce += force * Math.sin(ang);
    		yforce += force * Math.cos(ang);
    		//System.out.println("x: " + p.x + " y: " + p.y);//デバッグ用
    		//System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
    	}
    	
	}
	
	//反重力移動メソッド
	public void AntiGravMove(){
		AntiGravCalc();
		int dir = 1;
	    double dist = 20;	//移動距離
	    double forceangle = toRobocodeDegrees(Math.toDegrees(Math.atan2(yforce, xforce)));	//力の向き
	    double moveangle = forceangle - getHeading();	//移動する向き
	    //System.out.println("forceangle: " + forceangle); //デバッグ用
	    //System.out.println("moveangle: " + moveangle); //デバッグ用
	    
	    
	    //前進するか後退するか，右に曲がるか左に曲がるか決める
	    if(Math.abs(moveangle) <= 90) {
	    	dir = 1;
	    }else {
	    	//System.out.println("dir = -1");//デバッグ用
	    	dir = -1;
	    	if(moveangle >= 90) {
	    		moveangle -= 180;
	    	}else {
	    		moveangle += 180;
	    	}
	    }
	    
	    setTurnRight(moveangle);	//移動する方向を向く
	    setAhead(dist * dir);	//指定距離だけ進む
	}
	
	//計算補助メソッド（2点間の距離を計算）
	private double getDistance(double x1, double y1, double x2, double y2) {
		double x = x2-x1;
    	double y = y2-y1;
    	double distance = Math.sqrt(x*x + y*y);
    	return distance;  
	}
	
	//計算補助メソッド（Robocode用の角度に調整(-180 < return <= 180)）
	private double toRobocodeDegrees(double degrees) {
		//テスト用
		if((-180 <= degrees) && (degrees < -90)){
			return (-270 - degrees);
		}else if((-90 <= degrees) && (degrees < 270)) {
			return (90 - degrees);
		}else {
			System.out.println("Error: toRobocodeDegrees()");
			return (90 - degrees);
		}
		
		/*//本番用
		if(degrees < -90){
			return (-270 - degrees);
		}
		else{
			return (90 - degrees);
		}
		*/
	}
}
