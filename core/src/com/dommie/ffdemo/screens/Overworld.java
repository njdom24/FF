package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.sprites.Player;
import com.dommie.ffdemo.tools.B2WorldCreator;

public class Overworld extends MapScreen
{
	private boolean movedLastFrame;
	private float flashTimer;
	private float lastFlash;

	private boolean flashDone;

	public Overworld(GameInfo game, float locX, float locY, Player.State state)
	{
		super(game, "Overworld/Overworld.tmx", locX, locY, true);

		movedLastFrame = false;

		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Cornelia/Untitled.ogg"));
		m.setLooping(true);
		m.setVolume(0.8f);

		m.play();

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int) (locX / 16 + 0.01f), (int) (locY / 16 + 0.01f));
		player.setState(state);

		flashTimer = -1;
		lastFlash = -1;
		flashDone = false;
	}

	public void update(float dt)
	{
		super.update(dt);
		if(flashTimer >= 0)
		{
			flashTimer -= dt;
			if ((lastFlash-flashTimer) >= 0.2)
			{
				renderer.getBatch().setColor(Color.WHITE);
				//invisible = false;
				lastFlash = flashTimer;
			}
			else if((lastFlash - flashTimer) >= 0.1)
			{
				renderer.getBatch().setColor(Color.OLIVE);
				//invisible = true;
			}
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
			if(movedLastFrame && !player.isMoving())//AKA Player stopped moving
			{
				movedLastFrame = false;
				flash();
			}
			if(player.isMoving())
				movedLastFrame = true;
		}
	}

	private void flash()
	{
		enteringBattle = true;
		flashTimer = 1;
		lastFlash = 1.1f;
	}
}
