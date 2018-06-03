package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.screens.BattleScreen;

import java.util.Random;

public class Enemy extends Sprite
{
	public Body b2body;
	private int health;
	public int damage;
	private String name;

	private boolean invisible;
	private float flashTimer;
	private float lastFlash;
	private boolean dmgTaken;
	private Random rnd;

	private Sound damaged;

	public Enemy(World world, BattleScreen screen, String name)
	{
		super(new Texture("Battle/Enemies/" + name + ".png"));
		rnd = new Random();
		dmgTaken = false;
		damaged = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/Bash.wav"));
		scale(2);
		//setSize(getWidth()*2, getHeight()*2);
		//scale(2);
		setOrigin(0,0);
		//super(screen.getAtlas().findRegion("RedMage"));
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		this.name = name;

		if(name == "Goblin")
		{
			health = 5;
			damage = 1;
		}
		else if(name == "Wolf")
		{
			health = 10;
			damage = 2;
		}
		else
		{
			damage = 3;
			health = 20;
		}

		setBounds(0, 0, 26, 26);

		invisible = false;
		flashTimer = -1;
		lastFlash = -1;
	}

	public void update(float dt)
	{
		if(flashTimer >= 0.3)
		{
			flashTimer -= dt;
			if(flashTimer < 1)
			{
				if(dmgTaken)
				{
					damaged.play();
					dmgTaken = false;
				}
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
		//puts sprite on b2body
		setPosition(40, (GameInfo.V_HEIGHT - 32 + 56)/2 - this.getHeight()/1);//divide by 2 if not scaled
		//setRegion(getFrame(dt));
		b2body.setAwake(true);
	}

	public void takeDamage(int dmg)
	{
		dmgTaken = true;
		health -= dmg;
		flash();
	}

	public int getHealth()
	{
		return health;
	}

	public int giveGold()
	{
		return rnd.nextInt(10) + 10*damage;
	}

	public int giveExp()
	{
		return rnd.nextInt(10) + 10*damage;
	}

	public String getName()
	{
		return name.toUpperCase();
	}

	private void flash()
	{
		flashTimer = 1.5f;
		lastFlash = 1.6f;
	}

	public void draw(SpriteBatch sb)
	{
		if(!invisible)
			super.draw(sb);
	}

	public void dispose()
	{
		getTexture().dispose();
	}

}