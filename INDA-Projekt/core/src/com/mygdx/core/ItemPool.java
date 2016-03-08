package com.mygdx.core;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameObjects.Bomb;
import com.mygdx.gameObjects.BombPowerUp;
import com.mygdx.gameObjects.Fire;
import com.mygdx.gameObjects.FirePowerUp;

public class ItemPool {
	
	// Items
	public static int pooledBombs = 60;
	public static Vector2 bombPoolPosition = new Vector2(-100, -100);
	public static Bomb[] bombs = new Bomb[pooledBombs];
	
	public static int pooledFire = 100; 
	public static Vector2 firePoolPosition = new Vector2(-200, -200);
	public static Fire[] fires = new Fire[pooledFire];
	
	public static int pooledPowerUps = 40; 
	public static Vector2 powPoolPosition = new Vector2(200, 200);
	public static BombPowerUp[] bombPows = new BombPowerUp[pooledPowerUps];
	public static FirePowerUp[] firePows = new FirePowerUp[pooledPowerUps];
	//

}
