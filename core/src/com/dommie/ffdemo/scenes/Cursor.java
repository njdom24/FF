package com.dommie.ffdemo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cursor extends Sprite
{
	private boolean hidden;
	int limit;
	int pos;
	private int scale = LwjglApplicationConfiguration.getDesktopDisplayMode().width /com.dommie.ffdemo.GameInfo.V_WIDTH;
	private Sound moveCursor;

	public Cursor(int x, int y)
	{
		super(new Texture("Battle/Cursor.png"));
		moveCursor = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Text/Cursor Move.wav"));

		hidden = false;
		pos = 3;
		limit = 3;
		setPosition((x+8)*scale, (y+32)*scale);
		this.setScale(scale);
	}

	public void update(float dt)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			moveCursor.play();
			if(pos < limit)
			{
				setY(this.getY() + 16 * scale);
				pos++;
			}
			else
			{
				setY(this.getY() - (16*scale)*(limit-1));
				pos = 1;
			}
		}
		else
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
			{
				moveCursor.play();
				if(pos > 1)
				{
					setY(this.getY() - 16 * scale);
					pos--;
				}
				else
				{
					setY(this.getY() + (16*scale)*(limit-1));
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
		return pos;
	}
}
