package com.dommie.ffdemo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cursor extends Sprite
{
	private boolean hidden;
	int limit;
	int pos;
	private int dist;
	private int scale = 1;
	private Sound moveCursor;
	private int y;

	public Cursor(int x, int y)
	{
		this(x, y, 3, 1);
	}

	public Cursor(int x, int y, int positions, int distance)
	{
		super(new Texture("Battle/Cursor.png"));
		dist = distance;
		moveCursor = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Text/Cursor Move.wav"));

		hidden = false;
		pos = positions;
		limit = positions;
		this.y = y;
		setPosition(x, y);
		this.setScale(scale);
	}

	public void update(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			moveCursor.play();
			if(pos < limit)
				pos++;
			else
				pos = 1;
		}
		else
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
			{
				moveCursor.play();
				if(pos > 1)
				{
					pos--;
				}
				else
				{
					pos = limit;
				}
			}
	}

	public void hide()
	{
		hidden = true;
	}

	public void unHide()
	{
		hidden = false;
	}

	public void draw(SpriteBatch sb)
	{
		setY(y+(pos-limit)*16*dist);
		if(!hidden)
			super.draw(sb);
	}


	public int getPos()
	{
		System.out.println("ACTUAL POS: " + pos);
		return limit-pos+1;
	}
	public void setPos(int newPos)
	{
		if(limit-pos+1 > 0 && limit-pos+1 <= limit)
		{
			pos = limit+1 - newPos;
		}
	}
}
