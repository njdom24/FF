package com.dommie.ffdemo.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dommie.ffdemo.screens.MapScreen;
import com.dommie.ffdemo.sprites.Collision;
import com.dommie.ffdemo.sprites.NPC;

import java.util.ArrayList;

/**
 * Created by njdom24 on 5/8/2017.
 */

public class B2WorldCreator implements Disposable
{
    private World world;
    private TiledMap map;
    public B2WorldCreator(World world, TiledMap map, int[][] collisions)
    {
        this.world = world;
        this.map = map;

        //create collision bodies/fixtures
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //converts rectangles into 16x16 squares
            for(int i = 0; i < rect.getHeight(); i+=16)
                for(int j = 0; j < rect.getWidth(); j+=16){
                    Rectangle square = new Rectangle();
                    square.setSize(16);
                    square.setPosition(rect.getX()+j, rect.getY()+i);
                    collisions[collisions.length - 1 - (int)(square.getY()+1)/16][(int)(square.getX()+1)/16] = 1;
                    //new Collision(world, map, square);
                }

                /*
                for(int[] i : collisions)
				{
					for(int j : i)
					{
						System.out.print(j + ", ");
					}
					System.out.println();
				}

				System.out.println(collisions.length);
				System.out.println(collisions[0].length);
				*/

            //new Collision(world, map, rect);
        }

    }

    //TODO: Checks for direction (direction boolean array?)
    public ArrayList<NPC> createGenericNPCs(String[][] texts, String[] npcTypes, MapScreen m)
    {
        //create npc bodies/fixtures
        ArrayList<NPC> npcArray = new ArrayList<NPC>();

        int i = 0;
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            NPC newNPC = new NPC(world, map, rect, true, 2, npcTypes[i++], m);
            npcArray.add(newNPC);

            newNPC.setMessages(texts[i-1]);
        }
        return npcArray;
    }

	public void dispose()
	{
		world.dispose();
		map.dispose();
	}
}
