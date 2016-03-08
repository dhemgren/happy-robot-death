package com.mygdx.objectData;

import com.mygdx.core.GlobalVar;

public class BombData extends ObjectData{
	
	private boolean detonate;
	private float detonateX; //position to detonate
	private float detonateY;
	private byte firePower;
	
	public BombData(){
		type = GlobalVar.BIT_BOMB;
		detonate = false;
		detonateX = 0f;
		detonateY = 0f;
		x = 0f;
		y = 0f;
		active = false;
		
	}

	public BombData(byte type, float x, float y, boolean active){
		this.type = type;
		this.x = x;
		this.y = y;
		this.active = active;
		
		detonateX = 0f;
		detonateY = 0f;
		detonate = false;
	}
	
	public void setDetonatePosition(float x, float y){
		detonateX = x;
		detonateY = y;
	}
	public float detonatePositionX(){return detonateX;}
	public float detonatePositionY(){return detonateY;}
	public void setDetonate(boolean detonate){this.detonate = detonate;}
	public boolean detonate(){return detonate;}
	public void flagDetonation(){detonate = true;}
	public void unflagDetonation(){detonate = false;}
	public int firePower(){return firePower;}
	
}
