package com.mygdx.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.gameObjects.Box;
import com.mygdx.map.MapRandomizer;
import com.mygdx.objectData.BoxData;
import com.mygdx.objectData.ItemData;
import com.mygdx.player.Player;

public class GameStateManager {
	
	public static boolean inputBlocked;
	public static final Texture winPlayer1 =  new Texture(Gdx.files.internal("sprites/texts/win1.png"));
	public static final Texture winPlayer2 = new Texture(Gdx.files.internal("sprites/texts/win2.png"));
	public static final Texture winPlayer3 = new Texture(Gdx.files.internal("sprites/texts/win3.png"));
	public static final Texture winPlayer4 = new Texture(Gdx.files.internal("sprites/texts/win4.png"));
	public static int playerWinner = -1;


	private static final float sleepTime = GlobalVar.LEVEL_RESET_SLEEPTIME;
	public static float sleepTimer = 0f;
	public static ArrayList<Player> allPlayers;
	
	public static Array<Box> resetGame(TiledMap tileMap, World world, Array<Box> boxes){
		playerWinner = -1;
		sleepTimer = 0f;
		inputBlocked = false;
		
		//**RESET PLAYER STATS**//
		for(Player p : allPlayers)
			p.reset();
		
		//**REMOVE BOXES FROM WORLD & RESET ITEMS**//
		Array<Body> bodies = new Array<Body>(); 
		world.getBodies(bodies);	
		
		for(Body b : bodies){
			if(b.getUserData() instanceof BoxData){
				world.destroyBody(b);
			}
			if(b.getUserData() instanceof ItemData){
				ItemData i = (ItemData)b.getUserData();
				i.flagReset();
			}
		}
				
		boxes.clear();
		tileMap.getLayers().remove(GlobalVar.BOXLAYER_INDEX);
		
		
		//**CONSTRUCT NEW BOXES**//
		MapRandomizer mr = new MapRandomizer();
		TiledMapTileLayer boxLayer = mr.fillMap(world, tileMap, GlobalVar.BOX_DENSITY);
		boxes = mr.boxes; // Update body references in boxes
		tileMap.getLayers().add(boxLayer);
		tileMap.getLayers().get(GlobalVar.BOXLAYER_INDEX).setVisible(false);
		
		//**RESET SOUND**//
		SoundManager.playSound(SoundManager.bg1);
		
		
		return boxes;
	}
	
	//Check if someone has won the game
	public static boolean playerWon(){
		int alivePlayers = 0;
		int winner = 0;
		for(Player p : allPlayers){
			if(!p.dead()){
				alivePlayers++;
				winner = p.getPlayerNumber();
			}
		}
		
		boolean hasWon = alivePlayers < 2;
		
		if(hasWon){
			playerWinner = winner;
			SoundManager.playSound(SoundManager.win);
		}
		
		return hasWon;
	}
	
	
	public static Player getPlayer(int playerNr){
		return allPlayers.get(playerNr-1);
	}
		
}

