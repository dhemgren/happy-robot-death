package com.mygdx.core;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.mygdx.collisions.ContactHandler;
import com.mygdx.collisions.FireRayCastHandler;
import com.mygdx.collisions.WorldQuery;
import com.mygdx.debug.Square;
import com.mygdx.gameObjects.Bomb;
import com.mygdx.gameObjects.BombPowerUp;
import com.mygdx.gameObjects.Box;
import com.mygdx.gameObjects.Fire;
import com.mygdx.gameObjects.FirePowerUp;
import com.mygdx.input.InputManager;
import com.mygdx.main.MainGame;
import com.mygdx.map.MapBodyBuilder;
import com.mygdx.map.MapRandomizer;
import com.mygdx.objectData.BombData;
import com.mygdx.player.Player;
import com.mygdx.player.PlayerCreator;
import com.mygdx.player.Player.State;
import com.mygdx.utils.ItemPlacer;
import com.mygdx.utils.Shake;

public class CoreGame implements Screen {
	SpriteBatch batch;
	Texture img;

	public MainGame game;

	// TODO: Migrate to server!
	public static World WORLD; // Physics world
	private static ContactHandler contactHandler = new ContactHandler();
	private static WorldQuery worldQuery;
	private static FireRayCastHandler fireRayCast = new FireRayCastHandler();

	public static Body FRICTION; // Body used to maintain friction between players and floor
	private static float timeStep = 1f / 60f; // Interval of physics simulation

	// Debug-tools
	private Box2DDebugRenderer b2dr;
	private ShapeRenderer sRend;
	static float sWidth = 0.2f, sHeight = 0.2f;
	static ArrayList<Square> squares = new ArrayList<Square>();

	// Screen size and resolution
	private Viewport viewport;
	private OrthographicCamera camera;
	private Vector2 cameraCenterPos;
	private static Shake shake = new Shake(); // Shaking camera effect
	//

	// The level
	private TiledMap tileMap;
	private BatchTiledMapRenderer batch_tiledMapRenderer;
	private static int boxLayerIndex = 5;
	private static int[] bottomLayers = { 0, 1, 5 }; // 0: Floor, 1: Pillars, 5: Boxes
	private static int[] topLayers = { 2 }; // 2: All sprites to render above everything else

	private TiledMapTileLayer boxLayer;
	private Array<Box> boxes;
	private Texture mapSprite;
	private TextureRegion boxSprite;

	private TiledMapTileLayer wallLayer;
	//

	private boolean resetGame;

	// Debug-variables
	private static FPSLogger fps = new FPSLogger();

	float stateTime;

	public CoreGame(MainGame game) {
		this.game = game;
		create();
	}

	/**
	 * Due to internal referencing, a certain order of creation must be
	 * retained: Camera -> World -> Players -> Map All other create-methods have
	 * non-critical placement
	 */

	public void create() {
		setupCamera();
		setupSpriteBatch();
		createWorld();
		createPlayers(2);
		initItemPools();
		setupMap("maps/level.tmx");
		setupDebugRenderers();
		setupSound();
		stateTime = 0.0f;
	}

	private void setupCamera() {
		camera = new OrthographicCamera(GlobalVar.VIRTUAL_WIDTH, GlobalVar.VIRTUAL_HEIGHT);
		viewport = new StretchViewport(GlobalVar.VIRTUAL_WIDTH, GlobalVar.VIRTUAL_HEIGHT, camera);
	}

	private void setupSpriteBatch() {
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined); // Define where to project image
	}

	private void setupDebugRenderers() {
		b2dr = new Box2DDebugRenderer();
		sRend = new ShapeRenderer();
		sRend.setProjectionMatrix(camera.combined);
	}

	/**
	 * Creates all players on current map. Each player is joined to the friction
	 * floor for accurate physics calculation
	 */

	private void createPlayers(int numberOfPlayers) {
		if (numberOfPlayers < 1)
			numberOfPlayers = 1;

		if (numberOfPlayers > 4)
			numberOfPlayers = 4;

		GameStateManager.allPlayers = PlayerCreator.createPlayers(numberOfPlayers, FRICTION, WORLD);
	}

	private void createWorld() {
		/*
		 * The input vector defines gravitational pull on the x- and y-axis of
		 * the world. 0, 0 = no gravity in either direction. The boolean value
		 * removes inactive bodies from physics calculation, be sure to leave as
		 * true
		 */
		WORLD = new World(new Vector2(0, 0), true);
		WORLD.setContactListener(contactHandler);
		worldQuery = new WorldQuery();

		// Create the world friction floor----***
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(GlobalVar.VIRTUAL_WIDTH / 2f, GlobalVar.VIRTUAL_HEIGHT / 2f);
		FRICTION = WORLD.createBody(bdef);
		FRICTION.setUserData("friction floor");

		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = GlobalVar.BIT_FRICTION;
		fdef.filter.maskBits = 0;
		fdef.density = 0.0f;
		fdef.restitution = 0.5f;
		fdef.friction = 0f;
		fdef.shape = shape;

		FRICTION.createFixture(fdef).setUserData("friction floor");

		// Center friction floor in camera view space
		FRICTION.setTransform(new Vector2(GlobalVar.VIRTUAL_WIDTH / 2f + 1.5f, GlobalVar.VIRTUAL_HEIGHT / 2f), 0);
		shape.dispose();
		// ----------------------------------**
	}

	/**
	 * Pools for fire, bombs and items. 
	 * All objects in the game world have
	 * a static upper count
	 */
	private void initItemPools() {

		// Establish item pools--------------***
		for (int b = 0; b < ItemPool.bombs.length; b++)
			ItemPool.bombs[b] = new Bomb((byte) 1, GlobalVar.BOMB_TIME, WORLD, ItemPool.bombPoolPosition);

		for (int f = 0; f < ItemPool.fires.length; f++)
			ItemPool.fires[f] = new Fire(WORLD, ItemPool.firePoolPosition);

		for (int b = 0; b < ItemPool.bombPows.length; b++)
			ItemPool.bombPows[b] = new BombPowerUp(WORLD, ItemPool.powPoolPosition);

		for (int f = 0; f < ItemPool.firePows.length; f++)
			ItemPool.firePows[f] = new FirePowerUp(WORLD, ItemPool.powPoolPosition);
		// -----------------------------------***

	}

	private void setupSound() {
		SoundManager.playSound(SoundManager.bg1);
	}

	/**
	 * Creates a new random map based on a specified level in .tmx format.
	 */

	private void setupMap(String map) {

		// Map loading and rendering*******************
		tileMap = new TmxMapLoader().load(Gdx.files.internal(map).path());
		
		batch_tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1 / 32f);
		
		TiledMapTileLayer layer0 = (TiledMapTileLayer) tileMap.getLayers().get(0);

		Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / (2 * 32f),
				layer0.getHeight() * layer0.getTileHeight() / (2 * 32f), 0);

		cameraCenterPos = new Vector2(center.x, center.y);
		
		MapBodyBuilder.buildShapes(tileMap, GlobalVar.PPM, WORLD, GlobalVar.BIT_WALL, "wall", false); 
		MapRandomizer mapRand = new MapRandomizer();
		boxLayer = mapRand.fillMap(WORLD, tileMap, GlobalVar.BOX_DENSITY); // Insert random boxes
		boxLayer.setVisible(false);
		tileMap.getLayers().add(boxLayer);
		boxes = mapRand.boxes;
		mapSprite = mapRand.mapSprite;
		boxSprite = mapRand.boxSprite;

		wallLayer = (TiledMapTileLayer) tileMap.getLayers().get("Pillars");
		// --------------------------*******************

		camera.position.set(center);
		camera.update();

	}

	public void update(float dt) {
		WORLD.step(timeStep, 1, 1); // Note that step is called with a fixed
									// timestep

		updateGameObjects(dt);

		if (GlobalVar.SCREEN_SHAKE)
			shake.update(dt, camera, cameraCenterPos);
	}

	@Override
	public void render(float dt) {

		if (!GameStateManager.inputBlocked) {
			handleInputs();
		}

		// Update statetime and physics
		stateTime += Gdx.graphics.getDeltaTime();
		update(Gdx.graphics.getDeltaTime());

		// Clear screen
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch_tiledMapRenderer.setView(camera);
		batch_tiledMapRenderer.render(bottomLayers);

		// Batch BEGIN, order of rendering decides displayed layering----***
		batch.begin();
		renderBombs();
		renderBoxes();
		renderItems();
		renderPlayers();
		renderFire();
		batch.end();
		// Batch END-----------------------------------------------------***

		batch_tiledMapRenderer.render(topLayers);

		if (GameStateManager.playerWon()) {
			GameStateManager.inputBlocked = true;
			GameStateManager.sleepTimer += dt;
			Texture winScreen;

			switch (GameStateManager.playerWinner) {
			case 1:
				winScreen = GameStateManager.winPlayer1;
				break;
			case 2:
				winScreen = GameStateManager.winPlayer2;
				break;
			case 3:
				winScreen = GameStateManager.winPlayer3;
				break;
			case 4:
				winScreen = GameStateManager.winPlayer4;
				break;
			default:
				winScreen = GameStateManager.winPlayer1;
			}

			batch.begin();
			batch.draw(winScreen, 6.5f, 3.5f, 192 / GlobalVar.PPM, 64 / GlobalVar.PPM);
			batch.end();

			if (GameStateManager.sleepTimer > GlobalVar.LEVEL_RESET_SLEEPTIME)
				boxes = GameStateManager.resetGame(tileMap, WORLD, boxes);
		}

		if (resetGame) {
			boxes = GameStateManager.resetGame(tileMap, WORLD, boxes);
			resetGame = false;
		}
	}

	/**
	 * Calls the update-method for all active game objects. This step is
	 * important in order to keep object data synced with gametime
	 * 
	 * @param dt
	 *            deltatime for updates as float
	 */
	private void updateGameObjects(float dt) {
		for (Player p : GameStateManager.allPlayers)
			if (!p.dead())
				p.update();

		for (Bomb b : ItemPool.bombs) {
			if (b.active)
				b.update(dt);

			if (b.detonate)
				detonateBomb(b);
		}

		for (Fire f : ItemPool.fires)
			if (f.active)
				f.update(dt);

		for (FirePowerUp f : ItemPool.firePows)
			f.update(dt);

		for (BombPowerUp b : ItemPool.bombPows)
			b.update(dt);

		for (Box b : boxes)
			b.update(dt);
	}

	private void renderSquares() {
		sRend.setProjectionMatrix(camera.combined);
		sRend.updateMatrices();
		camera.update();
		sRend.begin(ShapeType.Line);
		for (Square s : squares) {
			sRend.rect(s.x, s.y, sWidth, sHeight, s.color, s.color, s.color, s.color);
			s.update();
		}
		sRend.end();
	}

	private void renderBoxes() {
		for (Box b : boxes)
			if (b.isActive())
				batch.draw(boxSprite, b.body.getPosition().x, b.body.getPosition().y, 1, 1);
	}

	/**
	 * Renders all players that are not dead
	 */
	private void renderPlayers() {
		for (Player p : GameStateManager.allPlayers) {
			if (!p.dead()) {
				batch.draw(p.animation().getKeyFrame(stateTime, true), p.body.getPosition().x - 0.5f,
						p.body.getPosition().y - 0.3f, 1, 1);
			}
		}
	}

	/**
	 * Check for active bombs and render at their position
	 */

	private void renderBombs() {
		for (Bomb b : ItemPool.bombs) {
			if (b.active && !b.detonate) {
				batch.draw(b.animation(), b.body.getPosition().x, b.body.getPosition().y, 1, 1);
			}
		}

	}

	/**
	 * Draw all active fires according to their current state
	 */

	private void renderFire() {
		float x;
		float y;
		for (Fire f : ItemPool.fires) {
			if (f.active) {
				x = f.body.getPosition().x - 0.5f; // Align animation with body
				y = f.body.getPosition().y - 0.5f; // Align animation with body
				batch.draw(f.animation().getKeyFrame(f.animTimer), x, y, 1, 1);

			}
		}
	}

	private void renderItems() {
		float x;
		float y;

		for (FirePowerUp f : ItemPool.firePows) {
			if (f.active) {
				x = f.body.getPosition().x;
				y = f.body.getPosition().y;
				batch.draw(f.animation(), x, y, 1, 1);
			}
		}

		for (BombPowerUp b : ItemPool.bombPows) {
			if (b.active) {
				x = b.body.getPosition().x;
				y = b.body.getPosition().y;
				batch.draw(b.animation(), x, y, 1, 1);
			}
		}

	}

	/**
	 * If a bomb is flagged to detonate, render fire at bombs position in
	 * proportion to the bombs specified firepower
	 */

	private void detonateBomb(Bomb b) {

		int firePower = b.getFirePower();
		float x = b.detonatePosition.x;
		float y = b.detonatePosition.y;
		System.out.println("detonate: " + x + " " + y);
		float offset = 0.5f;
		float rayx = x + offset;
		float rayy = y + offset;

		ItemPlacer.setFire(x, y, WORLD);

		Fire fireL = null;
		Fire fireR = null;
		Fire fireU = null;
		Fire fireD = null;

		boolean obstacleHitLeft = false;
		boolean obstacleHitRight = false;
		boolean obstacleHitUp = false;
		boolean obstacleHitDown = false;
		FireRayCastHandler fRay = new FireRayCastHandler();

		/**
		 * For each tile in each direction, a short raycast is done to check if
		 * the next tile is empty or if it holds a box / wall. Note that player
		 * and item collision is handled dynamically in the ContactHandler.
		 */

		for (int f = 1; f <= firePower; f++) {

			if (!obstacleHitUp) {
				WORLD.rayCast(fRay, new Vector2(rayx, rayy + f - 1), new Vector2(rayx, rayy + f));
				obstacleHitUp = fRay.hasCollided && Math.abs(fRay.hitPoint.y - (rayy + f - 1)) < 1;

				if (!obstacleHitUp) {
					fireU = ItemPlacer.setFire(x, y + f, WORLD);
					fireU.state = f == firePower ? Fire.State.Up : Fire.State.Vertical;
				}
			}

			if (!obstacleHitDown) {
				fRay.resetCollisionCheck();
				WORLD.rayCast(fRay, new Vector2(rayx, rayy - f + 1), new Vector2(rayx, rayy - f));

				if (fRay.hitPoint != null)
					obstacleHitDown = fRay.hasCollided && Math.abs(fRay.hitPoint.y - (rayy - f + 1)) < 1;

				if (!obstacleHitDown) {
					fireD = ItemPlacer.setFire(x, y - f, WORLD);
					fireD.state = f == firePower ? Fire.State.Down : Fire.State.Vertical;
				}
			}

			if (!obstacleHitRight) {
				fRay.resetCollisionCheck();
				WORLD.rayCast(fRay, new Vector2(rayx + f - 1, rayy), new Vector2(rayx + f, rayy));

				if (fRay.hitPoint != null)
					obstacleHitRight = fRay.hasCollided && Math.abs(fRay.hitPoint.x - (rayx + f - 1)) < 1;

				if (!obstacleHitRight) {
					fireR = ItemPlacer.setFire(x + f, y, WORLD);
					fireR.state = f == firePower ? Fire.State.Right : Fire.State.Horizontal;
				}
			}

			if (!obstacleHitLeft) {
				fRay.resetCollisionCheck();
				WORLD.rayCast(fRay, new Vector2(rayx - f + 1, rayy), new Vector2(rayx - f, rayy));

				if (fRay.hitPoint != null)
					obstacleHitLeft = fRay.hasCollided && Math.abs(fRay.hitPoint.x - (rayx - f + 1)) < 1;

				if (!obstacleHitLeft) {
					fireL = ItemPlacer.setFire(x - f, y, WORLD);
					fireL.state = f == firePower ? Fire.State.Left : Fire.State.Horizontal;
				}
			}
		}

		shake.shake(GlobalVar.SHAKE_TIME * firePower); // Screen shakes
														// proportionately to
														// fire power
		SoundManager.playSound(SoundManager.explosion1);
		b.detonate = false;
		b.getData().unflagDetonation();
		b.getOwner().decrementActiveBombs();

	}

	// Useful for debugging world checks
	public static void drawSquare(float x, float y, Color color) {
		squares.add(new Square(x, y, color));
	}

	private void handleInputs() {

		if (GlobalVar.DEBUG_MODE)
			if (Gdx.input.isKeyJustPressed(Input.Keys.R))
				resetGame = true;

		if (Gdx.input.isKeyPressed(Input.Keys.Q))
			Gdx.app.exit();

		for (Player p : GameStateManager.allPlayers)
			InputManager.handleInput(p);

	}

	@Override
	public void show() {// TODO Auto-generated method stub
	}

	@Override
	public void resize(int width, int height) {// TODO Auto-generated method
												// stub
	}

	@Override
	public void pause() {// TODO Auto-generated method stub
	}

	@Override
	public void resume() {// TODO Auto-generated method stub
	}

	@Override
	public void hide() {// TODO Auto-generated method stub
	}

	@Override
	public void dispose() { // TODO Auto-generated method stub
	}

}
