package com.mygdx.objectData;

import com.mygdx.objectData.PlayerData;

/**
 * Container class used to send
 * game object information over 
 * tcp packets 
 * @author danhemgren
 *
 */
public abstract class ObjectData {

	//Coordinates in game world
	protected float x;
	protected float y;
	
	//If object is active
	protected boolean active;
	
	//Flag object for reset
	protected boolean reset;
	
	//Collision layer for this object
	protected byte type;
	
	public ObjectData(){
		x = 0f;
		y = 0f;
		active = false;
		reset = false;
		type = 0;
	}
	
	public void SetPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float posX(){return x;}
	public float posY(){return y;}
	public boolean active(){return active;}
	public short getType(){return type;}
	public void setActive(boolean active){this.active = active;}
	public void flagReset(){reset = true;}
	public void unflagReset(){reset = false;}
	public boolean resetFlagged(){return reset;}

}
