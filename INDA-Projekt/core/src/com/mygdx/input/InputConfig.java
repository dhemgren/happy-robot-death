package com.mygdx.input;

import com.badlogic.gdx.Input;

public class InputConfig {
	
	
	public static final int QUIT = Input.Keys.Q;
	public static final int RESET = Input.Keys.R;
	
	
	public static final int[][] inputs =	
		/**
		 * Player 1 - 4, 
		 * Up
		 * Down
		 * Left
		 * Right
		 * Bomb
		 */
		{	
			//Player1
			{
				Input.Keys.W,
				Input.Keys.S,
				Input.Keys.A,
				Input.Keys.D,
				Input.Keys.T
			},	
			//Player2
			{
				Input.Keys.UP,
				Input.Keys.DOWN,
				Input.Keys.LEFT,
				Input.Keys.RIGHT,
				Input.Keys.P
			},
			//Player3
			{
				1,
				2,
				3,
				4,
				5
			},
			//Player4
			{
				6,
				7,
				8,
				9,
				0
			}
			
		};
}
