package com.mygdx.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.core.CoreGame;
import com.mygdx.core.SoundManager;

public class MainGame extends Game{

	public MainGame game;
	 
	
	@Override
	public void create() {
		game = this;
		SoundManager.setupSounds();
		setScreen(new com.mygdx.menus.MainMenu(game));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}
	
	@Override
	public void pause(){
		super.pause();
	}
	
	@Override
	public void resume(){
		super.resume();
	}

}
