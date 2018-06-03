package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;

public class WeaponShop extends Shop
{
	private boolean finished;

	public WeaponShop(GameInfo game, GameScreen exit)
	{
		super(game, "What doyou\nwant?", "WeaponShop", exit);
		c = new Cursor(130, 50, 5, 2);
		finished = false;

		prompt = new Hud(gamecam);
		prompt.createTextbox(9, 21, "\nWood\n50G\n\n\nIron\n100G\n\n\nGolden\n200G\n\n\nMagic\n400G\n\n\nEXIT", 22, 4);
		prompt.finishText();
	}

	public void render(float dt)
	{
		super.render(dt);

		game.hudBatch.begin();

		if(flashTimer == -1)
		{
			//prompt.update(dt);
			//prompt.draw(game.hudBatch);
			c.draw(game.hudBatch);
		}
		else
		{
			prompt.dispose();
		}

		game.hudBatch.end();
	}

	protected void update(float dt)
	{
		super.update(dt);
		if(flashTimer == -1)
		{
			if(!finished)
				c.update(dt);
			handleInput(dt);
		}
	}

	public void handleInput(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			if(!finished)
				switch(c.getPos())
				{
					case 1:
						decidePurchase(50);
						break;
					case 2:
						decidePurchase(100);
						break;
					case 3:
						decidePurchase(200);
						break;
					case 4:
						decidePurchase(400);
						break;
					case 5:
						exit();
				}
				else
					if(hud.isFinished())
						exit();
		}
	}

	private void decidePurchase(int cost)
	{
		if(money > cost)
		{
			changeLine(0, "" + c.getPos());
			money -= cost;
			moneyDisp.createTextbox(9, 3, " " + money + "G", 0, 27);
			moneyDisp.finishText();
			finished = true;
			prompt.dispose();
			c.dispose();
			hud.createTextbox(14, 4, "Come again!", -19, 15);
		}
		else
			c.incorrect();
	}
}
