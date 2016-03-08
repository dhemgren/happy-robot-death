package com.mygdx.objectData;

import com.mygdx.core.GlobalVar;

public class BoxData extends ObjectData {
	
	public BoxData(){
		this.type = GlobalVar.BIT_BOX;
		x = 0f;
		y = 0f;
		active = false;
	}
	
	public BoxData(byte type, float x, float y, boolean active){
		this.type = type;
		this.x = x;
		this.y = y;
		this.active = active;
	}
	
}
