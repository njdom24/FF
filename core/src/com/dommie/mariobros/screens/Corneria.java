package com.dommie.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dommie.mariobros.MarioBros;

/**
 * Created by njdom24 on 5/20/2017.
 */

public class Corneria extends MapScreen {

    public Corneria(MarioBros game, float locX, float locY)
    {
        super(game, "corneria.tmx", locX, locY);
    }

    public void handleInput(float dt)
    {
        super.handleInput(dt);

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            changeMap(new Corneria(game, 8, 8));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            revertMap();
        }

    }

}
