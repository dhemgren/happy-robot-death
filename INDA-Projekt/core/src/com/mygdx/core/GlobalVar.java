package com.mygdx.core;

public final class GlobalVar {
	
	//PPM = Pixels per meter. Conversion-variable. 
	public static final float PPM = 32f;
	
	//Collision categories
	public static final short BIT_PLAYER = 2;
	public static final short BIT_WALL = 4;
	public static final short BIT_BOX = 8;
	public static final short BIT_ITEM = 16;	
	public static final short BIT_EVERYTHING = -1;
	public static final short BIT_FIRE = 32;
	public static final short BIT_BOMB = 64;
	public static final short BIT_FRICTION = 128;
	
	public static final int MAX_FIREPOWER = 6;
	public static final int MAX_SPEED = 3;
	public static final int MAX_BOMBCAPACITY = 5;
		
	//Integer representations
	public static final byte PLAYER_UP = 1;
	public static final byte PLAYER_DOWN = 2;
	public static final byte PLAYER_LEFT = 3;
	public static final byte PLAYER_RIGHT = 4;
	public static final byte PLAYER_DEAD = 5;
	
	public static final byte FIRE_UP = 1;
	public static final byte FIRE_DOWN = 2;
	public static final byte FIRE_LEFT = 3;
	public static final byte FIRE_RIGHT = 4;
	public static final byte FIRE_HORIZONTAL = 5;
	public static final byte FIRE_VERTICAL = 6;
	public static final byte FIRE_MID = 7;
	
	public static final byte BOMB_POWERUP = 1;
	public static final byte FIRE_POWERUP = 2;
	
	public static final int BOXLAYER_INDEX = 5;
	
	public static final byte PLAYER_EMPTY = 0;
	public static final byte PLAYER_CONNECTED = 1;
	public static final byte PLAYER_READY = 2;

	public static final byte ERROR_GAME_FULL = 1;
	public static final byte ERROR_UNKNOWN = 2;
	public static final byte ERROR_ALL_PLAYERS_LEFT = 3;
	public static final byte ERROR_HOST_LEFT = 4;

	
	
	
	//Game config values:
	public static final boolean SCREEN_SHAKE = true;
	public static final float SHAKE_TIME = 0.1f;
	public static final float SHAKE_AMPLITUDE = 0.1f;  //Default 0.1f
	public static final int BOX_DENSITY = 50;
	public static final int LEVEL_RESET_SLEEPTIME = 4;
	public static final int MAX_PLAYERS = 4;
	
	public static final float MOVE_FORCE = 10f;
	public static final float MAX_MOVE_SPEED = 3f;
	
	public static final int VIRTUAL_HEIGHT = 9; //Height in world units, 1 unit = 1 tile
	public static final int VIRTUAL_WIDTH = 16; // Width in world units, 1 unit = 1 tile
	
	/**
	 * Lower this value if animations are choppy during network play.
	 * This will increase server side cpu usage. Default value 16.66667
	 */
	
	public static final long NETWORK_UPDATE_CYCLE_TIME = (long)16.66667;
	public static final int NETWORK_CLIENT_BUFFERSIZE = 262112;
	public static final int NETWORK_SERVER_BUFFERSIZE = 65536;
	
	public static final boolean SOUND = true;		//Enables sound fx.
	public static final boolean DEBUG_MODE = true;  //Enables single-player host mode for debug purposes
	
	public static final String NETWORK_DEFAULT_PORT_STRING = "54555";
	public static final int NETWORK_DEFAULT_PORT_INT = 54555;


	public static final float BOMB_TIME = 3f;
	public static final int DROP_RATE = 50; 		//The drop rate percentage for powerups
	//
	
	
	
}
