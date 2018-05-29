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
		setPosition(x, y);
		this.setScale(scale);
	}

	public void update(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			moveCursor.play();
			if(pos < limit)
			{
				setY(this.getY() + 16 * dist * scale);
				pos++;
			}
			else
			{
				setY(this.getY() - (16*dist*scale)*(limit-1));
				pos = 1;
			}
		}
		else
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
			{
				moveCursor.play();
				if(pos > 1)
				{
					setY(this.getY() - 16 * dist * scale);
					pos--;
				}
				else
				{
					setY(this.getY() + (16*dist*scale)*(limit-1));
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
		if(!hidden)
			super.draw(sb);
	}


	public int getPos()
	{
		return limit-pos+1;
	}
}
