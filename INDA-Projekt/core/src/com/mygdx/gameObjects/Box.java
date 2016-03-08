package com.mygdx.gameObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.GlobalVar;
import com.mygdx.objectData.BoxData;

public class Box {
	
	public Body body;
	private BoxData data;
	private boolean active;
	
	public Box(Body body){
		this.body = body;
		data = new BoxData((byte)GlobalVar.BIT_BOX, body.getPosition().x, body.getPosition().y, true);
		body.setUserData(data);
	}
	
	public void update(float dt){
		active = body.isActive();
		updateBoxData();
	}

	private void updateBoxData(){
		data.SetPosition(body.getPosition().x, body.getPosition().y);
		data.setActive(active);
	}
	
	public boolean isActive(){return active;}
	public void setActive(boolean b){active = b;}
}
