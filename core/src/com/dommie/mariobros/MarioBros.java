package com.dommie.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dommie.mariobros.screens.Corneria;

import java.util.Stack;

public class MarioBros extends Game {
	public static final int V_WIDTH = 432;
	public static final int V_HEIGHT = 240;
	public static final float PPM = 100;

    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short COLLISION_BIT = 4;
    public static final short DOOR_BIT = 8;
    public static Stack<Screen> screens = new Stack<Screen>();

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new Corneria(this, 264/MarioBros.PPM, 8/MarioBros.PPM));
	}

	@Override
	public void render ()
	{
		super.render();
	}


}