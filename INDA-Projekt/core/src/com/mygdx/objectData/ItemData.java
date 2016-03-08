package com.mygdx.objectData;

import com.mygdx.core.GlobalVar;

public class ItemData extends ObjectData{
	
	protected byte state;
	protected float animTimer;
	protected boolean pickedUp;
	protected byte itemType;
	
	public ItemData(){
		type = GlobalVar.BIT_ITEM;
		itemType = GlobalVar.BOMB_POWERUP;
		x = 0f;
		y = 0f;
		active = false;
		pickedUp = false;
	}
	
	public ItemData(byte itemType, float x, float y, float animTimer, boolean active){
		this.type = 16; // Hardcoded to B2DVars.BIT_ITEM = 16
		this.itemType = itemType;
		this.x = x;
		this.y = y;
		this.state = state;
		this.animTimer = animTimer;
		this.active = active;
		pickedUp = false;
	}
	
	public boolean pickedUp(){return pickedUp;}
	public void pickUp(){pickedUp = true;}
	public int itemType(){return itemType;}
	public void setAnimTimer(float animTimer){this.animTimer = animTimer;}
	public float animTimer(){return animTimer;}
	
}
