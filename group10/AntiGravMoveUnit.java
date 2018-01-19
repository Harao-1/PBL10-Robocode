package group10;
import java.util.Set;

import robocode.*;

public class AntiGravMoveUnit {
	private double xforce, yforce,r_xforce, r_yforce,w_xforce,w_yforce;
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
		r_xforce = 0;
		r_yforce = 0;
		w_xforce = 0;
		w_yforce = 0;
		//ロボからの反重力
    	for(AntiGrav p: map) {
    		force = p.weight / Math.pow(getDistance(robot.getX(), robot.getY(), p.x, p.y), 2);	//a=W/R^2
    		ang = Math.PI / 2 - Math.atan2(robot.getY() - p.y, robot.getX() - p.x);	//力の向き
    		r_xforce += force * Math.sin(ang);
    		r_yforce += force * Math.cos(ang);
    		/*
    		ang = Math.atan2(robot.getY() - p.y, robot.getX() - p.x);	//力の向き
    		xforce += force * Math.cos(ang);
    		yforce += force * Math.sin(ang);
    		*/
    		//System.out.println("r_xforce: " + r_xforce + " r_yforce: " + r_yforce); //デバッグ用
    	}
    	
    	/*
    	//固定点からの反重力
    	for(AntiGrav p: fixedPointerMap) {
    		force = p.weight / Math.pow(getDistance(robot.getX(), robot.getY(), p.x, p.y), 2);	//a=W/R^3
    		ang = Math.PI / 2 - Math.atan2(robot.getY() - p.y, robot.getX() - p.x);	//力の向き
    		xforce += force * Math.sin(ang);
    		yforce += force * Math.cos(ang);
    		//System.out.println("x: " + p.x + " y: " + p.y);//デバッグ用
    		//System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
    	}
    	*/
    	
    	//壁からの重力
    	final double WALLFORCE = 500000;
    	final int WALLNUMBER = 3;
    	w_xforce -= WALLFORCE / Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getBattleFieldWidth(), robot.getY()), WALLNUMBER);
    	w_xforce += WALLFORCE / Math.pow(getDistance(robot.getX(), robot.getY(),
    	 0, robot.getY()), WALLNUMBER);
    	w_yforce -= WALLFORCE / Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getX(), robot.getBattleFieldHeight()), WALLNUMBER);
    	w_yforce += WALLFORCE / Math.pow(getDistance(robot.getX(), robot.getY(),
    	 robot.getX(), 0), WALLNUMBER);
    	
    	//System.out.println("w_xforce: " + w_xforce + " w_yforce: " + w_yforce); //デバッグ用
    	
    	xforce = r_xforce + w_xforce;
    	yforce = r_yforce + w_yforce;
    	System.out.println("xforce: " + xforce + " yforce: " + yforce); //デバッグ用
	}
	
	//反重力移動メソッド
	public void AntiGravMove(){
		AntiGravCalc();
		int dir = 1;
	    double dist = 20;	//移動距離
	    double forceangle = toRobocodeDegrees(Math.toDegrees(Math.atan2(yforce, xforce)));	//力の向き
	    double moveangle = changeDegrees(forceangle - robot.getHeading());	//移動する向き
	    System.out.println("forceangle: " + forceangle); //デバッグ用
	    System.out.println("moveangle: " + moveangle); //デバッグ用
	    
	    
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
	
	private double changeDegrees(double degrees) {
		if(degrees <= -180) {
			return 360 + degrees;
		}else if(degrees >= 180) {
			return -360 + degrees;
		}else {
			return degrees;
		}
	}
}
