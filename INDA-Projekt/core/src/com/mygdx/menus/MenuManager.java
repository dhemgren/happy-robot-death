package com.mygdx.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.core.GlobalVar;

public class MenuManager {
	
	public static float framerate = 1/4f;
	public static TextureAtlas mainMenuSheet = new TextureAtlas(Gdx.files.internal("sprites/texts/menuitems.txt"));
	public static TextureAtlas lobbySheet = new TextureAtlas(Gdx.files.internal("sprites/texts/lobbyitems.txt"));
	public static TextureAtlas joinSheet = new TextureAtlas(Gdx.files.internal("sprites/texts/joinitems.txt"));
	public static TextureAtlas msgSheet = new TextureAtlas(Gdx.files.internal("sprites/texts/msgitems.txt"));

	
	public static Animation hostIdle = new Animation(framerate, mainMenuSheet.findRegions("hostidle"));
	public static Animation hostActive = new Animation(framerate, mainMenuSheet.findRegions("hostactive"));
	
	public static Animation joinIdle = new Animation(framerate, mainMenuSheet.findRegions("joinidle"));
	public static Animation joinActive = new Animation(framerate, mainMenuSheet.findRegions("joinactive"));

	public static Animation quitIdle = new Animation(framerate, mainMenuSheet.findRegions("quitidle"));
	public static Animation quitActive = new Animation(framerate, mainMenuSheet.findRegions("quitactive"));
	
	public static Animation distortLogo = new Animation(1/60f, mainMenuSheet.findRegions("mainlogobwdist"));

	public static TextureRegion logo = mainMenuSheet.findRegion("mainlogobw");
	public static TextureRegion logoLarge = mainMenuSheet.findRegion("mainlogobwlarge");
	
	public static TextureRegion lobbyBanner = lobbySheet.findRegion("lobbybanner");
	public static Animation zuluIdle = new Animation(framerate, lobbySheet.findRegions("zuluidle"));
	public static Animation zuluActive = new Animation(framerate, lobbySheet.findRegions("zuluactive"));
	public static Animation backIdle = new Animation(framerate, lobbySheet.findRegions("backidle"));
	public static Animation backActive = new Animation(framerate, lobbySheet.findRegions("backactive"));

	
	public static TextureRegion makeConnect = joinSheet.findRegion("connect");
	public static TextureRegion connectError = joinSheet.findRegion("connecterror");
	public static TextureRegion gameFull = joinSheet.findRegion("gamefull");
	public static TextureRegion pressToReturn = joinSheet.findRegion("presstoreturn");
	public static Animation makeIdle = new Animation(framerate, joinSheet.findRegions("makeidle"));
	public static Animation makeActive = new Animation(framerate, joinSheet.findRegions("makeactive"));
	
	public static TextureRegion allPlayersLeft = msgSheet.findRegion("allplayersleft");
	public static TextureRegion hostLeft = msgSheet.findRegion("hostleft");
	
	public static TextureRegion leaveGame = msgSheet.findRegion("leavegame");
	public static TextureRegion leaveGameHost = msgSheet.findRegion("leavegamehost");
	

	/**
	 * Returns the specified player status texture 
	 * for use in the lobby menu
	 */
	public static TextureRegion getPlayerStatusTexture(int player, int status){
		if(player < 1 || player > 4)
			player = 1;
		
		String filePath = "p" + player;
		
		switch(status){
			case GlobalVar.PLAYER_EMPTY:
				filePath += "empty";
				break;
			case GlobalVar.PLAYER_CONNECTED:
				filePath += "connected";
				break;
			case GlobalVar.PLAYER_READY:
				filePath += "ready";
				break;
			default:
				filePath += "empty";
		}
		return new TextureRegion(lobbySheet.findRegion(filePath));		
	}	
}
