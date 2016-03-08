package com.mygdx.utils;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.GlobalVar;
import com.mygdx.core.ItemPool;
import com.mygdx.core.SoundManager;
import com.mygdx.gameObjects.Bomb;
import com.mygdx.gameObjects.BombPowerUp;
import com.mygdx.gameObjects.Fire;
import com.mygdx.gameObjects.FirePowerUp;
import com.mygdx.gameObjects.Bomb.State;
import com.mygdx.player.Player;

public class ItemPlacer {
	
	public static void spawnRandomPowerUp(Vector2 position){
		Random r = new Random();
		int i = r.nextInt(100);
		System.out.println("Randomized value: " + i);
		if(i < GlobalVar.DROP_RATE){
				if(i < GlobalVar.DROP_RATE / 2){
				placePowerUp(GlobalVar.BOMB_POWERUP, position);
				return;
				}
				
				if(i >= (GlobalVar.DROP_RATE / 2 + 1)){
				placePowerUp(GlobalVar.FIRE_POWERUP, position);
				return;
				}
			}
	}
	
	
	/**
	 * Place the specified power up at the specified position
	 * @param type 
	 * @param position
	 */
	
	public static void placePowerUp(int type, Vector2 position){
		switch(type){		
			case GlobalVar.BOMB_POWERUP:
				
				BombPowerUp bombPow;
				
				for(BombPowerUp b : ItemPool.bombPows){
					if(!b.active){
						bombPow = b;
						bombPow.body.setTransform(position, 0);
						bombPow.active = true;
						return;
					}		
				}
				
				break;		
			case GlobalVar.FIRE_POWERUP:
				
				FirePowerUp firePow;
				
				for(FirePowerUp f : ItemPool.firePows){
					if(!f.active){
						firePow = f;
						firePow.body.setTransform(position, 0);
						firePow.active = true;
						return;
					}		
				}
				
				break;
			default:
				break;	
		}	
	}
	
	public static void dropBomb(Player player){

		System.out.println("player: " + player.getPlayerNumber() + " firepower: " + player.getData().getFirePower() + " bomb capacity: " + player.getData().getBombCapacity()
				+ " can drop bomb: " + player.canDropBomb());

		
		if(!player.canDropBomb())
			return;
		
		byte firePower = player.getData().getFirePower();
		System.out.println("player: " + player.getPlayerNumber() + " firepower: " + firePower);
		Vector2 bombPosition = CoordinateConverter.quantizePositionToGrid(player.body.getPosition());

		for(Bomb bomb : ItemPool.bombs){
			if(!bomb.active){

				//Flag bomb as active, set state to Ticking, and set firepower to players current firepower
				bomb.active = true;
				bomb.state = Bomb.State.Ticking;
				bomb.setFirePower(firePower); 
				player.body.getPosition();
				//Quantize player position to nearest tile center and place bomb there
				bomb.body.setTransform(bombPosition, 0);
				bomb.setOwner(player);
				player.incrementActiveBombs();
				SoundManager.playSound(SoundManager.dropbomb);
				return;
			}	
		}	
	}
	
	public static Fire setFire(float x, float y, World world){
		Fire fire = null;
		
		for(Fire f : ItemPool.fires){
			if(!f.active){
				fire = f;
				break;
			}		
		}	
		
		//If all fires are active, create new fire
		if(fire == null){
			fire = new Fire(world, ItemPool.firePoolPosition);
			System.out.println("Fire pool buffer underrun");
		}

		fire.body.setTransform(x + 0.5f, y + 0.5f, 0);
		fire.active = true;
		
		return fire;
	}
}
