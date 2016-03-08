package com.mygdx.debug;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.ItemPool;
import com.mygdx.gameObjects.Bomb;
import com.mygdx.gameObjects.BombPowerUp;
import com.mygdx.gameObjects.Fire;
import com.mygdx.gameObjects.FirePowerUp;

/**
 * Various debug methods
 * @author danhemgren
 *
 */

public class Debug {
	
	public static void printAllContacts(World w){
		Array<Contact> contacts = w.getContactList();
		
		for(Contact c : contacts){
			Fixture fa = c.getFixtureA();
			Fixture fb = c.getFixtureB();
			String a = (String)fa.getUserData();
			String b = (String)fb.getUserData();
			Vector2 apos = fa.getBody().getPosition();
			Vector2 bpos = fb.getBody().getPosition();

			if(a == null || b == null)
				continue;
			
			System.out.println(a + " at position " + apos + " collides with " + b + " at position " + bpos);
		}
	}
	
	public static void printAllBodies(World w){
		Array<Body> b = new Array<Body>();
		w.getBodies(b);
		
		System.out.println("Bodies in world: " + w.getBodyCount());
		for(Body body : b)
			System.out.println(body.getPosition());
	}
	
	public static void printMousePos(){
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		System.out.println("Current mouse pos. x: " + Gdx.input.getX() + " y: " + Gdx.input.getY());
	}
	
	public static void printActiveItemCount(){
		int fires = 0;
		int bombs = 0;
		int fpows = 0;
		int bpows = 0;
		
		for(Bomb b : ItemPool.bombs)
			if(b.active)
				bombs++;
	
		for(Fire f : ItemPool.fires)
			if(f.active)
				fires++;
		
		for(FirePowerUp fp : ItemPool.firePows)
			if(fp.active)
				fpows++;
		
		for(BombPowerUp bp : ItemPool.bombPows)
			if(bp.active)
				bpows++;
		
			System.out.println("Active item counts:");
			System.out.println("Fires: " + fires);
			System.out.println("Bombs: " + bombs);
			System.out.println("Fire Powerups: " + fpows);
			System.out.println("Bomb Powerups: " + bpows);
	}
	
}
