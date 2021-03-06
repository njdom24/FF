package com.dommie.ffdemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dommie.ffdemo.screens.Overworld;
import com.dommie.ffdemo.sprites.Player;

public class GameInfo extends Game {
	public static final int V_WIDTH = 432;
	public static final int V_HEIGHT = 240;
	/*
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short COLLISION_BIT = 4;
    public static final short DOOR_BIT = 8;
    */
    //public static Stack<GameScreen> screens = new Stack<GameScreen>();
    //public static GameScreen currentScreen;

	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		//setScreen(new Corneria(this, 264, 8));
		setScreen(new Overworld(this, 219 *16+8, 576+8, Player.State.DOWN));
		//setScreen(new Shop(this, null));
		//setScreen(new BattleScreen(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}


}
