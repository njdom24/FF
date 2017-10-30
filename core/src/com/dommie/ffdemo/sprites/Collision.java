package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.tools.WorldContactListener;

/**
 * Created by njdom24 on 5/18/2017.
 */

public class Collision extends InteractiveTileObject
{
    protected Fixture fixture;

    public Collision(World world, TiledMap map, Rectangle bounds)
    {
        super(world, map, bounds, false);
        fixture.setUserData(this);
        setCategoryFilter(GameInfo.COLLISION_BIT);
    }

    @Override
    public void onDownCollision() {
       // Gdx.app.log("Down wall", "Collision");
        WorldContactListener.currentCollisions.add("down");
    }
    public void onUpCollision() {
        //Gdx.app.log("Up wall", "Collision");
        WorldContactListener.currentCollisions.add("up");
    }
    public void onLeftCollision() {
        //Gdx.app.log("Left wall", "Collision");
        WorldContactListener.currentCollisions.add("left");
    }
    public void onRightCollision() {
       // Gdx.app.log("Right wall", "Collision");
        WorldContactListener.currentCollisions.add("right");
    }

}
