package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.tools.B2WorldCreator;
import com.dommie.ffdemo.tools.WorldContactListener;

/**
 * Created by njdom24 on 5/20/2017.
 */

public class Corneria extends MapScreen {

    public Corneria(GameInfo game, float locX, float locY)
    {
        super(game, "corneria.tmx", locX, locY);

        npcAtlas = new TextureAtlas("Overworld/Maps/Corneria/NPC/NPCs.atlas");
        String[] dialogue = new String[1];
        String[] types = {"Dancer"};

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
		creator = new B2WorldCreator(world, map, collisions);//dispose later?
		player.setCollisionArray(collisions);
		player.setStartingIndex((int)(locX/16), (int)(locY/16));
		npcs = creator.createGenericNPCs(dialogue, types, this);
        WorldContactListener.npcs = npcs;
    }

    public Corneria(GameInfo game)
    {
        super(game, "corneria.tmx", 264, 8);

        npcAtlas = new TextureAtlas("Overworld/Maps/Corneria/NPC/NPCs.atlas");
        String[] dialogue = new String[1];
        String[] types = {"Dancer"};

		collisions = new int[prop.get("height", Integer.class)][prop.get("width", Integer.class)];
        creator = new B2WorldCreator(world, map, collisions);
        player.setCollisionArray(collisions);
        player.setStartingIndex(264/16, 8/16);
        npcs = creator.createGenericNPCs(dialogue, types, this);
        WorldContactListener.npcs = npcs;
    }

    public void handleInput(float dt)
    {
        super.handleInput(dt);

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            Corneria m = new Corneria(game, 264-(16*2), 8);
            m.setToDispose(this);
            changeMap(m);
        }

		if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			hud.createTextbox(20, 4, "Testing... testing... Oh? Hello!");
		}

        if(player.b2body.getPosition().x <= 0)
        {
            Corneria m = new Corneria(game, 264, 8);
            m.setToDispose(this);
            changeMap(m);
        }
    }

}
