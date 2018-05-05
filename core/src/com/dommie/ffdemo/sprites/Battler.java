package com.dommie.ffdemo.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dommie.ffdemo.screens.BattleScreen;

public class Battler extends Sprite
{
	public Body b2body;

	public Battler(World world, BattleScreen screen)
	{
		super(screen.getAtlas().findRegion("RedMage"));
		BodyDef bdef = new BodyDef();
		bdef.position.set(32, 32);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		setBounds(0, 0, 26, 26);
	}

	public void update(float dt)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			b2body.setLinearVelocity(-10, 0);
		else
			b2body.setLinearVelocity(0,0);


		//puts sprite on b2body
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
		//setRegion(getFrame(dt));
		b2body.setAwake(true);
	}
}
