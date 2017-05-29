package com.dommie.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.dommie.mariobros.MarioBros;
import com.dommie.mariobros.scenes.Hud;
import com.dommie.mariobros.sprites.Mario;
import com.dommie.mariobros.sprites.NPC;
import com.dommie.mariobros.sprites.TestBody;
import com.dommie.mariobros.tools.B2WorldCreator;
import com.dommie.mariobros.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public abstract class MapScreen extends GameScreen{
    //Reference to Game, used to set Screens
    protected TextureAtlas mapAtlas;

    protected OrthographicCamera gamecam;//what the viewport displays

    //Tiled map variables
    protected TmxMapLoader maploader;
    protected MapProperties prop;

    //Box2d variables
    protected Mario player;

    //Movement variables
    protected boolean isMoving = false;
    protected Vector2 originalPos;
    public Set<String> mapCollisions;

    private TestBody t;

    private ArrayList<NPC> npcs;

    public MapScreen(MarioBros game, String mapName, float locX, float locY)
    {
        atlas = new TextureAtlas("overworld_jobs.atlas");
        this.game = game;
        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);
        hud = new Hud(game.batch, gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load(mapName);
        prop = map.getProperties();
        renderer = new OrthogonalTiledMapRenderer(map, 1/MarioBros.PPM);


        world = new World(new Vector2(0, 0), true);//sets gravity properties
        b2dr = new Box2DDebugRenderer();
        //map.getLayers().get(0).getObjects().get(0).

        player = new Mario(world, this);
        player.b2body.setTransform(locX, locY, 0);//world entrance location

        setCamera();

        npcs = new B2WorldCreator(world, map).createGenericNPCs(new String[0]);
        world.setContactListener(new WorldContactListener());
        mapCollisions = new TreeSet<String>();
        WorldContactListener.currentCollisions = mapCollisions;
        WorldContactListener.player = player;
        WorldContactListener.npcs = npcs;

        t = new TestBody(world, map);
    }

    public void changeMap(Screen m)
    {
        MarioBros.screens.push(this);
        game.setScreen(m);
    }

    public void revertMap()
    {
        Screen futureScreen = MarioBros.screens.pop();

        if(futureScreen instanceof MapScreen)
            WorldContactListener.currentCollisions = ((MapScreen) futureScreen).mapCollisions;

        game.setScreen(futureScreen);
    }

    public void setCamera()
    {
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        //changes inital camera placement based on initial player position
        if(isTooFarLeft())
            gamecam.position.set(13.5f*16/MarioBros.PPM, gamecam.position.y, 0);
        else
        if(isTooFarRight())
            gamecam.position.set((prop.get("width", Integer.class)-13.5f)*16f/MarioBros.PPM, gamecam.position.y, 0);
        if(isTooFarUp())
            gamecam.position.set(gamecam.position.x, (prop.get("height", Integer.class)-7.5f)*16f/MarioBros.PPM, 0);
        else
        if(isTooFarDown())
            gamecam.position.set(gamecam.position.x, 7.5f*16/MarioBros.PPM, 0);
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
        for(NPC n : npcs)
        {
            n.update(dt);
        }

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
        hud.draw(game.batch);
        game.batch.end();

        //set batch to draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private boolean smallDifference(float a, float b)
    {
        System.out.println("REE: " + (Math.abs(a-b)));
        return (Math.abs(a-b) <= 0.01);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    public boolean isTooFarLeft()
    {
        if(player.b2body.getPosition().x*MarioBros.PPM+8 <= 14*16)
            return true;
        else
            return false;
    }

    public boolean isTooFarRight()
    {
        int mapWidth = prop.get("width", Integer.class)+1;
        int mapPixelWidth = mapWidth*16;

        if(player.b2body.getPosition().x*MarioBros.PPM+8 >= mapPixelWidth - 14*16)
            return true;
        else
            return false;
    }

    public boolean isTooFarUp()
    {
        int mapHeight = prop.get("height", Integer.class)+1;
        int mapPixelHeight = mapHeight*16;

        if(player.b2body.getPosition().y*MarioBros.PPM+8 >= mapPixelHeight - 8*16)
            return true;
        else
            return false;
    }

    public boolean isTooFarDown()
    {
        if(player.b2body.getPosition().y*MarioBros.PPM+8 <= 8*16)
            return true;
        else
            return false;
    }

}
