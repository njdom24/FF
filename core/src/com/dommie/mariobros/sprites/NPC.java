package com.dommie.mariobros.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.mariobros.MarioBros;
import com.dommie.mariobros.tools.WorldContactListener;

import static com.dommie.mariobros.tools.WorldContactListener.smallDifference;

/**
 * Created by njdom24 on 5/27/2017.
 */

public class NPC extends InteractiveTileObject
{
    private int index;
    private boolean isVertical;

    private boolean isMoving;
    private int distance;
    private Vector2 originalPos;
    private Vector2 intendedPos;

    private int distancePos;
    private int distanceNeg;

    private float time;

    public NPC(World world, TiledMap map, Rectangle bounds, int index)
    {
        super(world, map, bounds, true);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COLLISION_BIT);

        intendedPos = body.getPosition();
        isVertical = false;
        this.index = index;
        isMoving = false;
    }

    public NPC(World world, TiledMap map, Rectangle bounds, int index, boolean vertical, int distance)
    {
        super(world, map, bounds, true);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COLLISION_BIT);

        intendedPos = body.getPosition();
        isVertical = vertical;

        this.index = index;
        this.distance = distance;
        isMoving = false;

        distancePos = 0;
        distanceNeg = distance/2;
    }

    public void update(float dt)
    {
        body.setAwake(true);
        if(!isMoving)
        {
            originalPos = new Vector2(body.getPosition().x, body.getPosition().y);
            time += dt;
            if (time >= 1)
            {
                time = 0;

                handleMovement(dt);
            }
        }
        else
        {
            //15.9 is used instead of 16 because it will go over, which makes snapping back into position look bad
            if(Math.abs(body.getPosition().x - originalPos.x) >= 15.9f || Math.abs(body.getPosition().y - originalPos.y) >= 15.9f)
            {
                isMoving = false;
                Vector2 lastSpeed = new Vector2(body.getLinearVelocity().x, body.getLinearVelocity().y);
                body.setLinearVelocity(0,0);
                body.setTransform(originalPos.x + lastSpeed.x/4, originalPos.y+lastSpeed.y/4, 0);
            }
        }
    }

    public void handleMovement(float dt)
    {
        float npcX = body.getPosition().x;
        float npcY = body.getPosition().y;
        float playerX = WorldContactListener.player.getIntendedPos().x;
        float playerY = WorldContactListener.player.getIntendedPos().y;

        if(isVertical)
        {
            //player is either not in the same x position or player is not 16 y pixels below
            if(distanceNeg > 0 && (!smallDifference(npcX, playerX) || !smallDifference(npcY -16,  playerY)))
            {
                distanceNeg--;
                if(distanceNeg == 0)
                    distancePos = distance/2 + 1;

                moveDown();
            }
            else
                //player is either not in the same x position or player is not 16 y pixels above
                if(distancePos > 0  && (!smallDifference(npcX, playerX) || !smallDifference(npcY +16,  playerY)))
                {
                    distancePos--;
                    if(distancePos == 0)
                        distanceNeg = distance/2 + 1;

                    moveUp();
                }
        }
        else
            //player is either not in the same y position or player is not 16 x pixels to the left
            if(distanceNeg > 0 && (!smallDifference(npcY, playerY) || !smallDifference(npcX -16,  playerX)))
            {
                distanceNeg--;
                if(distanceNeg == 0)
                    distancePos = distance/2 + 1;

                moveLeft();
            }
            else
                //player is either not in the same y position or player is not 16 x pixels to the right
                if(distancePos > 0  && (!smallDifference(npcY, playerY) || !smallDifference(npcX +16,  playerX)))
                {
                    distancePos--;
                    if(distancePos == 0)
                        distanceNeg = distance/2 + 1;

                    moveRight();
                }
    }

    private void moveLeft()
    {
        isMoving = true;
        intendedPos = new Vector2(body.getPosition().x - 16, body.getPosition().y);
        body.setLinearVelocity(-64f, 0);
    }

    private void moveRight()
    {
        isMoving = true;
        intendedPos = new Vector2(body.getPosition().x + 16, body.getPosition().y);
        body.setLinearVelocity(64f, 0);
    }

    private void moveUp()
    {
        isMoving = true;
        intendedPos = new Vector2(body.getPosition().x, body.getPosition().y + 16);
        body.setLinearVelocity(0, 64f);
    }

    private void moveDown()
    {
        isMoving = true;
        intendedPos = new Vector2(body.getPosition().x, body.getPosition().y - 16);
        body.setLinearVelocity(0, -64f);
    }

    public void setCategoryFilter(short filterBit)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    //where the player will end up at the end of their movement
    public Vector2 getIntendedPos()
    {
        return intendedPos;
    }

    @Override
    public void onDownCollision() {
    }
    public void onUpCollision() {
    }
    public void onLeftCollision() {
    }
    public void onRightCollision() {
    }
}