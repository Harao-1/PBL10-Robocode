package group10;
import java.util.HashSet;
import java.util.Set;

import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * G10_Leader - a robot by (your name here)
 */
public class G10_Leader_AntiGravMove extends TeamRobot
{
	/**
	 * run: G10_Leader's default behavior
	 */

	private Set<FixedPointer> fixedPointerMap = new HashSet<FixedPointer>();
	private MovingRobotMap movingRobotMap = new MovingRobotMap();
	private double xforce, yforce;

	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			setTurnGunRight(360);
			AntiGravMove();
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
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
	
	
	private void AntiGravCalc() {
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
    		force = p.weight / Math.pow(getDistance(getX(), getY(), p.x, p.y), 3);	//a=W/R^3
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