package group10;
import robocode.*;

public class AttackProcessingUnit {
	private AdvancedRobot theRobot;
	private MovingRobotMap theMap;
	
	private double power; //弾の威力
	private MovingRobot target;
	private double moveDegrees;
	private double width, height;
	private boolean state;//trueのとき等速度予測，falseのとき等加速度予測
	private int hitA, hitV, missA, missV;
	private int cnt;
	
	private final int STATE_CHANGE_CNT = 5;
	
	public AttackProcessingUnit(AdvancedRobot inRobot, MovingRobotMap inMap, double p){
		theRobot = inRobot;
		theMap = inMap;
		power = p;
		moveDegrees = 0;
		state = true;
	}
	
	public void setField(double w, double h) {
		width = w;
		height = h;
	}
	
	public void succeeded() {
		cnt += 1;
		if(state) hitV += 1;
		else hitA += 1;
	}
	
	public void missed() {
		cnt -= 1;
		if(state) missV += 1;
		else missA += 1;
		if(cnt <= -STATE_CHANGE_CNT) {
			state = !state;
			System.out.println(state);
		}
	}
	
	public void printHitAndMiss() {
		System.out.println("hitV: " + hitV + " hitA: " + hitA);
		System.out.println("missV: " + missV + " missA: " + missA);
	}
	
	public boolean processing(){
		double distance;
		double targetDegrees;
		
		if(moveDegrees == 0) {
			//ターゲットを決めて回転
			//最も近い敵にターゲットを定める
			distance = Double.MAX_VALUE;
			for(MovingRobot p: theMap) {
				if(distance > getDistance(theRobot.getX(), theRobot.getY(), p.getWillX_V(theRobot.getTime()), p.getWillY_V(theRobot.getTime()))) {
					if(p.IsEnemy()) {
						target = p;
					}
				}
			}
			
			//ターゲットが決まった場合は砲塔の回転，決まらなかった場合（初期でmapに何も入っていない場合）はfalseを返す
			if(target != null) {
				//単純な予測
				//targettime = theRobot.getTime() + getDistance(theRobot.getX(), theRobot.getY(), target.getWillX_V(theRobot.getTime()), target.getWillY_V(theRobot.getTime())) / (20 - 3 * power);
				//複雑な予測
				
				if(state) {
					targetDegrees = attackV();
				}else {
					targetDegrees = attackA();
				}
				moveDegrees = changeDegrees(targetDegrees - theRobot.getGunHeading());
			
				if(Math.abs(moveDegrees) > 20) {
					theRobot.setTurnGunRight(moveDegrees);
					if(moveDegrees > 0) {
						moveDegrees -= 20;
					}else {
						moveDegrees += 20;
					}
				}else {
					theRobot.setTurnGunRight(moveDegrees);
					moveDegrees = 0;
				}
			}else {
				return false;
			}
		}else { 
			//ターゲットが定まっているが砲塔の回転が足りない場合の処理
			//砲塔を回転させる処理だけ行う
			//targettime = theRobot.getTime() + getDistance(theRobot.getX(), theRobot.getY(), target.getWillX_V(theRobot.getTime()), target.getWillY_V(theRobot.getTime())) / (20 - 3 * power);

			//複雑な予測

	
			if(state) {
				targetDegrees = attackV();
			}else {
				targetDegrees = attackA();
			}
			moveDegrees = changeDegrees(targetDegrees - theRobot.getGunHeading());
			
			if(Math.abs(moveDegrees) >= 20) {
				theRobot.setTurnGunRight(moveDegrees);
				if(moveDegrees > 0) {
					moveDegrees -= 20;
				}else {
					moveDegrees += 20;
				}
			}else {
				theRobot.setTurnGunRight(moveDegrees);
				moveDegrees = 0;
			}	
		}
		
		//弾を撃つときはtrueを返す
		if((moveDegrees == 0) && (theRobot.getGunHeat() <= 0)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	//計算補助メソッド（2点間の距離を計算）
	private double getDistance(double x1, double y1, double x2, double y2) {
		double x = x2-x1;
    	double y = y2-y1;
    	double distance = Math.sqrt(x*x + y*y);
    	return distance;  
	}
	
	//計算補助メソッド（(角度調整用メソッド)return -180 <= degrees <= 180）
	private double changeDegrees(double degrees) {
		//System.out.println(degrees);	//デバッグ用
		if(degrees <= -180) {
			return 360 + degrees;
		}else if(degrees >= 180) {
			return -360 + degrees;
		}else {
			return degrees;
		}
	}
	
	private double returnTimeV(AdvancedRobot r, MovingRobot e) {
		double theta;
		double t;
		double l, v, p;
		double a,b,c;
		l = getDistance(r.getX(), r.getY(), e.getWillX_V(r.getTime()), e.getWillY_V(r.getTime()));
		v = Math.sqrt(Math.pow(e.getVelocityX(), 2) + Math.pow(e.getVelocityY(), 2));
		p = 20 - 3 * power;
		theta = Math.atan2(r.getY() - e.getWillY_V(r.getTime()), r.getX() - e.getWillX_V(r.getTime())) - Math.atan2(e.getVelocityY(), e.getVelocityX());
		a = Math.pow(p, 2) - Math.pow(v, 2);
		b = l*v*Math.cos(theta);
		c = -Math.pow(l, 2);
		t = (-b + Math.sqrt(Math.pow(b, 2) - a*c)) / a;
		if(t >= 0) {
			return t;
		}else {
			t = (-b - Math.sqrt(Math.pow(b, 2) - a*c)) / a;
			return t;
		}
	}
	
	private double returnTimeA(AdvancedRobot r, MovingRobot e) {
		double theta;
		double t;
		double l, v, p;
		double a,b,c;
		l = getDistance(r.getX(), r.getY(), e.getWillX_A(r.getTime()), e.getWillY_A(r.getTime()));
		v = Math.sqrt(Math.pow(e.getVelocityX(), 2) + Math.pow(e.getVelocityY(), 2));
		p = 20 - 3 * power;
		theta = Math.atan2(r.getY() - e.getWillY_A(r.getTime()), r.getX() - e.getWillX_A(r.getTime())) - Math.atan2(e.getVelocityY(), e.getVelocityX());
		a = Math.pow(p, 2) - Math.pow(v, 2);
		b = l*v*Math.cos(theta);
		c = -Math.pow(l, 2);
		t = (-b + Math.sqrt(Math.pow(b, 2) - a*c)) / a;
		if(t >= 0) {
			return t;
		}else {
			t = (-b - Math.sqrt(Math.pow(b, 2) - a*c)) / a;
			return t;
		}
	}
	
	private double attackV() {
		double targettime;
		double targetX, targetY;
		double tD;
		
		targettime = theRobot.getTime() + returnTimeV(theRobot, target);
		targetX = target.getWillX_V(targettime);
		if(targetX < 0) targetX = 0;
		else if(targetX > width) targetX = width;
		targetY = target.getWillY_V(targettime);
		if(targetY < 0) targetY = 0;
		else if(targetY > height) targetY = height;
		
		tD = 90 - Math.toDegrees(Math.atan2(targetY - theRobot.getY(), targetX - theRobot.getX()));

		return tD;
	}
	
	private double attackA() {
		double targettime;
		double targetX, targetY;
		double tD;
		
		targettime = theRobot.getTime() + returnTimeA(theRobot, target);
		targetX = target.getWillX_A(targettime);
		if(targetX < 0) targetX = 0;
		else if(targetX > width) targetX = width;
		targetY = target.getWillY_A(targettime);
		if(targetY < 0) targetY = 0;
		else if(targetY > height) targetY = height;
		
		tD = 90 - Math.toDegrees(Math.atan2(targetY - theRobot.getY(), targetX - theRobot.getX()));

		return tD;
	}
}
