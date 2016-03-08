package com.mygdx.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.core.GlobalVar;
import com.mygdx.objectData.BombData;
import com.mygdx.player.Player;

public class Bomb {
	private byte firePower;
	private float timeToDetonate;
	private float timer;
	
	private Player owner;
	
	//Pool variables
	public boolean active;
	public boolean detonate;
	public Vector2 detonatePosition;
	
	private BombData data;

	private Vector2 poolPosition;
	public Body body; //Body for easy positioning in world
	
	public State state;
	
	//***Animations***
	private Animation tickingAnim;
	private Animation blowUpAnim;
	private static float framerate = 1/12f; 
	private float animTimer;
	//
	
	public enum State{
		Ticking, Exploding, Idle
	}
	
	
	public Bomb(byte firePower, float timeToDetonate, World world, Vector2 poolPosition){
		
		this.poolPosition = poolPosition;
		active = false;
		
		this.timeToDetonate = timeToDetonate;
		timer = 0;
		
		TextureAtlas spriteSheet = new TextureAtlas(Gdx.files.internal("sprites/items/items.txt"));
		
		BodyDef bdef = new BodyDef();
		body = world.createBody(bdef);
		body.setType(BodyType.KinematicBody); // Set bomb bodies to ignore physics
		body.setTransform(poolPosition, 0); //Set body at pool position
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2f, new Vector2(0.5f, 0.5f), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = GlobalVar.BIT_BOMB;
		fdef.filter.maskBits = GlobalVar.BIT_FIRE;
		body.createFixture(fdef);
		shape.dispose();
		detonate = false;
		
		detonatePosition = new Vector2(0f, 0f);
		
		
		state = State.Idle;
		this.firePower = firePower;
		tickingAnim = new Animation(framerate, spriteSheet.findRegions("bomb"));
		tickingAnim.setPlayMode(PlayMode.LOOP);
		
		data = new BombData((byte)GlobalVar.BIT_BOMB, body.getPosition().x, body.getPosition().y, false);
		body.setUserData(data); // Store reference to this bombs data in world body

	
	}
	
	public BombData getData(){return data;}
	public void setFirePower(byte f){firePower = f;}
	public byte getFirePower(){return firePower;}
	public void setOwner(Player player){owner = player;}
	public Player getOwner(){return owner;}
	
	
	public void update(float dt){
		timer += dt;
		
		if(timer >= timeToDetonate && !detonate || data.detonate() && !detonate)
			detonate();
		
		updateBombData();
	}
	
	
	public void updateBombData(){
		data.SetPosition(body.getPosition().x, body.getPosition().y);
		data.setActive(active);
		data.setDetonate(detonate);
		data.setDetonatePosition(detonatePosition.x, detonatePosition.y);
	}
	
	/**
	 * Flag this bomb for detonation at its position
	 * and then return it to the pool.
	 */
	public void detonate(){
		detonatePosition = new Vector2(body.getPosition().x, body.getPosition().y);
		detonate = true;
		data.flagDetonation();
		reset();
	}
		
	public void reset(){
		active = false;
		timer = 0f;
		body.getFixtureList().first().getFilterData().maskBits = GlobalVar.BIT_FIRE;

		//Return to pool position in world space
		body.setTransform(poolPosition, 0);
		updateBombData();
	}

	
	public TextureRegion animation(){		
		switch(state){
		case Idle: return tickingAnim.getKeyFrame(timer, true);
		
		case Ticking: return tickingAnim.getKeyFrame(timer, true);
		
		case Exploding: return tickingAnim.getKeyFrame(timer, true);
		
		default: return tickingAnim.getKeyFrame(timer, true);
		}
	}

}
