package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.screens.BattleScreen;

public class Enemy extends Sprite
{
	public Body b2body;
	private int health;
	private String name;

	public Enemy(World world, BattleScreen screen, int health, String name)
	{
		super(new Texture("Battle/Enemies/Goblin.png"));
		this.scale(2);
		//super(screen.getAtlas().findRegion("RedMage"));
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		this.health = health;
		this.name = name;

		setBounds(0, 0, 26, 26);
	}

	public void update(float dt)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			b2body.setLinearVelocity(-10, 0);
		else
			b2body.setLinearVelocity(0,0);


		//puts sprite on b2body
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2 + 2*16);
		//setRegion(getFrame(dt));
		b2body.setAwake(true);
	}

	public void takeDamage(int dmg)
	{
		health -= dmg;
	}

	public int getHealth()
	{
		return health;
	}

	public String getName()
	{
		return name;
	}
}