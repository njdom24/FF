package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.sprites.NPC;
import com.dommie.ffdemo.sprites.Player;
import com.dommie.ffdemo.tools.B2WorldCreator;
import com.dommie.ffdemo.tools.WorldContactListener;

import java.util.ArrayList;

/**
 * Created by njdom24 on 5/20/2017.
 */

public class Corneria extends MapScreen {

    public Corneria(GameInfo game, float locX, float locY, Player.State state)
    {
        super(game, "corneria.tmx", locX, locY, false);

        m = Gdx.audio.newMusic(Gdx.files.internal("Music/Cornelia/Untitled.ogg"));
		m.setLooping(true);
		m.setVolume(0.8f);

        npcAtlas = new TextureAtlas("Overworld/Maps/Corneria/NPC/NPCs.atlas");
        String[][] dialogue = new String[1][2];
        //String[] types = {"Dancer"};
        dialogue[0][0] = "Steph is a heckin\nnerd\nxDDDDDDDDDDDDDDD";
        dialogue[0][1] = "i want to die please\nhelp me";

        npcs = new ArrayList<NPC>();

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int)(locX/16+0.01f), (int)(locY/16+0.01f));
		player.setState(state);
		//npcs = creator.createGenericNPCs(dialogue, types, this);

		Rectangle r = new Rectangle(19*16, 7*16, 16, 16);
		npcs.add(new NPC(world, map, r, true, 2, "Dancer", this));
		npcs.get(0).setMessages(dialogue[0]);


        WorldContactListener.npcs = npcs;
    }

    public void handleInput(float dt)
	{
		super.handleInput(dt);

		if (Gdx.input.isKeyJustPressed(Input.Keys.B))
		{
			hud.createTextbox(20, 4, "Testing... testing... Oh? Hello!");
		}

		if (player.b2body.getPosition().x <= 0)
		{
			Corneria map = new Corneria(game, 264, 8, Player.State.UP);
			map.setToDispose(this);
			changeMap(map);
		}

		if ((player.b2body.getPosition().x == 264 && player.b2body.getPosition().y == 8 && Gdx.input.isKeyPressed(Input.Keys.DOWN))
				|| (player.b2body.getPosition().x == 504 && player.b2body.getPosition().y == 184 && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				|| (player.b2body.getPosition().x == 264 && player.b2body.getPosition().y == 376 && Gdx.input.isKeyPressed(Input.Keys.UP)))
		{
			transitionSound.dispose();
			transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/ExitTown.wav"));
			queuedMap = new Overworld(game, 3512, 616, Player.State.DOWN);
			flash();
			flashColor = Color.DARK_GRAY;
		}
	}

	public void update(float dt)
	{
		super.update(dt);

		if(flashTimer == -1)
		{
			m.play();
			if (player.b2body.getPosition().x == 184 && player.b2body.getPosition().y == 216)
			{
				door = new Sprite(new Texture("Overworld/Maps/Corneria/OpenedDoor.png"));
				door.setPosition(player.b2body.getPosition().x-8, player.b2body.getPosition().y-8);
				transitionSound.dispose();
				transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/Door.wav"));
				queuedMap = new WeaponShop(game, new Corneria(game, 184, 200, Player.State.DOWN));
				flash();
				flashColor = Color.PURPLE;
			}
		}

		if(queuedMap != null && flashUpdate(dt, flashColor))//if flashing is finished
		{
			queuedMap.setToDispose(this);
			changeMap(queuedMap);
		}
	}

}
