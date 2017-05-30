package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Hud;
import com.dommie.ffdemo.tools.WorldContactListener;

public abstract class GameScreen implements Screen{
    //Reference to Game, used to set Screens
    protected GameInfo game;
    protected TextureAtlas atlas;

    protected Viewport gamePort;
    protected Hud hud;

    //Tiled map variables
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    protected World world;
    protected Box2DDebugRenderer b2dr;

    public void changeMap(Screen m)
    {
        com.dommie.ffdemo.GameInfo.screens.push(this);
        game.setScreen(m);
    }

    public void revertMap()
    {
        Screen futureScreen = com.dommie.ffdemo.GameInfo.screens.pop();

        if(futureScreen instanceof MapScreen)
            WorldContactListener.currentCollisions = ((MapScreen) futureScreen).mapCollisions;

        game.setScreen(futureScreen);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }
    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

}
