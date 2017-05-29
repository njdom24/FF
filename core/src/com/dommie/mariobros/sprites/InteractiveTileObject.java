package com.dommie.mariobros.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.mariobros.MarioBros;

/**
 * Created by njdom24 on 5/8/2017.
 */

public abstract class InteractiveTileObject
{
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;


    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, boolean isDynamic)
    {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        if(isDynamic)
            bdef.type = BodyDef.BodyType.DynamicBody;
        else
            bdef.type = BodyDef.BodyType.StaticBody;

        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight()/2) / MarioBros.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2 / MarioBros.PPM, bounds.getHeight()/2 / MarioBros.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        fixture = body.createFixture(fdef);
    }

    public abstract void onDownCollision();
    public abstract void onUpCollision();
    public abstract void onLeftCollision();
    public abstract void onRightCollision();

    public void setCategoryFilter(short filterBit)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }


    /*
    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds)
    {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight()/2) / MarioBros.PPM);

        body = world.createBody(bdef);

        Vector2[] v2 = {new Vector2(-8/MarioBros.PPM, (float)7.5/MarioBros.PPM), new Vector2((float)-7.5/MarioBros.PPM, 8/MarioBros.PPM),new Vector2((float)7.5/MarioBros.PPM, 8/MarioBros.PPM),new Vector2(8/MarioBros.PPM, (float)7.5/MarioBros.PPM)
                ,new Vector2(8/MarioBros.PPM, (float)-7.5/MarioBros.PPM), new Vector2((float)7.5/MarioBros.PPM, -8/MarioBros.PPM),new Vector2((float)-7.5/MarioBros.PPM, -8/MarioBros.PPM),new Vector2(-8/MarioBros.PPM, (float)-7.5/MarioBros.PPM)};

        shape.set(v2);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
    */

}
