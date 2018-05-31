package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;

public class WeaponShop extends Shop
{
	private Hud wepList;
	private Cursor c;

	public WeaponShop(GameInfo game, GameScreen exit)
	{
		super(game, "What doyou\nwant?", "Weapon", exit);
		c = new Cursor(130, 50, 5, 2);

		wepList = new Hud(gamecam);
		wepList.createTextbox(9, 21, "\nWood\n50G\n\n\nIron\n100G\n\n\nGolden\n200G\n\n\nMagic\n400G\n\n\nEXIT", 22, 4);
		wepList.finishText();

	}

	public void render(float dt)
	{
		super.render(dt);

		game.hudBatch.begin();

		if(flashTimer == -1)
		{
			moneyDisp.update(dt);
			moneyDisp.draw(game.hudBatch);
			hud.update(dt);
			hud.draw(game.hudBatch);
			wepList.update(dt);
			wepList.draw(game.hudBatch);
			c.draw(game.hudBatch);
		}
		else
		{
			moneyDisp.dispose();
			hud.dispose();
			wepList.dispose();
		}

		game.hudBatch.end();
	}

	protected void update(float dt)
	{
		super.update(dt);
		c.update(dt);
		handleInput(dt);
	}

	public void handleInput(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			int money = Integer.parseInt(getLine(3));
			switch(c.getPos())
			{
				case 1:
					if(money >= 50)
					{
						changeLine(0, "" + c.getPos());
						incrementLine(3, -50);
					}
					break;
				case 2:
					if(money >= 100)
					{
						changeLine(0, "" + c.getPos());
						incrementLine(3, -100);
					}
					break;
				case 3:
					if(money >= 200)
					{
						changeLine(0, "" + c.getPos());
						incrementLine(3, -200);
					}
					break;
				case 4:
					if(money >= 400)
					{
						changeLine(0, "" + c.getPos());
						incrementLine(3, -400);
					}
					break;
			}
			exit();
		}
	}
}
