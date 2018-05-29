package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.sprites.Player;
import com.dommie.ffdemo.tools.B2WorldCreator;

import java.util.Random;

public class Overworld extends MapScreen
{
	private boolean movedLastFrame;
	private float flashTimer;
	private float lastFlash;

	private Sound enterBattle;

	private Random rnd;

	public Overworld(GameInfo game, float locX, float locY, Player.State state)
	{
		super(game, "Overworld/Overworld.tmx", locX, locY, true);

		movedLastFrame = false;
		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Overworld/Overworld Loop.ogg"));
		m.setLooping(true);
		m.setVolume(0.4f);

		m.play();

		enterBattle = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/EnterBattle.wav"));

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int) (locX / 16 + 0.01f), (int) (locY / 16 + 0.01f));
		player.setState(state);

		flashTimer = -1;
		lastFlash = -1;

		rnd = new Random();
	}

	public void update(float dt)
	{
		super.update(dt);
		if(player.b2body.getPosition().x == 3512 && player.b2body.getPosition().y == 632)
		{
			MapScreen corneria = new Corneria(game, 264, 8, Player.State.UP);
			corneria.setToDispose(this);
			changeMap(corneria);
		}
		if(flashTimer >= 0)
		{
			flashTimer -= dt;
			if ((lastFlash-flashTimer) >= 0.2)
			{
				renderer.getBatch().setColor(Color.WHITE);
				lastFlash = flashTimer;
			}
			else if((lastFlash - flashTimer) >= 0.1)
			{
				renderer.getBatch().setColor(Color.OLIVE);
			}
		}
		else if (enteringBattle && flashTimer >= -0.6)
		{
			renderer.getBatch().setColor(Color.BLACK);
			flashTimer -= dt;
		}
		else if(lastFlash != -1)//Flashing is just finished
		{
			BattleScreen btlScrn = new BattleScreen(game, player.getIntendedPos(), player.getState());
			btlScrn.setToDispose(this);
			changeMap(btlScrn);
		}
		else
			renderer.getBatch().setColor(Color.WHITE);

		if(player.getPos() == 3 && flashTimer < 0)
		{
			if(rnd.nextInt(99) < 10)
			{
				if (movedLastFrame && !player.isMoving())//AKA Player stopped moving
				{
					movedLastFrame = false;
					flash();
				}
				if (player.isMoving())
					movedLastFrame = true;
			}
		}
	}

	private void flash()
	{
		m.pause();
		enterBattle.play();
		enteringBattle = true;
		flashTimer = 1;
		lastFlash = 1.1f;
	}

	public void dispose()
	{
		super.dispose();
		enterBattle.dispose();
	}
}
