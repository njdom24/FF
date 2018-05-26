package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.dommie.ffdemo.tools.B2WorldCreator;
import com.dommie.ffdemo.tools.WorldContactListener;

import java.util.ArrayList;

public abstract class MapScreen extends GameScreen{
    //Reference to Game, used to set Screens

    protected OrthographicCamera gamecam;//what the viewport displays

    //Tiled map variables
    protected TmxMapLoader maploader;
    protected MapProperties prop;

    //Box2d variables
    protected Player player;

    private TestBody t;
    //private boolean justPaused;

	protected B2WorldCreator creator;
    protected ArrayList<NPC> npcs;
    protected int[][] collisions;
    private NPC speakingNPC;

    private boolean paused;
    public MapScreen(GameInfo game, String mapName, float locX, float locY, boolean isOverworld)
    {
    	//justPaused = false;
    	paused = false;

        atlas = new TextureAtlas("overworld_jobs_2.atlas");
        this.game = game;
        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
        hud = new Hud(gamecam);

        maploader = new TmxMapLoader();
        map = maploader.load(mapName);
        prop = map.getProperties();
        renderer = new OrthogonalTiledMapRenderer(map);

        world = new World(new Vector2(0, 0), true);//sets gravity properties
        b2dr = new Box2DDebugRenderer();

        player = new Player(world, this, isOverworld);
        player.b2body.setTransform(locX, locY, 0);//world entrance location

        setCamera();

        world.setContactListener(new WorldContactListener());
        WorldContactListener.player = player;
        //GameInfo.currentScreen = this;

        //hud.createTextbox(23, 5, "Good ol' fashioned\ntest.");//just for demo, should only me put in child classes for specific npcs

		//t = new TestBody(world);
    }

    public void changeMap(GameScreen m)
    {
    	super.changeMap(m);
    	player.b2body.setTransform(player.getIntendedPos(), 0);
    }

    protected void setChangeElements()
    {
    	super.setChangeElements();

    	WorldContactListener.player = player;
    	WorldContactListener.npcs = npcs;

    	//System.out.println("Baws");
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
    	boolean justPaused = false;
    	if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !player.isMoving())
		{
			if(!paused)
				if(npcs != null && !npcs.isEmpty())
					for (NPC n : npcs)//Checks if player is interacting with an NPC
					{
						paused = n.playerIsAdjacent(collisions, player.getState());
						if (paused)
						{
							justPaused = true;
							speakingNPC = n;
							hud = new Hud(gamecam, n.getMessages());//Creates a text box from NPC's dialogue
							hud.displayMessage();
						}
						break;
					}
			System.out.println("Foddy");
			if(!justPaused)
			{
				if(!hud.isFinished())
					hud.finishText();
				else
					if (speakingNPC != null && !hud.advanceText())
					{
						speakingNPC = null;
						hud.quitText();
						paused = false;
					}
			}
		}
    }

    public void update(float dt)//delta time
    {
		//if(justPaused)
		//	justPaused = false;

		handleInput(dt);
    	if(!paused)
    	{
			player.update(dt);
			//t.update(dt);

			if (prevScreen != null) {
				prevScreen.dispose();
				prevScreen = null;
				System.out.println("disposed");
			}

			world.step(1 / 60f, 6, 2);


			if (!isTooFarLeft() && !isTooFarRight())
				gamecam.position.x = player.b2body.getPosition().x;
			if (!isTooFarUp() && !isTooFarDown())
				gamecam.position.y = player.b2body.getPosition().y;

			gamecam.update();
			renderer.setView(gamecam);
		}
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

        //set batch to draw what the Hud camera sees
        game.batch.setProjectionMatrix(gamecam.combined);

        //game.hudBatch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);

        if(npcs != null && !npcs.isEmpty())
			for(NPC n : npcs)
			{
				if(!paused)
					n.update(delta);
				n.draw(game.batch);
			}

        game.batch.end();

        game.hudBatch.begin();
        hud.update(delta);
        hud.draw(game.hudBatch);
        game.hudBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public boolean isTooFarLeft()
    {
		return player.b2body.getPosition().x + 8 <= 14 * 16;
    }

    public boolean isTooFarRight()
    {
        int mapWidth = prop.get("width", Integer.class)+1;
        int mapPixelWidth = mapWidth*16;

		return player.b2body.getPosition().x + 8 >= mapPixelWidth - 14 * 16 - 1;
    }

    public boolean isTooFarUp()
    {
        int mapHeight = prop.get("height", Integer.class)+1;
        int mapPixelHeight = mapHeight*16;

		return player.b2body.getPosition().y + 8 >= mapPixelHeight - 8 * 16 + 1;
    }

    public boolean isTooFarDown()
    {
		return player.b2body.getPosition().y + 8 <= 8 * 16;
    }

    public void dispose()
    {
    	super.dispose();
    	player.dispose();

    	for(NPC n : npcs)
    		n.dispose();

    }

}
