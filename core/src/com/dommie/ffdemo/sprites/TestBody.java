package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.tools.WorldContactListener;

/**
 * Created by njdom24 on 5/29/2017.
 */

public class TestBody
{
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;

    public TestBody(World world, TiledMap map)
    {
        this.world = world;
        this.map = map;
        this.bounds = new Rectangle(7,7,16,16);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set((bounds.getX() + bounds.getWidth()/2), (bounds.getY() + bounds.getHeight()/2) );

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2, bounds.getHeight()/2 );
        fdef.shape = shape;
        fdef.isSensor = true;
        fixture = body.createFixture(fdef);
        shape.dispose();
    }

    public void update(float dt)
    {
        body.setTransform(WorldContactListener.player.getIntendedPos(), 0);
    }
}
