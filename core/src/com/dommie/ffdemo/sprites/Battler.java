package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dommie.ffdemo.screens.GameScreen;

public class Battler extends Sprite
{
	public Body b2body;
	private int health;
	//String name;

	private boolean swinging;
	private float swingTimer;
	private float lastSwing;
	private Array<TextureRegion> animFrames;
	private Animation<TextureRegion> runHorizontal;
	private Animation<TextureRegion> attacking;
	private Animation<TextureRegion> swordSwing;
	private boolean wasMoving;
	private float stateTimer;
	private Sprite sword;

	public Battler(World world, String name, GameScreen screen)
	{
		super(screen.getAtlas().findRegion(name));
		//this.name = name;
		scale(2);
		BodyDef bdef = new BodyDef();
		bdef.position.set(384, 128);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		setBounds(0, 0, 26, 26);

		health = 10;

		sword = new Sprite(new TextureAtlas("Battle/Players/Sword.atlas").findRegion("Sword"));

		sword.scale(2);
		animFrames = new Array<TextureRegion>();
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 0; i <= 1; i++)
			frames.add(new TextureRegion(getTexture(), i*26+getRegionX(), getRegionY(), 26, 26));

		runHorizontal = new Animation<TextureRegion>(0.075f, frames);
		animFrames.addAll(frames);
		frames.clear();

		for(int i = 1; i <= 2; i++)
			frames.add(new TextureRegion(getTexture(), i*26+getRegionX(), getRegionY(), 26, 26));
		attacking = new Animation<TextureRegion>(0.075f, frames);
		animFrames.addAll(frames);
		frames.clear();

		for(int i = 0; i <= 1; i++)
			frames.add(new TextureRegion(sword.getTexture(), i*40+sword.getRegionX(), sword.getRegionY(), 40, 26));
		swordSwing = new Animation<TextureRegion>(0.075f, frames);
		animFrames.addAll(frames);
		frames.clear();

		stateTimer = 0;
		wasMoving = false;

		swinging = false;
		swingTimer = -1;
		lastSwing = -1;
	}

	public void update(float dt)
	{
		getFrame(dt);
		//sword.setPosition(b2body.getPosition().x - getWidth() + 2, b2body.getPosition().y + 26);
		sword.setPosition(b2body.getPosition().x - getWidth() - 6 - 16, b2body.getPosition().y + 40 - 1*16);
		setPosition(b2body.getPosition().x - getWidth() / 2 - 16, b2body.getPosition().y - getHeight()/2 + 16);
		//setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2 + 2*16);
	}

	public void takeDamage()
	{
		health--;
		System.out.println("Player health is: " + health);
	}

	public void animate(float timer, float dt)
	{
		swinging = false;
		if(swingTimer < 0)
		{
			swingTimer = 0.5f;
		}
		if (timer > 1)
			b2body.setLinearVelocity(-120, 0);
		else if (timer > 0.5)
		{
			swinging = true;
			b2body.setLinearVelocity(0,0);
		}
		else
			b2body.setLinearVelocity(120, 0);
	}

	public void getFrame(float dt)
	{
		//TextureRegion region;
		if(b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
		{
			wasMoving = true;
			setRegion(runHorizontal.getKeyFrame(stateTimer, true));
		}
		else
		{
			wasMoving = false;

			if(swinging)
			{
				setRegion(attacking.getKeyFrame(stateTimer, true));
				sword.setRegion(swordSwing.getKeyFrame(stateTimer, true));
				//same for sword
			}
			else
			{
				setRegion(runHorizontal.getKeyFrame(0));
				sword.setRegion(swordSwing.getKeyFrame(0));
			}
		}

		if(wasMoving || swinging)
			stateTimer += dt;
		else
			stateTimer = 0;

		//return region;
	}

	public void draw(SpriteBatch sb)
	{
		super.draw(sb);
		if(swinging)
			sword.draw(sb);
	}




}
