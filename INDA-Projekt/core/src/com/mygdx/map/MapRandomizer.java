package com.mygdx.map;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.GlobalVar;
import com.mygdx.gameObjects.Box;

public class MapRandomizer {
	
	public Array<Box> boxes;
	public Texture mapSprite;
	public TextureRegion boxSprite;
	
	/**
	 * Constructs a layer of randomly placed boxes
	 * on the specified map and creates corresponding bodies 
	 * (/w fixtures) in the defined world.
	 * @param world The physical world space for colliders
	 * @param map The map to juxtapose layer onto
	 * @param density How many boxes to place (upper bound)
	 * @return a TiledMapTileLayer of randomly placed boxes
	 */
	
	public TiledMapTileLayer fillMap(World world, TiledMap map, int density){
		
		Random random = new Random();
		int boxesToPlace = density;	
				
		//Get the properties of map---------------***
		MapProperties prop = map.getProperties();
		int mapWidth = prop.get("width", Integer.class);		 // *** These are in tiles, not pixels
		int mapHeight = prop.get("height", Integer.class); 		 // ***
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);
	
		TiledMapTileLayer boxReferenceLayer = (TiledMapTileLayer) map.getLayers().get("Boxes");
		Cell box = boxReferenceLayer.getCell(0, 0);
		mapSprite = box.getTile().getTextureRegion().getTexture();
		boxSprite = box.getTile().getTextureRegion();
	
		
		//Grab the needed tile layers from the map***
		TiledMapTileLayer floor = (TiledMapTileLayer)map.getLayers().get("Floor"); 
		TiledMapTileLayer pillars = (TiledMapTileLayer)map.getLayers().get("Pillars");
		//----------------------------------------***
		
		
		//Set blocked tiles-----------------------------***
		boolean[][] tileBlocked = new boolean[mapWidth][mapHeight];
	
		tileBlocked[2][6] = true;  //Bottom left corner
		tileBlocked[2][7] = true;  //
		tileBlocked[3][7] = true;  //
		
		tileBlocked[2][1] = true;  //Top left corner
		tileBlocked[2][2] = true;  //
		tileBlocked[3][1] = true;  //
		
		tileBlocked[16][6] = true; //Bottom right corner
		tileBlocked[16][7] = true; //
		tileBlocked[15][7] = true; //
		
		tileBlocked[16][1] = true; //Top right corner
		tileBlocked[16][2] = true; //
		tileBlocked[15][1] = true; //
		//----------------------------------------------***
		
		//Set required tiles----------------------------***
		boolean[][] placeTile = new boolean[mapWidth][mapHeight];
		
		placeTile[2][4] = true;  //Keep left players separated
		
		placeTile[16][4] = true; //Keep right players separated
		
		//TODO: Implement barriers between horizontally adjacent players
		
		//----------------------------------------------***
		
		
		//Creates two layers of the same tile-dimension and area as map, one for tiles and one for colliders
		TiledMapTileLayer boxLayer = new TiledMapTileLayer(mapWidth, mapHeight, tilePixelWidth, tilePixelHeight);
		MapLayer boxColliders = new MapLayer();
		
		
		//Iterate through each tile on tilemap and construct the random boxes
		for(int w = 2; w < mapWidth - 2; w++){
			for(int h = 1; h < mapHeight - 1; h++){
	
				//Make sure that box is placeable at cell
				if(tileBlocked[w][h] || floor.getCell(w, h) == null || pillars.getCell(w,h) != null){
					continue;
				}
				
				//If placeTile is true, this tile is a required tile and will spawn
				if(placeTile[w][h] || random.nextInt(32) > 10 && boxesToPlace > 0){
					float x = w*32f;
					float y = h*32f;
					MapObject m = new RectangleMapObject(x, y, 32f, 32f);
					boxColliders.getObjects().add(m);
					m.getProperties().put("x", x); //Store coordinates in collider
					m.getProperties().put("y", y); 
					
					boxLayer.setCell(w, h, box); //Sets the box at this position
					boxesToPlace--;
				}
				
			}
		}
		
		boxes = MapBodyBuilder.buildBoxShapesFromLayer(boxColliders, 32f, world, GlobalVar.BIT_BOX, "box");
		return boxLayer;
	}
}
