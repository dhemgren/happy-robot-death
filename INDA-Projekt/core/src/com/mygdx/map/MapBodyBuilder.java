package com.mygdx.map;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.GlobalVar;
import com.mygdx.gameObjects.Box;

public class MapBodyBuilder {
	
	/**
	 * This creates rigid bodies from specified tiles
	 * in a tilemap. Note that only rectangle objects
	 * are currently supported.
	 */

    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static float ppt = 0;
    private static float posX;
    private static float posY;
    
    public static Array<Box> buildBoxShapesFromLayer(MapLayer mapLayer, float pixels, 
    	
    	World world, short collisionLayer, String description){
    	
    	Array<Box> boxes = new Array<Box>();
    	Map map = new Map();
    	map.getLayers().add(mapLayer);
    	map.getLayers().get(0).setName("Colliders");
    	Array<Body> bodies = buildShapes(map, pixels, world, collisionLayer, description, true);
    	
    	Box box;
    	
    	for(Body b : bodies){
    		box = new Box(b);
    		box.setActive(true);
    		boxes.add(box);
    	}
    	return boxes;    	 
    }

    public static Array<Body> buildShapes(Map map, float pixels, World world, 
    	short collisionLayer, String description, boolean isBoxLayer) {
    	
        ppt = pixels;
        
        MapObjects objects = map.getLayers().get("Colliders").getObjects();
        Array<Body> bodies = new Array<Body>();
       
        for(MapObject object : objects) {
        	
            if (object instanceof TextureMapObject) 
                continue;
            

            Shape shape;
         
            if (object instanceof RectangleMapObject) {
            	//System.out.println("Rectangle found");
                shape = getRectangle((RectangleMapObject)object);
            }
            else if (object instanceof PolygonMapObject) {
            	//System.out.println("Polygon found");
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject) {
            	//System.out.println("Polyline found");
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof CircleMapObject) {
            	//System.out.println("Circle found");
                shape = getCircle((CircleMapObject)object);
            }
            else {
                continue;
            }
            
        	posX = object.getProperties().get("x", Float.class) / ppt;
        	posY = object.getProperties().get("y", Float.class) / ppt;
            
            //Create body and define fixture for this tile
            BodyDef bdef = new BodyDef();
            bdef.type = BodyType.KinematicBody;
            bdef.allowSleep = false;      
          	bdef.position.set(posX, posY);
            
            FixtureDef fdef = new FixtureDef();
            fdef.filter.categoryBits = collisionLayer;
        	fdef.filter.maskBits = GlobalVar.BIT_PLAYER | GlobalVar.BIT_FIRE;
        	fdef.shape = shape;
            Body body = world.createBody(bdef);
            Fixture f = body.createFixture(fdef);
            f.setUserData(description);
            
            if(!isBoxLayer)
            	body.setUserData(description);
            
            bodies.add(body);

            shape.dispose();
        }
        
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.width * 0.5f) / ppt,
                                   (rectangle.height * 0.5f ) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                         rectangle.height * 0.5f / ppt,
                         size,
                         0.0f);

        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape(); 
        chain.createChain(worldVertices);
        return chain;
    }
}