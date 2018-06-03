package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;

public class Inn extends Shop
{
	private Sprite blackScreen;
	private float animTimer;
	private Sound rest;
	private boolean finished;
	private boolean cantAfford;

	public Inn(GameInfo game, GameScreen exit)
	{
		super(game, "Hello!", "Inn", exit);
		m.pause();
		m.dispose();
		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Cornelia/Inn.ogg"));
		m.setLooping(true);

		cantAfford = false;
		finished = false;
		rest = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/InnRest.wav"));
		rest.setVolume(0, 0.1f);

		blackScreen = new Sprite(new Texture("Battle/Black.png"));
		blackScreen.scale(2);
		animTimer = -1;

		hud.createTextbox(14, 8, "It's 50 gil a night.\n\nWill you be staying\nwith us?", -19, 12);
		hud.finishText();
		c = new Cursor(117, 18, 2, 2);

		prompt = new Hud(gamecam);
		prompt.createTextbox(6, 9, "\nYES\n\n\n\nNO", 19, 12);
		prompt.finishText();
	}

	public void render(float dt)
	{
		super.render(dt);

		game.hudBatch.begin();

		if(flashTimer == -1 && animTimer == -1)
		{
			c.draw(game.hudBatch);
		}
		else
		{
			prompt.dispose();
		}
		if(finished)
		{
			hud.update(dt);
			hud.draw(game.hudBatch);
			m.play();
		}

		game.hudBatch.end();

		game.batch.begin();

		if(flashTimer == -2)
			player.draw(game.batch);
		if(animTimer != -1)
		{
			keepCover.setAlpha(0);
			blackScreen.draw(game.batch);
		}

		game.batch.end();
	}

	protected void update(float dt)
	{
		super.update(dt);
		if(!finished && flashTimer != -2)
			c.update(dt);
		if(flashTimer == -1 && animTimer == -1 || finished)
			handleInput(dt);
		else if(animTimer != -1)
		{
			if(animTimer - dt > 0)
			{
				animTimer -= dt;
				if(animTimer > 6 - 6/3)
					blackScreen.setAlpha(0.3333333f);
				else if(animTimer > 6 - 12/3)
					blackScreen.setAlpha(0.6666666f);
				else
					blackScreen.setAlpha(1);
			}
			else if(animTimer - dt > -1)
			{
				animTimer -= dt;
				blackScreen.setAlpha(1+animTimer);
			}
			else
				animTimer = -1;
		}
		else
		{
			finished = true;
			//flashTimer = -1;
		}
	}

	protected void handleInput(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			System.out.println("Thonking");
			if(!finished)
				switch(c.getPos())
				{
					case 1:
						if(money >= 50)
						{
							c.playSound();
							changeLine(2, "" + (10 + Integer.parseInt(getLine(1)) * 5));
							money -= 50;
							animTimer = 6;
							flashTimer = -2;//Just to make m stop playing
							m.pause();
							//m.dispose();
							rest.play();
							hud.createTextbox(14, 8, "Did you\nsleep well? \nWe hope to\nsee you\nagain soon!", -19, 12);
							//hud.finishText();
						}
						else
							if(!cantAfford)
							{
								//c.playSound();
								hud.createTextbox(14, 8, "I'm sorry.\nYou don't\nhave enough gil.", -19, 12);
								cantAfford = true;//Just to stop exit() from being called more than once
							}
						break;
					case 2:
						exit();
						break;
				}
			else if(hud.isFinished() && !cantAfford)
				exit();
		}
	}

	protected void exit()
	{
		super.exit();
		cantAfford = true;
		hud.finishText();
		m.setVolume(0);
		keepCover.setAlpha(1);
	}
}
