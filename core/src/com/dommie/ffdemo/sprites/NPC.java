package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.tools.WorldContactListener;

import static com.dommie.ffdemo.tools.WorldContactListener.smallDifference;

/**
 * Created by njdom24 on 5/27/2017.
 */

public class NPC extends InteractiveTileObject
{
    public State currentState;
    public State previousState;
    private enum State{LEFT, RIGHT, UP, DOWN};
    private Animation<TextureRegion> runHorizontal;
    private Animation<TextureRegion> runUp;
    private Animation<TextureRegion> runDown;

    private float stateTimer;
    private float animSpeed;
    private Sprite spr;

    protected TextureAtlas atlas;

    private int index;
    private boolean isVertical;

    private boolean isMoving;
    private int distance;
    private Vector2 originalPos;
    private Vector2 intendedPos;

    private int distancePos;
    private int distanceNeg;

    private float time;

    public NPC(World world, TiledMap map, Rectangle bounds, int index, String name)
    {
        super(world, map, bounds, true);
        spr = new Sprite(GameInfo.currentScreen.getNPCAtlas().findRegion(name));

        fixture.setUserData(this);
        setCategoryFilter(GameInfo.COLLISION_BIT);

        intendedPos = body.getPosition();
        isVertical = false;
        this.index = index;
        isMoving = false;
    }

    public NPC(World world, TiledMap map, Rectangle bounds, int index, boolean vertical, int distance, String name)
    {
        super(world, map, bounds, true);

        spr = new Sprite(GameInfo.currentScreen.getNPCAtlas().findRegion(name));
        currentState = State.UP;
        stateTimer = 0;
        animSpeed = 0.125f;
        spr.setPosition(16,16);

        fixture.setUserData(this);
        setCategoryFilter(com.dommie.ffdemo.GameInfo.COLLISION_BIT);

        intendedPos = body.getPosition();
        isVertical = vertical;

        this.index = index;
        this.distance = distance;
        isMoving = false;

        distancePos = 0;
        distanceNeg = distance/2;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i <= 1; i++)
            frames.add(new TextureRegion(spr.getTexture(), i*16+spr.getRegionX(), spr.getRegionY(), 16, 16));
        runDown = new Animation<TextureRegion>(animSpeed, frames);
        frames.clear();

        frames = new Array<TextureRegion>();
        for(int i = 0; i <= 1; i++)
            frames.add(new TextureRegion(spr.getTexture(), i*16+spr.getRegionX(), spr.getRegionY()+16, 16, 16));
        runUp = new Animation<TextureRegion>(animSpeed, frames);
        frames.clear();

        frames = new Array<TextureRegion>();
        for(int i = 0; i <= 1; i++)
            frames.add(new TextureRegion(spr.getTexture(), i*16+spr.getRegionX(), spr.getRegionY()+32, 16, 16));
        runHorizontal = new Animation<TextureRegion>(animSpeed, frames);
        frames.clear();

        spr.setBounds(0, 0, 16, 16);
    }

    public void update(float dt)
    {
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
        spr.setPosition(body.getPosition().x - spr.getWidth()/2, body.getPosition().y - spr.getHeight()/2);
        spr.setRegion(getFrame(dt));
        body.setAwake(true);
    }

    public void draw(SpriteBatch sb)
    {
        spr.draw(sb);
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

    public TextureRegion getFrame(float dt)
    {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case LEFT:
                if(isMoving)
                    region = runHorizontal.getKeyFrame(stateTimer, true);
                else
                    region = runHorizontal.getKeyFrame(0);
                break;
            case RIGHT:
                if(isMoving)
                    region = runHorizontal.getKeyFrame(stateTimer, true);
                else
                    region = runHorizontal.getKeyFrame(0);
                break;
            case UP:
                if(isMoving)
                    region = runUp.getKeyFrame(stateTimer, true);
                else
                    region = runUp.getKeyFrame(0);
                break;
            case DOWN:
                if(isMoving)
                    region = runDown.getKeyFrame(stateTimer, true);
                else
                    region = runDown.getKeyFrame(0);
                break;
            default:
                region = runDown.getKeyFrame(stateTimer, true);
                break;
        }

        if (!region.isFlipX() && currentState == State.RIGHT)
        {
            region.flip(true, false);
            //runningLeft = false;
        }
        else if (region.isFlipX() && currentState == State.LEFT)
        {
            region.flip(true, false);
            //runningLeft = true;
        }


        if(currentState == previousState)
            stateTimer += dt;
        else
            stateTimer = 0;

        previousState = currentState;
        return region;

    }

    public State getState()
    {
        if(body.getLinearVelocity().y > 0)
            return State.UP;
        else if(body.getLinearVelocity().y < 0)
            return State.DOWN;
        else if(body.getLinearVelocity().x > 0)
            return State.RIGHT;
        else if(body.getLinearVelocity().x < 0)
            return State.LEFT;
        else
            return currentState;
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