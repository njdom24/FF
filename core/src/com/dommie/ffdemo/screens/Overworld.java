package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.sprites.Player;
import com.dommie.ffdemo.tools.B2WorldCreator;

import java.util.Random;

public class Overworld extends MapScreen
{
	private int tilesMoved;

	private Random rnd;
	private boolean battleTriggered;

	public Overworld(GameInfo game, float locX, float locY, Player.State state)
	{
		super(game, "Overworld/Overworld.tmx", locX, locY, true);

		battleTriggered = false;
		tilesMoved = 0;

		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Overworld/Overworld Loop.ogg"));
		m.setLooping(true);
		m.setVolume(0.4f);

		//m.play();

		//transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/EnterTown.wav"));

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int) (locX / 16 + 0.01f), (int) (locY / 16 + 0.01f));
		player.setState(state);

		rnd = new Random();
	}

	public void update(float dt)
	{
		super.update(dt);
		if(flashTimer == -1)
			if(player.b2body.getPosition().x >= 3496 && player.b2body.getPosition().x <= 3528 && player.b2body.getPosition().y >= 584 && player.b2body.getPosition().y <= 632)
			{
				if ((player.b2body.getPosition().x == 3512 && player.b2body.getPosition().y == 632) ||
					(player.b2body.getPosition().x == 3496 && player.b2body.getPosition().y == 600) ||
					(player.b2body.getPosition().x == 3496 && player.b2body.getPosition().y == 616) ||
					(player.b2body.getPosition().x == 3528 && player.b2body.getPosition().y == 616))
				{
					//transitionSound.dispose();
					//transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/EnterTown.wav"));
					flash();
					flashColor = Color.DARK_GRAY;
					queuedMap = new Corneria(game, 264, 8, Player.State.UP);
				}
			}

		if(queuedMap != null && flashUpdate(dt, flashColor))//if flashing is finished
		{
			queuedMap.setToDispose(this);
			changeMap(queuedMap);
		}

		if(flashTimer == -1)
			if (battleTriggered && !player.isMoving())//AKA Player stopped moving
			{
				transitionSound.dispose();
				transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/EnterBattle.wav"));
				queuedMap = new BattleScreen(game, player.getIntendedPos(), player.getState());
				flash();
				flashColor = Color.OLIVE;
			}
			else if(player.getPos() == 3)
			{
				if(player.movedThisFrame)
				{
					System.out.println("?????");
					if (!battleTriggered)
					{
						player.movedThisFrame = false;
						if (tilesMoved < 10)
							tilesMoved++;
						int thing = rnd.nextInt(99);
						if (thing < (5 + tilesMoved))
							battleTriggered = true;
					}
				}
			}
			else
				tilesMoved = 0;
	}

	protected void flash()
	{
		super.flash();
	}

	public void dispose()
	{
		super.dispose();
		transitionSound.dispose();
	}
}
