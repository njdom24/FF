package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dommie.ffdemo.screens.GameScreen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Battler extends Sprite
{
	public Body b2body;
	public int health;
	public int maxHealth;
	//String name;

	private boolean swinging;
	private float swingTimer;
	private Array<TextureRegion> animFrames;
	private Animation<TextureRegion> runHorizontal;
	private Animation<TextureRegion> attacking;
	private Animation<TextureRegion> swordSwing;
	private Animation<TextureRegion> victory;
	public boolean battleWon;
	private boolean wasMoving;
	private float stateTimer;
	private boolean invisible;
	private float flashTimer;
	private float lastFlash;
	public int wepIndex;
	private int level;

	private Sound damaged;

	private Sprite sword;

	public Battler(World world, String name, GameScreen screen)
	{
		super(screen.getAtlas().findRegion(name));

		level = Integer.parseInt(GameScreen.getLine(1));
		battleWon = false;
		damaged = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/Take Damage.wav"));
		scale(2);
		BodyDef bdef = new BodyDef();
		bdef.position.set(384, 128);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		setBounds(0, 0, 26, 26);

		maxHealth = 10 + 5*level;
		health = Integer.parseInt(GameScreen.getLine(2));

		sword = new Sprite(new TextureAtlas("Battle/Players/Swords/Sword.atlas").findRegion("Sword"));

		wepIndex = 0;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
			wepIndex = Integer.parseInt(reader.readLine());

			reader.close();
		}
		catch (IOException e)
		{
		}

		switch(wepIndex)
		{
			case 1:
				sword.setColor(Color.ORANGE);
				break;
			case 2:
				sword.setColor(Color.WHITE);
				break;
			case 3:
				sword.setColor(Color.GOLD);
				break;
			case 4:
				sword.setColor(Color.CYAN);
				break;
		}

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

		frames.add(new TextureRegion(getTexture(), 0*26+getRegionX(), getRegionY(), 26, 26));
		frames.add(new TextureRegion(getTexture(), 3*26+getRegionX(), getRegionY(), 26, 26));
		victory = new Animation<TextureRegion>(0.25f, frames);
		animFrames.addAll(frames);


		//animFrames.
		frames.clear();

		stateTimer = 0;
		wasMoving = false;

		swinging = false;
		swingTimer = -1;

		stopFlash();
	}

	public void update(float dt)
	{
		if(flashTimer >= 0)
		{
			flashTimer -= dt;
			if(flashTimer < 1)
			{
				if ((lastFlash - flashTimer) >= 0.1)
				{
					invisible = false;
					lastFlash = flashTimer;
				} else if ((lastFlash - flashTimer) >= 0.05)
				{
					invisible = true;
				}
			}
		}
		else
			invisible = false;

		if(health <= 0)
		{
			invisible = false;
			flash();
		}
		getFrame(dt);
		//sword.setPosition(b2body.getPosition().x - getWidth() + 2, b2body.getPosition().y + 26);
		sword.setPosition(b2body.getPosition().x - getWidth() - 6 - 30, b2body.getPosition().y + 40 - 1*16);
		setPosition(b2body.getPosition().x - getWidth() / 2 - 30, b2body.getPosition().y - getHeight()/2 + 16);
		//setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2 + 2*16);
	}

	public void takeDamage()
	{
		damaged.play();
		setRegion(new TextureRegion(getTexture(), 4*26+getRegionX(), getRegionY(), 26, 26));
		health--;
		System.out.println("Player health is: " + health);
		flash();
	}

	public void animate(float timer, float dt)
	{
		if(flashTimer < 0)
		{
			swinging = false;
			if (swingTimer < 0.1f)
			{
				swingTimer = 0.5f;
			}
			if (timer > 1)
				b2body.setLinearVelocity(-120, 0);
			else if (timer > 0.5)
			{
				swinging = true;
				b2body.setLinearVelocity(0, 0);
			}
			else if(timer > 0)
				b2body.setLinearVelocity(120, 0);
			else
			{
				sword.setPosition(b2body.getPosition().x - getWidth() - 6 - 30, b2body.getPosition().y + 40 - 1*16);
				setPosition(b2body.getPosition().x - getWidth() / 2 - 30, b2body.getPosition().y - getHeight()/2 + 16);
			}

		}
	}

	public void getFrame(float dt)
	{
		if(false)
			setRegion(new TextureRegion(getTexture(), 4*26+getRegionX(), getRegionY(), 26, 26));
		else
		{
			if (flashTimer < 0)
				if (battleWon)
				{
					setRegion(victory.getKeyFrame(stateTimer, true));
					stateTimer += dt;
				} else
				{
					//TextureRegion region;
					if (b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
					{
						wasMoving = true;
						setRegion(runHorizontal.getKeyFrame(stateTimer, true));
					} else
					{
						wasMoving = false;

						if (swinging)
						{
							setRegion(attacking.getKeyFrame(stateTimer, true));
							sword.setRegion(swordSwing.getKeyFrame(stateTimer, true));
						} else
						{
							setRegion(runHorizontal.getKeyFrame(0));
							sword.setRegion(swordSwing.getKeyFrame(0));
						}
					}

					if (wasMoving || swinging)
						stateTimer += dt;
					else
						stateTimer = 0;

					//return region;
				}
		}
	}

	private void flash()
	{
		flashTimer = 1;
		lastFlash = 1.1f;
	}

	public void stopFlash()
	{
		flashTimer = -1;
		lastFlash = -1;
		invisible = false;
	}

	public void draw(SpriteBatch sb)
	{
		if(!invisible)
			super.draw(sb);
		if(swinging)
			sword.draw(sb);
	}

	public void dispose()
	{
		sword.getTexture().dispose();
		getTexture().dispose();
	}

	public String getHealth()
	{
		return "" + health + "/" + maxHealth;
	}
}