package com.mygdx.player;
import static com.mygdx.core.CoreGame.WORLD;
import static com.mygdx.core.GlobalVar.PPM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.GlobalVar;
import com.mygdx.objectData.PlayerData;
import com.mygdx.utils.CoordinateConverter;


public class Player {
public static final float MOVE_SPEED = 0.05f;
public static final float DAMPING = 0.8f;

public enum State{
	Idle, Up, Down, Left, Right, Dead
}

private State state;
private PlayerData data;
public Body body;

private boolean remove;

private int playerNumber;

//Player sprites and animation
private TextureAtlas spriteSheet;
private Animation upAnim, upIdleAnim, downAnim, downIdleAnim,
leftAnim, leftIdleAnim, rightAnim, rightIdleAnim;

//Player game attributes
private int bombCapacity;
private int droppedBombs;
private int activeBombs;
private Iterator bombIterator;
private int firePower;
private int speedCount;
private Vector2 spawnPosition;

public boolean immortal;

private boolean killed;

private int UP;
private int DOWN;
private int LEFT;
private int RIGHT;
private int BOMB;

/*
 * Creates a new player.
 * @param Vector2 position: denotes starting position
 * @param boolean player1: if true = player 1, false = player 2
 */

public Player(int player, Vector2 position, World world){
	
	if(player < 1)
		player = 1;
	
	if(player > 4)
		player = 4;
	
	playerNumber = player;
	
	immortal = false;
	
	spawnPosition = CoordinateConverter.quantizePositionToGrid(position);
	droppedBombs = 0;
	bombCapacity = 1;
	firePower = 1;
	speedCount = 1;
	killed = false;
	
	activeBombs = 0;
	
	//Create player body
	BodyDef bdef = new BodyDef();
	bdef.position.set(spawnPosition);
	bdef.type = BodyType.DynamicBody;
	bdef.allowSleep = false;
	
	FixtureDef fdef = new FixtureDef();
	
	PolygonShape shape = new PolygonShape();	//The box collider
	shape.setAsBox(0.3f, 0.25f);				//The box collider
	fdef.shape = shape;
	//Collision mask---* 
	fdef.filter.categoryBits = GlobalVar.BIT_PLAYER;
	fdef.filter.maskBits = GlobalVar.BIT_BOX | GlobalVar.BIT_WALL | 
			GlobalVar.BIT_ITEM | GlobalVar.BIT_FIRE | GlobalVar.BIT_BOMB;
	//-----------------*
	body = world.createBody(bdef);
	body.createFixture(fdef).setUserData("player");
	shape.dispose();

	//Initialize state to down
	state = State.Down;
	
	//Create player data. This will be sent as packets via kryonet
	data = new PlayerData((byte)GlobalVar.BIT_PLAYER, (byte)player, body.getPosition().x, body.getPosition().y, true);
	body.setUserData(data); // Store reference to data object in user data for external referencing

	createAnimations(playerNumber);
	}

	private void createAnimations(int playernr){

		//TODO: If player1 == false, construct using player 2 sprite sheet
		//i.e. using spriteSheet = player1 == true ? [player1sheet] : [player2sheet];
		//If more players are to be integrated, consider a switch statement
		
		String sheetFile = "sprites/characters/ninja" + playernr + ".txt";
		
		float framerate = 1/24f;
		spriteSheet = new TextureAtlas(Gdx.files.internal(sheetFile));
		
		upAnim = new Animation(framerate, spriteSheet.findRegions("up"));
		upAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		upIdleAnim = new Animation(framerate, spriteSheet.findRegion("up", 4));

		
		downAnim = new Animation(framerate, spriteSheet.findRegions("down"));
		downAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		downIdleAnim = new Animation(framerate, spriteSheet.findRegion("down", 4));

		rightAnim = new Animation(framerate, spriteSheet.findRegions("side"));
		rightAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		rightIdleAnim = new Animation(framerate * 1.25f, spriteSheet.findRegion("side", 4));
		
		
		/**
		 * Left running animation needs to be mirrored
		 */
		Array<TextureAtlas.AtlasRegion> left = spriteSheet.findRegions(("side"));
		
		for(TextureAtlas.AtlasRegion a : left)
			a.flip(true, false);
		
		leftAnim = new Animation(framerate, left);
		leftAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		leftIdleAnim = new Animation(framerate * 1.25f, left.get(3));
		
	}
	
	public void setBombCapacity(int c){
		if(c < 1)
			c = 1;
		
		if(c > GlobalVar.MAX_BOMBCAPACITY)
			c = GlobalVar.MAX_BOMBCAPACITY;
		
		bombCapacity = c;
	}
	
	public void incrementBombCapacity(){
		if(bombCapacity < GlobalVar.MAX_BOMBCAPACITY){
			bombCapacity++;
			
		data.incrementFirePower();
		}
	}
	
	public void setFirePower(int f){
		if(f < 1)
			f = 1;
		
		if(f >= GlobalVar.MAX_FIREPOWER)
			f = GlobalVar.MAX_FIREPOWER;
		
		firePower = f;
	}
	
	public void incrementFirePower(){
		if(firePower < GlobalVar.MAX_FIREPOWER){
			firePower++;
		
		data.incrementFirePower();
		}
	}
	
	public int getPlayerNumber(){return playerNumber;}
	public int getFirePower(){return firePower;}
	public void incrementActiveBombs(){activeBombs++;}
	public void decrementActiveBombs(){activeBombs--;}
	public boolean canDropBomb(){return activeBombs < data.getBombCapacity();}
	public void setState(State state){this.state = state;}
	public void remove(){remove = true;}
	public boolean removeThis(){return remove;}
	public void clearState(){state = null;}
	public void kill(){killed = true;}
	public boolean dead(){return killed;}
	public PlayerData getData(){return data;}
	

	public void update(){
		killed = data.dead();
		updatePlayerData();
	}
	
	public void reset(){
		data.setActive(true);
		killed = false;
		firePower = 1;
		bombCapacity = 1;
		data.reset();
		body.setActive(true);
		body.setTransform(spawnPosition, 0);
		activeBombs = 0;
	}
	
	
	private void updatePlayerData(){
		
		data.setActive(!killed);
		data.SetPosition(body.getPosition().x - 0.5f, body.getPosition().y - 0.3f);
		data.setVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y);
		
		switch(state){
			case Up:
				data.setState((byte)GlobalVar.PLAYER_UP);
				break;
			case Down:
				data.setState((byte)GlobalVar.PLAYER_DOWN);
				break;
			case Left:
				data.setState((byte)GlobalVar.PLAYER_LEFT);
				break;
			case Right:
				data.setState((byte)GlobalVar.PLAYER_RIGHT);
				break;
		}
	}

	
	public Animation animation(){
		switch(state){
			case Up:
				return body.getLinearVelocity().y > 0 ? upAnim : upIdleAnim;
			case Down:
				return body.getLinearVelocity().y < 0 ? downAnim : downIdleAnim;
			case Left:
				return body.getLinearVelocity().x < 0 ? leftAnim : leftIdleAnim;
			case Right:
				return body.getLinearVelocity().x > 0 ? rightAnim : rightIdleAnim;
			default:
				return downAnim;
		}	
	}

	public int keyUp(){return UP;}
	public int keyDown(){return DOWN;}
	public int keyLeft(){return LEFT;}
	public int keyRight(){return RIGHT;}
	public int keyBomb(){return BOMB;}
	
	public void setKeyUp(int u){UP = u;}
	public void setKeyDown(int d){DOWN = d;}
	public void setKeyLeft(int l){LEFT = l;}
	public void setKeyRight(int r){RIGHT = r;}
	public void setKeyBomb(int b){BOMB =b;}
	
}
