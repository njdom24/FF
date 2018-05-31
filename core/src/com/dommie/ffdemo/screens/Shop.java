package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Hud;

public abstract class Shop extends GameScreen
{
	private Sprite player;
	private Sprite shopkeep;
	private Sprite keepCover;
	private GameScreen returnScreen;
	protected Hud moneyDisp;

	public Shop(GameInfo game, String question, String shopType, GameScreen exit)
	{
		this.game = game;
		returnScreen = exit;
		transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/Door.wav"));

		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Cornelia/Wep.ogg"));
		m.setLooping(true);
		m.setVolume(0.8f);

		gamecam = new OrthographicCamera();
		gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
		hud = new Hud(gamecam);
		hud.createTextbox(9, 5, question, -21, 24);
		hud.finishText();

		moneyDisp = new Hud(gamecam);
		moneyDisp.createTextbox(9, 3, " " + getLine(3) + "G", 0, 27);
		moneyDisp.finishText();

		player = new Sprite(new TextureAtlas("Battle/Players/BattleSprites.atlas").findRegion("RedMage"));
		player.scale(2);
		player.setPosition(32,-8);

		shopkeep = new Sprite(new Texture("Shops/" + shopType + "Shop.png"));
		shopkeep.scale(2);
		shopkeep.setPosition(-96,-43);

		keepCover = new Sprite(new Texture("Shops/KeepCover.png"));
		keepCover.scale(2);
		keepCover.setPosition(-76,-8);
	}
	@Override
	public void render(float delta) {
		//update separately from render

		update(delta);
		//clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//b2dr.render(world, gamecam.combined);

		//set batch to draw what the Hud camera sees
		game.batch.setProjectionMatrix(gamecam.combined);
		game.hudBatch.setProjectionMatrix(gamecam.projection);

		game.batch.begin();
		shopkeep.draw(game.batch);
		if(flashTimer == -1)
			player.draw(game.batch);
		else
		{
			hud.dispose();
			keepCover.draw(game.batch);
		}

		game.batch.end();
	}

	protected void update(float dt)
	{
		if(flashTimer == -1)
			m.play();
		if (prevScreen != null) {
			prevScreen.dispose();
			prevScreen = null;
		}
		if(flashUpdate(dt, Color.FIREBRICK))
		{
			returnScreen.setToDispose(this);
			changeMap(returnScreen);
		}

	}

	protected boolean flashUpdate(float dt, Color col)
	{
		if(flashTimer >= 0)
		{
			flashTimer -= dt;
			if ((lastFlash-flashTimer) >= 0.2)
			{
				shopkeep.setColor(Color.WHITE);
				lastFlash = flashTimer;
			}
			else if((lastFlash - flashTimer) >= 0.1)
			{
				shopkeep.setColor(col);
			}
		}
		else if (flashTimer >= -0.6)
		{
			shopkeep.setColor(Color.BLACK);
			flashTimer -= dt;
		}
		else if(lastFlash != -1)//Flashing is just finished
		{
			return true;
		}
		else
		{
			shopkeep.setColor(Color.WHITE);
		}
		return false;
	}

	protected void exit()
	{
		flash();
		m.pause();
		m.dispose();
	}
}
