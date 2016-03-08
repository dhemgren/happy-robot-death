package com.mygdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	
	//==== GAME SOUNDS ====
	public static Sound walk1;
	public static Sound walk2;
	public static Sound walk3;
	public static Sound walk4;
	public static Sound walk5;
	public static Sound powerup1;
	public static Sound powerup2;
	public static Sound dropbomb;
	public static Sound logoDistort;
	public static Sound explosion1;
	
	
	// ==== MUSIC ====
	public static Music menubg;
	public static Music bg1;
	public static Music win;


	
	// ==== MENU SOUNDS ====
	public static Sound select1;

	
	public static void setupSounds(){
		walk1 = Gdx.audio.newSound(Gdx.files.internal("sounds/walk1.wav"));
		walk2 = Gdx.audio.newSound(Gdx.files.internal("sounds/walk2.wav"));
		walk3 = Gdx.audio.newSound(Gdx.files.internal("sounds/walk3.wav"));
		walk4 = Gdx.audio.newSound(Gdx.files.internal("sounds/walk4.wav"));
		walk5 = Gdx.audio.newSound(Gdx.files.internal("sounds/walk5.wav"));
		powerup1 = Gdx.audio.newSound(Gdx.files.internal("sounds/powerup1.wav"));
		powerup2 = Gdx.audio.newSound(Gdx.files.internal("sounds/powerup2.wav"));
		dropbomb = Gdx.audio.newSound(Gdx.files.internal("sounds/dropbomb.wav"));
		logoDistort = Gdx.audio.newSound(Gdx.files.internal("sounds/logodistort.wav"));	
		explosion1 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.wav"));
		menubg = Gdx.audio.newMusic(Gdx.files.internal("sounds/songs/menu.mp3"));
		bg1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/songs/1.mp3"));
		win = Gdx.audio.newMusic(Gdx.files.internal("sounds/songs/win.mp3"));
		select1 = Gdx.audio.newSound(Gdx.files.internal("sounds/select1.wav"));
	}
	
	public static void stopAllBG(){
		menubg.stop();
		bg1.stop();
		win.stop();
	}
	
	/**
	 * Returns if the music already playing,
	 * otherwise plays the song from the beginning
	 * @param song of type Music
	 */
	public static void playSound(Music music){
		if(music.isPlaying())
			return;
		
		stopAllBG();
		music.setLooping(true);
		music.setPosition(0f);
		music.play();
	}
	
	/**
	 * Stops the sound(if playing), resets it
	 * and replays from the beginning
	 * @param sound of type Sound
	 */
	public static void playSound(Sound sound){
		sound.stop();
		sound.play();
	}
	
}
