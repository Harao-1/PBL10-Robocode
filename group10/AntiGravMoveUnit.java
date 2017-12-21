package group10;
import java.util.Set;

import robocode.*;

public class AntiGravMoveUnit {
	private double xforce, yforce;
	private AdvancedRobot robot;
	private MovingRobotMap map;
	private Set<FixedPointer> fixedPointerMap;
	
	AntiGravMoveUnit(AdvancedRobot r, MovingRobotMap m, Set<FixedPointer> f){
		robot = r;
		map = m;
		fixedPointerMap = f;
	}
	
	private void AntiGravCalc() {

    	double force;
    	double ang;
    	
		xforce = 0;
		yforce = 0;
		//ロボからの反重力
    	for(AntiGrav p: map) {
    		force = p.weight / Math.pow(getDistance(robot.getX(), robot.getY(), p.x, p.y), 2);	//a=W/R^2
    		ang = Math.PI / 2 - Math.atan2(robot.getY() - p.y, robot.getX() - p.x);	//力の向き
    		xforce += force * Math.sin(ang);
    		yforce += force * Math.cos(ang);
    		//System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
    	}
    	
    	//固定点からの反重力
    	for(AntiGrav p: fixedPointerMap) {
    		force = p.weight / Math.pow(getDistance(robot.getX(), robot.getY(), p.x, p.y), 2);	//a=W/R^3
    		ang = Math.PI / 2 - Math.atan2(robot.getY() - p.y, robot.getX() - p.x);	//力の向き
    		xforce += force * Math.sin(ang);
    		yforce += force * Math.cos(ang);
    		//System.out.println("x: " + p.x + " y: " + p.y);//デバッグ用
    		//System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
    	}
    	
    	/*
    	//壁からの重力
    	final double WALLFORCE = 50;
    	xforce += WALLFORCE / Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getBattleFieldWidth(), robot.getY()), 3);
    	xforce -= WALLFORCE/Math.pow(getDistance(robot.getX(), robot.getY(),
    	 0, robot.getY()), 3);
    	yforce += WALLFORCE/Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getX(), robot.getBattleFieldHeight()), 3);
    	yforce -= WALLFORCE/Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getX(), 0), 3);
    	 */
    	
	}
	
	//反重力移動メソッド
	public void AntiGravMove(){
		AntiGravCalc();
		int dir = 1;
	    double dist = 20;	//移動距離
	    double forceangle = toRobocodeDegrees(Math.toDegrees(Math.atan2(yforce, xforce)));	//力の向き
	    double moveangle = forceangle - robot.getHeading();	//移動する向き
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
	    
	    robot.setTurnRight(moveangle);	//移動する方向を向く
	    robot.setAhead(dist * dir);	//指定距離だけ進む
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
