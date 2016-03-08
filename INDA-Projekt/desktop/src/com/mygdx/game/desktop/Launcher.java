package com.mygdx.game.desktop;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.core.CoreGame;
import com.mygdx.main.*;


public class Launcher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.vSyncEnabled = true;
		
		new LwjglApplication(new MainGame(), config);	
	}
}
