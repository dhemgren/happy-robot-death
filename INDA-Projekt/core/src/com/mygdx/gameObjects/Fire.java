package com.mygdx.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.core.GlobalVar;
import com.mygdx.objectData.FireData;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Fire {
	
	public Body body;
	public boolean active;
	public State state;
	
	private FireData data;
	
	private Animation fireUp, fireDown, fireLeft, fireRight, fireMid, fireVert, fireHori;
	
	public float animTimer;
	private float animDuration;

	
	public enum State{
		Mid, Horizontal, Vertical, Up, Down, Left, Right
	}
	
	private Vector2 poolPosition;
	
	public Fire(World world, Vector2 poolPosition){
		active = false;
		animTimer = 0;
		state = State.Mid; //Default to center fire tile
		this.poolPosition = poolPosition;
		BodyDef bdef = new BodyDef();
		body = world.createBody(bdef);
        bdef.allowSleep = false;
		body.setUserData(this);
		body.setType(BodyType.DynamicBody);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.3f, 0.3f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = GlobalVar.BIT_FIRE;
		fdef.filter.maskBits = GlobalVar.BIT_BOX | GlobalVar.BIT_WALL | GlobalVar.BIT_ITEM |
				GlobalVar.BIT_PLAYER | GlobalVar.BIT_BOMB;
		shape.dispose();
		
		/**
		 * Set Fire fixture to sensor (important).
		 * This means that the fire will not
		 * collide with other objects, but will still
		 * send and receive Contact-information,
		 * which is exactly what we want to happen
		 * with fire tiles. The fire-object should tell us
		 * if a player or box has been hit, but should not
		 * directly collide with it.
		 */
		fdef.isSensor = true;  				  
		body.createFixture(fdef).setUserData("fire");
		
		data = new FireData((byte)GlobalVar.BIT_FIRE, body.getPosition().x, body.getPosition().y, animTimer, animDuration);
		createFireAnimations();
		
	}
	
	//Return firetile to pool

	
	public void update(float dt){
		updateFireData();
		
		animTimer += dt;
		if(animTimer > animDuration){
			reset();
		}
		
	}
	
	public void updateFireData(){
		data.SetPosition(body.getPosition().x - 0.5f, body.getPosition().y - 0.5f); //Include render offset in position
		data.setActive(active);
		data.setAnimTimer(animTimer);
		
		//Record current state in FireData
		switch(state){
		case Up:
			data.setState(GlobalVar.FIRE_UP);
			break;
		case Down:
			data.setState(GlobalVar.FIRE_DOWN);
			break;
		case Left:
			data.setState(GlobalVar.FIRE_LEFT);
			break;
		case Right:
			data.setState(GlobalVar.FIRE_RIGHT);
			break;
		case Mid:
			data.setState(GlobalVar.FIRE_MID);
			break;
		case Horizontal:
			data.setState(GlobalVar.FIRE_HORIZONTAL);
			break;
		case Vertical:
			data.setState(GlobalVar.FIRE_VERTICAL);
			break;
		}
		
		
	}
	
	/**
	 * Imports bombfire.png and creates all fire animations
	 */
	
	private void createFireAnimations(){
		TextureAtlas sheet = new TextureAtlas(Gdx.files.internal("sprites/items/bombfire.txt"));
		float framerate = 1/24f; 
		
		fireUp = new Animation(framerate, sheet.findRegions("fireup"));
		fireUp.setPlayMode(PlayMode.NORMAL);
		
		animDuration = fireUp.getAnimationDuration(); //All animations are of equal length
		
		fireDown = new Animation(framerate, sheet.findRegions("firedown"));
		fireDown.setPlayMode(PlayMode.NORMAL);
		
		fireLeft = new Animation(framerate, sheet.findRegions("fireleft"));
		fireLeft.setPlayMode(PlayMode.NORMAL);
		
		fireRight = new Animation(framerate, sheet.findRegions("fireright"));
		fireRight.setPlayMode(PlayMode.NORMAL);
		
		fireMid = new Animation(framerate, sheet.findRegions("firemid"));
		fireMid.setPlayMode(PlayMode.NORMAL);
		
		fireVert = new Animation(framerate, sheet.findRegions("firevert"));
		fireVert.setPlayMode(PlayMode.NORMAL);
		
		fireHori = new Animation(framerate, sheet.findRegions("firehori"));
		fireHori.setPlayMode(PlayMode.NORMAL);
		
	}
	
	public Animation animation(){
		
		switch(state){
			case Mid:
				return fireMid;
		 	
			case Up:
				return fireUp;
		 		
			case Down:
				return fireDown;
		 		
			case Left:
				return fireLeft;
		 		
			case Right:
				return fireRight;
		 		
			case Horizontal:
				return fireHori;
				
			case Vertical:
				return fireVert;
		 	
			default:
				return fireMid;

		}
	}
	
	public FireData getData(){return data;}
	
	private void reset(){
		body.setTransform(poolPosition, 0);
		active = false;
		animTimer = 0;
	}
	
}
