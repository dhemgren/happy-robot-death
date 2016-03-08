package com.mygdx.objectData;

import com.mygdx.core.GlobalVar;
import com.mygdx.objectData.PlayerData;

public class FireData extends ObjectData {
	
	private float animTimer;
	private float animDuration;
	
	private byte state;
	
	public FireData(){
		type = (byte)GlobalVar.BIT_FIRE;
		x = 0f;
		y = 0f;
		animTimer = 0f;
		animDuration = 0f;
		state = GlobalVar.FIRE_MID;
	}
	
	public FireData(byte type, float x, float y, float animTimer, float animDuration){
		this.type = type;
		this.x = x;
		this.y = y;
		this.animTimer = animTimer;
		this.animDuration = animDuration;
		state = (byte)GlobalVar.FIRE_MID;
	}
	
	
	public void setState(byte state){this.state = state;}
	public byte getState(){return state;}
	public float getAnimTimer(){return animTimer;}
	public void setAnimTimer(float animTimer){this.animTimer = animTimer;}
	

}
