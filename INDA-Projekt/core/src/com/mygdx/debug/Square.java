package com.mygdx.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple square object for 
 * easy debug rendering of
 * raycasts, positions etc
 * @author danhemgren
 *
 */

public class Square {

	public float timer;
	private static float animTime = 4f;
	public float x;
	public float y;
	public Color color;
	
	public Square(float x, float y, Color color){
		this.x = x;
		this.y = y;
		this.color = color;
		timer = animTime;
	}
	
	public void update(){
		timer -= Gdx.graphics.getDeltaTime();
	}
}
