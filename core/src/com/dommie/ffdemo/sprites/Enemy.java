package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.screens.BattleScreen;

public class Enemy extends Sprite
{
	public Body b2body;
	private int health;
	private String name;

	private boolean invisible;
	private float flashTimer;
	private float lastFlash;
	private boolean dmgTaken;

	private Sound damaged;

	public Enemy(World world, BattleScreen screen, int health, String name)
	{
		super(new Texture("Battle/Enemies/Goblin.png"));
		dmgTaken = false;
		damaged = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/Bash.wav"));
		scale(2);
		//super(screen.getAtlas().findRegion("RedMage"));
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		this.health = health;
		this.name = name;

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
		setPosition(b2body.getPosition().x - getWidth() / 2 - 128, b2body.getPosition().y - getHeight()/2 + 2*16);
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

	public String getName()
	{
		return name;
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