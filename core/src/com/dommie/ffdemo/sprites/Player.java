package com.dommie.ffdemo.sprites;

//import static com.dommie.ffdemo.tools.WorldContactListener.currentCollisions; TODO: Uncomment later for NPC dialogue

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dommie.ffdemo.screens.MapScreen;

import static com.dommie.ffdemo.tools.WorldContactListener.npcs;
import static com.dommie.ffdemo.tools.WorldContactListener.smallDifference;

/**
 * Created by njdom24 on 5/5/2015.
 */

public class Player extends Sprite implements Disposable
{
	public enum State{LEFT, RIGHT, UP, DOWN}
	public State currentState;
	public State previousState;

	public Body b2body;
	private TextureRegion stand;

	private Array<TextureRegion> animFrames;
	private Animation<TextureRegion> runHorizontal;
	private Animation<TextureRegion> runUp;
	private Animation<TextureRegion> runDown;

	private float stateTimer;
	private float animSpeed;
	private boolean isMoving;
	public boolean movedThisFrame;
	private boolean isOverworld;

	private int[][] collisionArray;

	private int curX, curY;
	private Vector2 intendedPos;
	private Vector2 originalPos;

	public Player(World world, MapScreen screen, boolean overworld)
	{
		super(screen.getAtlas().findRegion("RedMage"));
		currentState = State.UP;
		stateTimer = 0;
		animSpeed = 0.15f;

		intendedPos = new Vector2(0,0);
		originalPos = new Vector2(0,0);
		animFrames = new Array<TextureRegion>();
		movedThisFrame = false;
		isOverworld = overworld;

		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 0; i <= 1; i++)
			frames.add(new TextureRegion(getTexture(), i*16+getRegionX(), getRegionY()-1, 16, 16));

		runHorizontal = new Animation<TextureRegion>(animSpeed, frames);
		animFrames.addAll(frames);
		frames.clear();

		frames = new Array<TextureRegion>();
		for(int i = 2; i <= 3; i++)
			frames.add(new TextureRegion(getTexture(), i*16+getRegionX(), getRegionY()-1, 16, 16));
		runUp = new Animation<TextureRegion>(animSpeed, frames);
		animFrames.addAll(frames);
		frames.clear();

		frames = new Array<TextureRegion>();
		for(int i = 4; i <= 5; i++)
			frames.add(new TextureRegion(getTexture(), i*16+getRegionX(), getRegionY()-1, 16, 16));
		runDown = new Animation<TextureRegion>(animSpeed, frames);
		animFrames.addAll(frames);
		frames.clear();

		definePlayer(world);

		setBounds(0, 0, 16, 16);
	}

	public void setCollisionArray(int[][] collisions)
	{
		collisionArray = collisions;
		//System.out.println("Done!");
	}

	public void update(float dt)
	{
		movedThisFrame = false;
		handleInput(dt);
		if(movedThisFrame)
			System.out.println("TEEEEEEEET");
		//puts sprite on b2body
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
		setRegion(getFrame(dt));
		b2body.setAwake(true);
	}

	public TextureRegion getFrame(float dt) {
		currentState = getState();
		TextureRegion region;
		switch (currentState) {
			case LEFT:
				if(isTryingToMove())
					region = runHorizontal.getKeyFrame(stateTimer, true);
				else
					region = runHorizontal.getKeyFrame(0);
				break;
			case RIGHT:
				if(isTryingToMove())
					region = runHorizontal.getKeyFrame(stateTimer, true);
				else
					region = runHorizontal.getKeyFrame(0);
				break;
			case UP:
				if(isTryingToMove())
					region = runUp.getKeyFrame(stateTimer, true);
				else
					region = runUp.getKeyFrame(0);
				break;
			case DOWN:
				if(isTryingToMove())
					region = runDown.getKeyFrame(stateTimer, true);
				else
					region = runDown.getKeyFrame(0);
				break;
			default:
				region = stand;
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
		if(b2body.getLinearVelocity().y > 0)
			return State.UP;
		else if(b2body.getLinearVelocity().y < 0)
			return State.DOWN;
		else if(b2body.getLinearVelocity().x > 0)
			return State.RIGHT;
		else if(b2body.getLinearVelocity().x < 0)
			return State.LEFT;
		else
			return currentState;
	}

	public void handleInput(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.N))
		{
			System.out.println(getPos());
		}
		//System.out.println("PLAYER POS: (" + curX + ", " + curY + ")");
		boolean canMove = false;
		if(npcs == null || npcs.isEmpty())
			canMove = true;
		//canMove = true;

		float playerX = b2body.getPosition().x;
		float playerY = b2body.getPosition().y;
		float npcX = 0;
		float npcY = 0;

		if(!isMoving)
		{
			previousState = currentState;
			originalPos = new Vector2(b2body.getPosition().x, b2body.getPosition().y);

			if(Gdx.input.isKeyPressed(Input.Keys.UP))
			{
				if(npcs != null)
					for(NPC n : npcs)
					{
						npcX = n.getIntendedPos().x;
						npcY = n.getIntendedPos().y;

						if(!smallDifference(npcX, playerX) || !smallDifference(playerY+16,  npcY))
							canMove = true;
						else
						{
							canMove = false;
							break;
						}
					}

				setState(Player.State.UP);
				if(canMove)
				{
					updateAnimationSpeed(0.15f);
					if (curY < collisionArray.length-1 && (collisionArray[collisionArray.length-curY-2][curX] == 0 || collisionArray[collisionArray.length-curY-2][curX] == 3))//no upward collisions, not allowed to move out of bounds
					{
						movedThisFrame = true;
						isMoving = true;
						curY += 1;

						if(!isOverworld)
						{
							collisionArray[collisionArray.length - (curY - 1) - 1][curX] = 0;
							collisionArray[collisionArray.length - curY - 1][curX] = 2;
						}
						//intendedPos = new Vector2(b2body.getPosition().x, b2body.getPosition().y + 16);
						b2body.setLinearVelocity(0, 64f);
						//currentCollisions.clear();

						//printColArray();
					}
					else
						updateAnimationSpeed(0.30f);
				}
				else
					updateAnimationSpeed(0.30f);
			}
			else
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				if(npcs != null)
					for(NPC n : npcs)
					{
						npcX = n.getIntendedPos().x;
						npcY = n.getIntendedPos().y;

						if(!smallDifference(npcX, playerX) || !smallDifference(playerY-16,  npcY))
							canMove = true;
						else
						{
							canMove = false;
							break;
						}
					}

				setState(Player.State.DOWN);
				if(canMove)
				{
					updateAnimationSpeed(0.15f);
					if (curY > 0 && (collisionArray[collisionArray.length-curY][curX] == 0 || collisionArray[collisionArray.length-curY][curX] == 3))//no downward collisions, not allowed to move out of bounds
					{
						movedThisFrame = true;
						isMoving = true;
						curY -= 1;
						if(!isOverworld)
						{
							collisionArray[collisionArray.length - (curY + 1) - 1][curX] = 0;
							collisionArray[collisionArray.length - curY - 1][curX] = 2;
						}
						//intendedPos = new Vector2(b2body.getPosition().x, b2body.getPosition().y - 16);
						b2body.setLinearVelocity(0, -64f);
						//currentCollisions.clear();//TODO: Use later for NPC interactions

						//printColArray();
					}
					else
						updateAnimationSpeed(0.30f);
				}
				else
					updateAnimationSpeed(0.30f);
			}
			else
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				if(npcs != null)
					for(NPC n : npcs)
					{
						npcX = n.getIntendedPos().x;
						npcY = n.getIntendedPos().y;

						if(!smallDifference(npcY, playerY) || !smallDifference(playerX-16,  npcX))
							canMove = true;
						else
						{
							canMove = false;
							break;
						}
					}

				setState(Player.State.LEFT);
				if(canMove)
				{
					updateAnimationSpeed(0.15f);
					if (curX > 0 && (collisionArray[collisionArray.length-curY-1][curX-1] == 0 || collisionArray[collisionArray.length-curY-1][curX-1] == 3))//no left collisions, not allowed to move out of bounds
					{
						movedThisFrame = true;
						isMoving = true;
						curX -= 1;
						if(!isOverworld)
						{
							collisionArray[collisionArray.length - curY - 1][curX + 1] = 0;
							collisionArray[collisionArray.length - curY - 1][curX] = 2;
						}
						//intendedPos = new Vector2(b2body.getPosition().x - 16, b2body.getPosition().y);
						b2body.setLinearVelocity(-64f, 0);
						//currentCollisions.clear();

						//printColArray();
					}
					else
						updateAnimationSpeed(0.30f);
				}
				else
					updateAnimationSpeed(0.30f);
			}
			else
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				if(npcs != null)
					for(NPC n : npcs)
					{
						npcX = n.getIntendedPos().x;
						npcY = n.getIntendedPos().y;

						if(!smallDifference(npcY, playerY) || !smallDifference(playerX+16,  npcX))
							canMove = true;
						else
						{
							canMove = false;
							break;
						}
					}
				setState(Player.State.RIGHT);
				if(canMove)
				{
					updateAnimationSpeed(0.15f);
					if (curX < collisionArray[0].length-1 && (collisionArray[collisionArray.length-curY-1][curX+1] == 0 || collisionArray[collisionArray.length-curY-1][curX+1] == 3))//no right collisions, not allowed to move out of bounds
					{
						movedThisFrame = true;
						isMoving = true;
						curX += 1;
						if(!isOverworld)
						{
							collisionArray[collisionArray.length - curY - 1][curX - 1] = 0;
							collisionArray[collisionArray.length - curY - 1][curX] = 2;
						}
						//intendedPos = new Vector2(b2body.getPosition().x + 16, b2body.getPosition().y);
						b2body.setLinearVelocity(64f, 0);
						//currentCollisions.clear();

						//printColArray();
					} else
						updateAnimationSpeed(0.30f);
				}
				else
					updateAnimationSpeed(0.30f);
			}
		}
		else
		{
			//15.8 is used instead of 16 because it will go over, which makes snapping back into position look bad
			if(Math.abs(b2body.getPosition().x - originalPos.x) >= 15.9f || Math.abs(b2body.getPosition().y - originalPos.y) >= 15.9f) {
				isMoving = false;
				Vector2 lastSpeed = new Vector2(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y);
				b2body.setLinearVelocity(0,0);
				b2body.setTransform(originalPos.x + lastSpeed.x/4, originalPos.y+lastSpeed.y/4, 0);
			}
		}
	}

	public boolean isTryingToMove()
	{
		if((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
			return true;
		else
			return (b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0);
	}

	//where the body will end up at the end of its movement
	public Vector2 getIntendedPos()
	{
		intendedPos = new Vector2(curX*16 + 8, curY*16 + 8);
		//System.out.println("Player: (" + curX*16 + ", " + curY*16 + ")");
		return intendedPos;
	}


	public void definePlayer(World world)
	{
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
	}

	public void updateAnimationSpeed(float speed)
	{
		animSpeed = speed;

		runUp.setFrameDuration(animSpeed);
		runDown.setFrameDuration(animSpeed);
		runHorizontal.setFrameDuration(animSpeed);
	}

	public void setState(State s)
	{
		previousState = currentState;
		currentState = s;
	}

	public void setStartingIndex(int x, int y)
	{
		curX = x;
		curY = y;
	}

	public void dispose()
	{
		//stand.getTexture().dispose();

		getTexture().dispose();
		/*
		for(TextureRegion t : animFrames)
			t.getTexture().dispose();
		*/
	}

	public boolean isMoving()
	{
		return isMoving;
	}

	public int getPos()
	{
		return collisionArray[collisionArray.length-curY-1][curX];
	}

	private void printColArray()
	{
		System.out.println("\n\n\n");
		for(int[] i : collisionArray)
		{
			for(int j : i)
			{
				System.out.print(j + ", ");
			}
			System.out.println();
		}
	}
}
