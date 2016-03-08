package com.mygdx.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.core.CoreGame;
import com.mygdx.core.GlobalVar;
import com.mygdx.core.SoundManager;
import com.mygdx.main.MainGame;



public class MainMenu implements Screen {

	private MainGame game;
	private Stage stage;
	
	private OrthographicCamera camera;
	private static float cameraZoom = 0f;
	
	private SpriteBatch batch;
	private Texture topLogo;
		
	private Vector2 logoPos, hostPos, quitPos, logoScaled, buttonScaled;
	private static float logoScale = 1.5f;
	
	private static final float fxTime = 5f;
	private float fxTimer;
	private boolean distortLogo;
	
	private static final float distortTime = 0.05f;
	private float distortTimer;
	
	private float stateTime;
	
	private int[] selected;
	
	
	public MainMenu(final MainGame game) {
		this.game = game;
		stateTime = 0f;
		fxTimer = 0f;
		distortTimer = 0f;
		
		camera = new OrthographicCamera(GlobalVar.VIRTUAL_WIDTH, GlobalVar.VIRTUAL_HEIGHT);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom += cameraZoom;
		camera.update();
		
		logoPos = new Vector2(Gdx.graphics.getWidth()/2 - 280, Gdx.graphics.getHeight()/2 - 48);
		hostPos = new Vector2(Gdx.graphics.getWidth()/2 - 64, Gdx.graphics.getHeight()/2 - 48);
		quitPos = new Vector2(Gdx.graphics.getWidth()/2 - 64, Gdx.graphics.getHeight()/2 - 76);
		logoScaled = new Vector2(MenuManager.logoLarge.getRegionWidth() * logoScale, MenuManager.logoLarge.getRegionHeight() * logoScale);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
				
		//Set up texture
		topLogo = new Texture(Gdx.files.internal("sprites/texts/mainlogobw.png"));
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		
		// Keep track of which is selected.
		selected = new int[2];  
		
		//Initialize
		for(int i = 0; i<selected.length; i++) {
			selected[i] = 0;
		}
		selected[0] = 1;
		
		SoundManager.playSound(SoundManager.menubg);
	}
	

	@Override
	public void render(float delta) {
		float graphicsDelta = Gdx.graphics.getDeltaTime();

		stateTime += graphicsDelta;
		
		if(!distortLogo)
			fxTimer += graphicsDelta;
		else
			distortTimer += graphicsDelta;

		
		if(fxTimer > fxTime){
			SoundManager.logoDistort.play(1f);
			distortLogo = true;
			fxTimer = 0f;
		}

			
		if(distortTimer > distortTime && distortLogo){
			distortLogo = false;
			distortTimer = 0f;
		}
		
		float mod = 5f * (float)Math.sin(stateTime);
		//camera.zoom += cameraMod;
		//camera.rotate(cameraMod);
		camera.update();
        batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
		batch.begin();
		
		if(!distortLogo)
			batch.draw(MenuManager.logoLarge, logoPos.x, logoPos.y, logoScaled.x + mod, logoScaled.y + mod);
		else
			batch.draw(MenuManager.distortLogo.getKeyFrame(stateTime, true), logoPos.x, logoPos.y, logoScaled.x, logoScaled.y);

		if(selected[0] == 1)
			batch.draw(MenuManager.hostActive.getKeyFrame(stateTime, true), hostPos.x, hostPos.y);
		else
			batch.draw(MenuManager.hostIdle.getKeyFrame(stateTime, true), hostPos.x, hostPos.y);
		
		if(selected[1] == 1)
			batch.draw(MenuManager.quitActive.getKeyFrame(stateTime, true), quitPos.x, quitPos.y);
		else
			batch.draw(MenuManager.quitIdle.getKeyFrame(stateTime, true), quitPos.x, quitPos.y);
		
		batch.end();
		
		handleInputs();
	}
	
	private void moveSelectionUp() {
		for(int i = 0; i < selected.length; i++) {
			if(selected[i] == 1 && i == 0)
				break;
			else if(selected[i] == 1) {
				selected[i-1] = 1;
				selected[i] = 0;
				break;
			}
		}
	}
	
	private void moveSelectionDown() {
		for(int i = 0; i < selected.length; i++) {
			if(selected[i] == 1 && i == 1)
				break;
			else if(selected[i] == 1) {
				selected[i+1] = 1;
				selected[i] = 0;
				break;
			}
		}
	}
	
	private void handleAction() {
		for(int i = 0; i < selected.length; i++) {
			if(selected[i] == 0)
				continue;
			if(selected[i] == 1) {
				switch(i) {
				case 0: 
						game.setScreen(new CoreGame(game));
						break;
				case 1: Gdx.app.exit();
						break;	
				}
			}
		}
	}
	

	private void handleInputs(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
			moveSelectionUp();
			SoundManager.playSound(SoundManager.walk1);
			return;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
			moveSelectionDown();
			SoundManager.playSound(SoundManager.walk1);
			return;
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				handleAction();
				return;
		}
	
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
		
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
	}
	


	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() { }
}
