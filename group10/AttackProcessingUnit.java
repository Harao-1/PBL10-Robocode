package group10;
import robocode.*;
import java.util.*;

public class AttackProcessingUnit {
	private AdvancedRobot theRobot;
	private MovingRobotMap theMap;

	private double power = 1.1; //弾の威力

	public AttackProcessingUnit(AdvancedRobot inRobot, MovingRobotMap inMap){
		theRobot = inRobot;
		theMap = inMap;
	}
	
	public void processing(){

	double distance = Math.sqrt(Math.pow((theMap.getTargetX() - theRobot.getX()), 2) + Math.pow((theMap.getTargetY() - theRobot.getY()), 2)); //ターゲットと自機との距離
	long attackTime = new Double(distance / (20 - 3 * power)).longValue(); //弾がターゲットに当たるまでの時間

	double targetDegrees; //ターゲットの向きの角度(-180度〜180度)
	if(theMap.targetWillX(theRobot.getTime() + attackTime) == theRobot.getX()) {
		if(theMap.targetWillY(theRobot.getTime() + attackTime) > theRobot.getY()) {
			targetDegrees = 0;
		} else {
			targetDegrees = 180;
		}
	} else {
		targetDegrees = Math.toDegrees(Math.atan((theMap.targetWillY(theRobot.getTime() + attackTime) - theRobot.getY()) / (theMap.targetWillX(theRobot.getTime() + attackTime) - theRobot.getX())));
	}
	
	double remainderDegrees =  180 + targetDegrees - theRobot.getGunHeading(); //ターゲットと砲台の向きの差
	if(remainderDegrees < -360) {
		theRobot.turnGunLeft(- remainderDegrees - 360);
	} else if(remainderDegrees >= -360 && remainderDegrees < -180) {
		theRobot.turnGunRight(remainderDegrees + 360);
	} else if(remainderDegrees >= -180 && remainderDegrees < 0) {
		theRobot.turnGunLeft(-remainderDegrees);
	} else if(remainderDegrees >= 0 && remainderDegrees < 180) {
		theRobot.turnGunRight(remainderDegrees);
	} else if(remainderDegrees >= 180 && remainderDegrees < 360) {
		theRobot.turnGunLeft(360 - remainderDegrees);
	} else if(remainderDegrees >= 360) {
		theRobot.turnGunRight(remainderDegrees - 360);
	} //ターゲットと砲台の向きを揃える

	theRobot.execute();
	long standardTime = theRobot.getTime();
	theRobot.execute();
	while(true){
		if(theRobot.getGunHeat()>-10) break;
		long tmpTime = theRobot.getTime() - standardTime;
		theRobot.execute();
		if(tmpTime > 10){
	theRobot.fire(power);
	theRobot.execute();
	break;
		}
	}

	}
	
	/**
	private double distanceAlly1 = Math.sqrt(Math.pow((ally1.x - getX()), 2) + Math.pow(ally1.y - getY()), 2)); //味方1と自機との距離
	private double distanceAlly2 = Math.sqrt(Math.pow((ally2.x - getX()), 2) + Math.pow(ally2.y - getY()), 2)); //味方2と自機との距離
	private double lineAlly1 = Math.abs((theMap.targetY - getY()) * (ally1.x - getX()) - (theMap.targetX - getX()) * (ally1.y - getY())) / Math.sqrt(Math.pow((theMap.targetX - getX()), 2) + (theMap.targetY - getY()), 2)); //味方1と予測線との距離
	private double lineAlly2 = Math.abs((theMap.targetY - getY()) * (ally2.x - getX()) - (theMap.targetX - getX()) * (ally2.y - getY())) / Math.sqrt(Math.pow((theMap.targetX - getX()), 2) + (theMap.targetY - getY()), 2)); //味方1と予測線との距離
	
	private boolean hitAlly1; //味方1に弾が当たるかどうか
	private boolean hitAlly2; //味方2に弾が当たるかどうか
	if((theMap.targetX - getX()) * (ally1.x - getX()) >= 0 && theMap.targetY - getY()) * (ally1.y - getY()) >= 0 && lineAlly1 < 100 - 8 * (distanceAlly1 / (20 - 3 * power))) { //味方1が予測線の近くにあれば
		hitAlly1 = true;
	} else {
		hitAlly1 = false;
	}
	if((theMap.targetX - getX()) * (ally2.x - getX()) >= 0 && theMap.targetY - getY()) * (ally2.y - getY()) >= 0 && lineAlly2 < 100 - 8 * (distanceAlly2 / (20 - 3 * power))) { //味方2が予測線の近くにあれば
		hitAlly2 = true;
	} else {
		hitAlly2 = false;
	}
	
	if(hitAlly1 == false && hitAlly2 == false) { //弾が味方に当たらないならば
		fire(power);
	}
	*/
}
