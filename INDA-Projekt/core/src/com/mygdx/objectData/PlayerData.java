package com.mygdx.objectData;

import com.mygdx.core.GlobalVar;

public class PlayerData extends ObjectData{

	private byte state;
	private byte playerNumber;
	
	private float velX;
	private float velY;
	private boolean immortal;
	
	private byte bombCapacity;
	private byte firePower;
	
	public PlayerData(){
		state = GlobalVar.PLAYER_DOWN;
		x = 0f;
		y = 0f;
		active = true;
		playerNumber = 1;
		velX = 0f;
		velY = 0f;
		firePower = 1;
		bombCapacity = 1;
	}
	
	public PlayerData(byte type, byte playerNumber, float x, float y, boolean active){
		this.type = type;
		this.x = x;
		this.y = y;
		this.active = active;
		this.playerNumber = playerNumber;
		velX = 0f;
		velY = 0f;
		bombCapacity = 1;
		firePower = 1;
	}

	public void setVelocity(float x, float y){
		velX = x;
		velY = y;
	}
	
	public void setState(byte state){
		this.state = state;
	}
	
	public void reset(){
		bombCapacity = 1;
		firePower = 1;
	}
	
	public void incrementBombCapacity(){bombCapacity++;}
	public void incrementFirePower(){firePower++;}

	public void kill(){
		active = false;
		state = GlobalVar.PLAYER_DEAD;
	}
	
	public int state(){return state;}
	public float velX(){return velX;}
	public float velY(){return velY;}
	public int playerNumber(){return playerNumber;}
	public boolean dead(){return !active;}
	public boolean immortal(){return immortal;}
	public byte getFirePower(){return firePower;}
	public byte getBombCapacity(){return bombCapacity;}
	
}
