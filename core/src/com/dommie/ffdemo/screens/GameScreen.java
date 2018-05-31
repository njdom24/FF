package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

import java.io.*;

public abstract class GameScreen implements Screen, Disposable{
    //Reference to Game, used to set Screens
	private static final int LINE_COUNT = 4;
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

	protected float flashTimer;
	protected float lastFlash;
	protected Sound transitionSound;

	public GameScreen()
	{
		transitionSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Town/EnterTown.wav"));
		flashTimer = -1;
		lastFlash = -1;
	}

    public void changeMap(GameScreen scrn)
    {
        //GameInfo.screens.push(this);
        //GameInfo.currentScreen = m;

        game.setScreen(scrn);
    }
    
    protected void setChangeElements()
    {
    	game.setScreen(this);
    	//GameInfo.currentScreen = this;
    }
    
    public void setToDispose(GameScreen g)
    {
    	prevScreen = g;
    }

    protected void changeLine(int line, String content)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("TempSave.txt"));
			BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
			for(int i = 0; i < LINE_COUNT; i++)
				if(i != line)
				{
					writer.write(reader.readLine());
					writer.newLine();
				}
				else
				{
					reader.readLine();
					writer.write(content);
					writer.newLine();
				}
			reader.close();

			writer.close();
			reader.close();
			writer = new BufferedWriter(new FileWriter("Save.txt"));
			reader = new BufferedReader(new FileReader("TempSave.txt"));

			for(int i = 0; i < LINE_COUNT; i++)
			{
				writer.write(reader.readLine());
				writer.newLine();
			}

			writer.close();
			reader.close();
		}
		catch (IOException e)
		{
		}
	}

	protected void flash()
	{
		m.pause();
		flashTimer = 1;
		lastFlash = 1.1f;
		transitionSound.play();
	}

	public static void incrementLine(int line, int change)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("TempSave.txt"));
			BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
			for(int i = 0; i < LINE_COUNT; i++)
				if(i != line)
				{
					writer.write(reader.readLine());
					writer.newLine();
				}
				else
				{
					writer.write("" + (Integer.parseInt(reader.readLine()) + change));
					writer.newLine();
				}
			reader.close();

			writer.close();
			reader.close();
			writer = new BufferedWriter(new FileWriter("Save.txt"));
			reader = new BufferedReader(new FileReader("TempSave.txt"));

			for(int i = 0; i < LINE_COUNT; i++)
			{
				writer.write(reader.readLine());
				writer.newLine();
			}

			writer.close();
			reader.close();
		}
		catch (IOException e)
		{
		}
	}

	public static String getLine(int line)
	{
		String toReturn = "";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
			for(int i = 0; i < LINE_COUNT; i++)
				if(i != line)
					reader.readLine();
				else
				{
					toReturn = reader.readLine();
					break;
				}
				reader.close();
		}
		catch (IOException e)
		{
		}
		return toReturn;
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
    	if(atlas != null)
    		atlas.dispose();
    	if(npcAtlas != null)
    		npcAtlas.dispose();
    	if(map != null)
    		map.dispose();
        if(renderer != null)
        	renderer.dispose();
        if(b2dr != null)
            b2dr.dispose();
        hud.dispose();
        if(world != null)
        	world.dispose();
        if(m != null)
			m.dispose();
    }

}
