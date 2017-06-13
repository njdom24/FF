package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Hud;
import com.dommie.ffdemo.sprites.NPC;
import com.dommie.ffdemo.sprites.Player;
import com.dommie.ffdemo.sprites.TestBody;
import com.dommie.ffdemo.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public abstract class MapScreen extends GameScreen{
    //Reference to Game, used to set Screens

    protected OrthographicCamera gamecam;//what the viewport displays

    //Tiled map variables
    protected TmxMapLoader maploader;
    protected MapProperties prop;

    //Box2d variables
    protected Player player;

    //Movement variables
    public Set<String> mapCollisions;

    private TestBody t;

    protected ArrayList<NPC> npcs;

    public MapScreen(GameInfo game, String mapName, float locX, float locY)
    {
        atlas = new TextureAtlas("overworld_jobs.atlas");
        this.game = game;
        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
        hud = new Hud(game.batch, gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load(mapName);
        prop = map.getProperties();
        renderer = new OrthogonalTiledMapRenderer(map);


        world = new World(new Vector2(0, 0), true);//sets gravity properties
        b2dr = new Box2DDebugRenderer();
        //map.getLayers().get(0).getObjects().get(0).

        player = new Player(world, this);
        player.b2body.setTransform(locX, locY, 0);//world entrance location

        setCamera();


        world.setContactListener(new WorldContactListener());
        mapCollisions = new TreeSet<String>();
        WorldContactListener.currentCollisions = mapCollisions;
        WorldContactListener.player = player;
        GameInfo.currentScreen = this;

        t = new TestBody(world, map);
    }
    
    public void changeMap(GameScreen m)
    {
    	super.changeMap(m);
    	player.b2body.setTransform(player.getIntendedPos(), 0);
    }
    
    protected void setChangeElements()
    {
    	super.setChangeElements();
    	
    	WorldContactListener.currentCollisions = mapCollisions;
    	WorldContactListener.player = player;
    	WorldContactListener.npcs = npcs;
    }

    public void setCamera()
    {
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        //changes inital camera placement based on initial player position
        if(isTooFarLeft())
            gamecam.position.set(13.5f*16, gamecam.position.y, 0);
        else
        if(isTooFarRight())
            gamecam.position.set((prop.get("width", Integer.class)-13.5f)*16f, gamecam.position.y, 0);
        if(isTooFarUp())
            gamecam.position.set(gamecam.position.x, (prop.get("height", Integer.class)-7.5f)*16f, 0);
        else
        if(isTooFarDown())
            gamecam.position.set(gamecam.position.x, 7.5f*16, 0);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    public void handleInput(float dt)
    {

    }

    public void update(float dt)//delta time
    {
        player.update(dt);

        handleInput(dt);
        t.update(dt);
        world.step(1/60f, 6, 2);


        if(!isTooFarLeft() && !isTooFarRight())
            gamecam.position.x = player.b2body.getPosition().x;
        if(!isTooFarUp() && !isTooFarDown())
            gamecam.position.y = player.b2body.getPosition().y;

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //update separately from render
        update(delta);

        //clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render game map
        renderer.render();

        //render Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        hud.createTextbox(20,4);
        //set batch to draw what the Hud camera sees
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        //hud.draw(game.batch);
        for(NPC n : npcs)
        {
            n.update(delta);
            n.draw(game.batch);
        }

        game.batch.end();

        //set batch to draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    public boolean isTooFarLeft()
    {
        if(player.b2body.getPosition().x+8 <= 14*16)
            return true;
        else
            return false;
    }

    public boolean isTooFarRight()
    {
        int mapWidth = prop.get("width", Integer.class)+1;
        int mapPixelWidth = mapWidth*16;

        if(player.b2body.getPosition().x+8 >= mapPixelWidth - 14*16 - 1)
            return true;
        else
            return false;
    }

    public boolean isTooFarUp()
    {
        int mapHeight = prop.get("height", Integer.class)+1;
        int mapPixelHeight = mapHeight*16;

        if(player.b2body.getPosition().y+8 >= mapPixelHeight - 8*16 + 1)
            return true;
        else
            return false;
    }

    public boolean isTooFarDown()
    {
        if(player.b2body.getPosition().y+8 <= 8*16)
            return true;
        else
            return false;
    }

}
