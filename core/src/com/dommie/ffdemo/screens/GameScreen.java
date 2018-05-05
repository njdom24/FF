package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Hud;

public abstract class GameScreen implements Screen, Disposable{
    //Reference to Game, used to set Screens
    protected GameInfo game;
    protected TextureAtlas atlas;
    protected TextureAtlas npcAtlas;
    protected OrthographicCamera gamecam;

    protected Viewport gamePort;
    protected Hud hud;

    protected Music m;

    //Tiled map variables
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    protected World world;
    protected Box2DDebugRenderer b2dr;
    
    protected GameScreen prevScreen;//dispose on normal change, keep on BattleScreen change

    public void changeMap(GameScreen m)
    {
        //GameInfo.screens.push(this);
        //GameInfo.currentScreen = m;

        game.setScreen(m);
    }

    /*
    public void revertMap()
    {
    	//prevScreen.setChangeElements();
        game.setScreen(prevScreen);
    	prevScreen.setToDispose(this);
    }*/

    
    protected void setChangeElements()
    {
    	game.setScreen(this);
    	//GameInfo.currentScreen = this;
    }
    
    public void setToDispose(GameScreen g)
    {
    	prevScreen = g;
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public TextureAtlas getNPCAtlas()
    {
        return npcAtlas;
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
    	game.dispose();
    	atlas.dispose();
    	npcAtlas.dispose();
        map.dispose();
        renderer.dispose();
        b2dr.dispose();
        hud.dispose();
        world.dispose();
        m.dispose();
    }

}
