package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;
import com.dommie.ffdemo.sprites.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Shop extends GameScreen
{
	private Sprite player;
	private Sprite shopkeep;
	private Hud wepList;
	private Cursor c;

	public Shop(GameInfo game)
	{
		this.game = game;

		c = new Cursor(130, 35, 4, 2);

		gamecam = new OrthographicCamera();
		gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
		hud = new Hud(gamecam);
		hud.createTextbox(9, 5, "What doyou\nwant?", -21, 24);
		hud.finishText();

		wepList = new Hud(gamecam);
		wepList.createTextbox(9, 19, "\n\nWood\n\n\n\nIron\n\n\n\nGolden\n\n\n\nMagic", 22, 5);
		wepList.finishText();

		player = new Sprite(new TextureAtlas("Battle/Players/BattleSprites.atlas").findRegion("RedMage"));
		player.scale(2);
		player.setPosition(32,-8);

		shopkeep = new Sprite(new Texture("Shops/WeaponShop.png"));
		shopkeep.scale(2);
		shopkeep.setPosition(-96,-43);
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
		player.draw(game.batch);
		shopkeep.draw(game.batch);
		game.batch.end();

		game.hudBatch.begin();

		hud.update(delta);
		hud.draw(game.hudBatch);
		wepList.update(delta);
		wepList.draw(game.hudBatch);

		c.draw(game.hudBatch);

		game.hudBatch.end();
	}

	private void update(float dt)
	{
		if (prevScreen != null) {
			prevScreen.dispose();
			prevScreen = null;
		}
		c.update(dt);
		handleInput(dt);
	}

	public void handleInput(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter("TempSave.txt"));
				writer.write("" + c.getPos());

				writer.close();

				Corneria map = new Corneria(game, 184, 200, Player.State.DOWN);
				map.setToDispose(this);
				changeMap(map);
			}
			catch (IOException e)
			{

			}
		}
	}
}
