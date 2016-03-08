package com.mygdx.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.core.GlobalVar;
import com.mygdx.core.SoundManager;
import com.mygdx.objectData.ItemData;
import com.mygdx.objectData.PlayerData;


/**
 * This class handles the collisions that happen in 
 * the world. Each Contact holds information about
 * the fixtures that collide.
 * @author danhemgren
 *
 */
public class ContactHandler implements ContactListener {

	public ContactHandler(){}
	
	@Override
	public void beginContact(Contact c) {
		//System.out.println("COLLISION DETECTED");
		Fixture a = c.getFixtureA();
		Fixture b = c.getFixtureB();
		short aCategory = c.getFixtureA().getFilterData().categoryBits; 
		short bCategory = c.getFixtureB().getFilterData().categoryBits;
		//System.out.println("Cat a: " + aCategory + " Cat b: " + bCategory);
		
		if(aCategory == GlobalVar.BIT_FIRE || bCategory == GlobalVar.BIT_FIRE){
			if(aCategory == GlobalVar.BIT_FIRE)
				handleFire(a, b);		
			else
				handleFire(b, a);
		}
		
		// If one category is player and the other is item -> handle item pickup
		if(aCategory == GlobalVar.BIT_ITEM || bCategory == GlobalVar.BIT_ITEM){
			if(aCategory == GlobalVar.BIT_PLAYER || bCategory == GlobalVar.BIT_PLAYER)
				handleItem(c.getFixtureA(), c.getFixtureB());
		}
		
	}

	
	private void handleFire(Fixture fire, Fixture other){
		
		short otherCategory = other.getFilterData().categoryBits;
		
		// Box collision handled via raycasting. Walls not affected by fire.
		if(otherCategory == GlobalVar.BIT_BOX || otherCategory == GlobalVar.BIT_WALL)
			return;
		
		// Fire hits player -> player dies
		if(otherCategory == GlobalVar.BIT_PLAYER){
			PlayerData p = (PlayerData)other.getBody().getUserData();
			
			if(!p.immortal())
				p.kill();
			
			return;
		}
			
		// Fire hits item -> item is destroyed
		if(otherCategory == GlobalVar.BIT_ITEM){
			System.out.println("Item destroyed");
			ItemData i = (ItemData)other.getBody().getUserData();
			i.flagReset();
			return;
		}
			
		
	}
	
	private void handleItem(Fixture a, Fixture b){
		PlayerData player;
		ItemData item;
		
		if(a.getFilterData().categoryBits == GlobalVar.BIT_PLAYER){
			player = (PlayerData)a.getBody().getUserData();
			item = (ItemData)b.getBody().getUserData();
			
			if(item.itemType() == GlobalVar.FIRE_POWERUP){
				SoundManager.playSound(SoundManager.powerup1);
				player.incrementFirePower();
				item.flagReset();
				return;	
			}
				
			if(item.itemType() == GlobalVar.BOMB_POWERUP){
				SoundManager.playSound(SoundManager.powerup2);
				player.incrementBombCapacity();
				item.flagReset();
				return;				
			}
		}
		
		if(b.getFilterData().categoryBits == GlobalVar.BIT_PLAYER){
			player = (PlayerData)b.getBody().getUserData();
			item = (ItemData)a.getBody().getUserData();
			
			if(item.itemType() == GlobalVar.FIRE_POWERUP){
				SoundManager.playSound(SoundManager.powerup1);
				player.incrementFirePower();
				item.flagReset();
				return;
				
			}
				
			if(item.itemType() ==  GlobalVar.BOMB_POWERUP){
				SoundManager.playSound(SoundManager.powerup2);
				player.incrementBombCapacity();
				item.flagReset();
				return;
			}
		}
	}
	
	@Override
 	public void endContact(Contact c) {	
 		Fixture fa = c.getFixtureA(); 
		Fixture fb = c.getFixtureB();
		short aCategory = fa.getFilterData().categoryBits;
		short bCategory = fb.getFilterData().categoryBits;
		
		if(aCategory == GlobalVar.BIT_BOMB || bCategory == GlobalVar.BIT_BOMB){
			if(aCategory == GlobalVar.BIT_PLAYER || bCategory == GlobalVar.BIT_PLAYER){//TODO
			}
		}
			 	
		if(fa.getUserData() != null && fa.getUserData().equals("player")){//TODO
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("player")){//TODO
		}
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {		
	}
	

}
