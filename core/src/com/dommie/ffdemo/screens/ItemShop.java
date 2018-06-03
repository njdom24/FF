package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;

public class ItemShop extends Shop
{
	private Hud quantity;
	private boolean buying;
	private int amount;
	private int price;
	private int purchaseLine;

	public ItemShop(GameInfo game, GameScreen exit)
	{
		super(game, "What doyou\nwant?", "ItemShop", exit);
		c = new Cursor(86, 26, 3, 2);

		prompt = new Hud(gamecam);
		prompt.createTextbox(14, 13, "\nPOTION\n10G\n\n\nSUPER POTION25G\n\n\nEXIT", 19, 9);
		prompt.finishText();
		quantity = new Hud(gamecam);
		buying = false;
		amount = 1;
		price = 0;
		purchaseLine = 0;
	}

	public void render(float dt)
	{
		super.render(dt);

		game.hudBatch.begin();

		if(flashTimer == -1)
		{
			if(buying)
				quantity.draw(game.hudBatch);
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
			if(!buying)
				c.update(dt);
			handleInput(dt);
		}
	}

	public void handleInput(float dt)
	{
		if(buying)
		{

			if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
			{
				c.playSound();
				amount++;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
			{
				c.playSound();
				amount--;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
			{
				c.playSound();
				amount += 10;
				if(amount > 100)
					amount -= 100;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
			{
				c.playSound();
				amount -= 10;
				if(amount < 0)
					amount += 100;
			}
			if(amount == 100)
				amount = 1;
			else if(amount == 0)
				amount = 99;

			quantity.createTextbox(14, 6, "\n<" + amount + ">\n\n" + price*amount + "G", 19, (int)(c.getY()-10)/8 + 14);
			quantity.finishText();

			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			{
				c.playSound();
				buying = false;
			}

			else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			{
				if(price*amount <= money)
				{
					hud.createTextbox(9, 5, "Thanks!", -21, 24);
					purchase.play();
					money -= price*amount;
					moneyDisp.createTextbox(9, 3, " " + money + "G", 0, 27);
					moneyDisp.finishText();
					incrementLine(purchaseLine, amount);
					buying = false;
					//exit();
				}
				else
					c.incorrect();
			}
		}
		else
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			{
				System.out.println("BUYING: " + buying);
				System.out.println("CPOS: " + c.getPos());
					switch(c.getPos())
					{
						case 1:
							decidePurchase(10, 4);
							break;
						case 2:
							decidePurchase(25, 5);
							break;
						case 3:
							exit();
							break;
					}
			}
	}

	private void decidePurchase(int cost, int line)
	{
		purchaseLine = line;
		if(!buying && money >= cost)
		{
			//c.playSound();
			amount = 1;
			buying = true;
			hud.createTextbox(9, 5, "How\nmany?", -21, 24);
			price = cost;
			quantity.createTextbox(14, 6, "\n<" + amount + ">\n\n" + price*amount + "G", 19, (int)(c.getY()-10)/8 + 14);
			quantity.finishText();
		}
		else
			c.incorrect();
	}

}
