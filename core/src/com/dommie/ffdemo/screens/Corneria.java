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

        npcs = new B2WorldCreator(world, map).createGenericNPCs(dialogue, types, this);
        WorldContactListener.npcs = npcs;
    }

    public Corneria(GameInfo game)
    {
        super(game, "corneria.tmx", 264, 8);

        npcAtlas = new TextureAtlas("Overworld/Maps/Corneria/NPC/NPCs.atlas");
        String[] dialogue = new String[1];
        String[] types = {"Dancer"};

        npcs = new B2WorldCreator(world, map).createGenericNPCs(dialogue, types, this);
        WorldContactListener.npcs = npcs;
    }

    public void handleInput(float dt)
    {
        super.handleInput(dt);

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            Corneria m = new Corneria(game, 8, 8);
            m.setToDispose(this);
            changeMap(m);
        }

        if(player.b2body.getPosition().x <= 0)
        {
            Corneria m = new Corneria(game, 264, 8);
            m.setToDispose(this);
            changeMap(m);
        }
    }

}
