package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.tools.B2WorldCreator;

public class Overworld extends MapScreen
{
	public Overworld(GameInfo game, float locX, float locY)
	{
		super(game, "Overworld/Overworld.tmx", locX, locY, true);

		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Cornelia/Untitled.ogg"));
		m.setLooping(true);
		m.setVolume(0.8f);

		m.play();

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int) (locX / 16 + 0.01f), (int) (locY / 16 + 0.01f));
	}
}
