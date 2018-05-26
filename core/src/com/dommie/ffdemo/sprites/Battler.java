package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Battler
{
	public Body b2body;
	private int health;
	String name;

	public Battler(String name)
	{
		this.name = name;
		//super(screen.getAtlas().findRegion("RedMage"));
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		//b2body = world.createBody(bdef);

		//setBounds(0, 0, 26, 26);

		health = 10;
	}

	public void update(float dt)
	{

	}

	public String getName()
	{
		return name;
	}

	public void takeDamage()
	{
		health--;
		System.out.println("Player health is: " + health);
	}


}
