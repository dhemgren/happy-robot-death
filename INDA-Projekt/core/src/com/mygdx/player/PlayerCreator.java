package com.mygdx.player;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.mygdx.input.InputConfig;
import com.mygdx.utils.CoordinateConverter;

public class PlayerCreator {
	
	public static ArrayList<Player> createPlayers(int numberOfPlayers, Body frictionfloor, World world){
		
				ArrayList<Player> allPlayers = new ArrayList<Player>();
				
				if(numberOfPlayers < 1)
					numberOfPlayers = 1;
				
				if(numberOfPlayers > 4)
					numberOfPlayers = 4;
				
				// Join players to friction floor----***
				FrictionJointDef def = new FrictionJointDef();
				def.bodyA = frictionfloor;
		 		def.maxForce = 2f;// set something sensible;
				def.maxTorque = 2f;// set something sensible;
				FrictionJoint joint;
				// ----------------------------------***
				
				Vector2 spawnPos;
				
				for(int i = 0; i < numberOfPlayers; i++){
					
					//Set spawn position for players 1 - 4
					switch(i){
					case 0:
						spawnPos = CoordinateConverter.quantizePositionToGrid(new Vector2(3, 1));
						break;
					case 1:
						spawnPos = CoordinateConverter.quantizePositionToGrid(new Vector2(16, 8));
						break;
					case 2:
						spawnPos = CoordinateConverter.quantizePositionToGrid(new Vector2(16, 1));
						break;
					case 3:
						spawnPos = CoordinateConverter.quantizePositionToGrid(new Vector2(3, 8));
						break;
					default:
						spawnPos = new Vector2(3,2);
					}
					
					Player p = new Player(i + 1, spawnPos, world);
					p.setKeyUp(InputConfig.inputs[i][0]);
					p.setKeyDown(InputConfig.inputs[i][1]);
					p.setKeyLeft(InputConfig.inputs[i][2]);
					p.setKeyRight(InputConfig.inputs[i][3]);
					p.setKeyBomb(InputConfig.inputs[i][4]);
					
					allPlayers.add(p); //Create player and add to list
					def.bodyB = allPlayers.get(i).body;
					joint = (FrictionJoint) world.createJoint(def); //Join this player to friction floor
				}
				
				return allPlayers;		
			}
	
}
