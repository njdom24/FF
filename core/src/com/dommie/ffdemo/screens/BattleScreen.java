package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.scenes.Cursor;
import com.dommie.ffdemo.scenes.Hud;
import com.dommie.ffdemo.sprites.Battler;
import com.dommie.ffdemo.sprites.Enemy;
import com.dommie.ffdemo.sprites.TestBody;

public class BattleScreen extends GameScreen
{
	Enemy e1;
	Battler b1;
	private TestBody t;
	private boolean playerTurn;
	private Cursor cursor;
	private float turnTimer;

	public BattleScreen(GameInfo game, float locX, float locY)
	{
		//TODO: Take reference to MapScreen to transition back
		turnTimer = 0;;
		playerTurn = false;
		cursor = new Cursor(8, 16);
		b2dr = new Box2DDebugRenderer();
		atlas = new TextureAtlas("Battle/Players/BattleSprites.atlas");
		this.game = game;
		gamecam = new OrthographicCamera();

		gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
		renderer = new OrthogonalTiledMapRenderer(map);

		world = new World(new Vector2(0, 0), true);//sets gravity properties

		//b1 = new Battler(world, this);
		//b1.b2body.setTransform(216, 120, 0);
		e1 = new Enemy (world, this, 2, "GOBLIN");
		hud = new Hud(gamecam);
		hud.createTextbox(50, 8, "A " + e1.getName() + " APPEARS!");
		e1.b2body.setTransform(216, 120, 0);
		//hud.createBattleMenu();
	}

	public void setCamera()
	{
		gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
	}


	public void update(float dt)//delta time
	{
		//if(justPaused)
		//	justPaused = false;
		setCamera();
		//handleInput(dt);
		e1.update(dt);
		if (prevScreen != null) {
			prevScreen.dispose();
			prevScreen = null;
			System.out.println("disposed");
		}

		world.step(1 / 60f, 6, 2);
		cursor.update(dt);
		gamecam.update();
		renderer.setView(gamecam);
		if(!playerTurn && hud.isFinished() && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			playerTurn = true;
			hud.createBattleMenu(e1);
			turnTimer = .1f;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			hud.finishText();
		progBattle(dt);
	}

	@Override
	public void render(float delta) {
		//update separately from render

		update(delta);
		//clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		b2dr.render(world, gamecam.combined);

		//set batch to draw what the Hud camera sees
		game.batch.setProjectionMatrix(gamecam.combined);
		//game.hudBatch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();
		e1.draw(game.batch);
		game.batch.end();

		game.hudBatch.begin();
		hud.update(delta);
		hud.draw(game.hudBatch);

		if(playerTurn)
			cursor.draw(game.hudBatch);

		game.hudBatch.end();
	}

	private void progBattle(float dt)
	{
		//System.out.println("Timer: " + turnTimer);
		//System.out.println("PlayerTurn: " + playerTurn);
		if(turnTimer <= 0)
		{
			turnTimer = 0;
			if (playerTurn)
				if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				{
					System.out.println("YEEEET");
					switch (cursor.getPos())
					{
						case 3:
							attack();
							break;
						case 2:
							//heal();
							break;
						case 1:
							System.out.println("No running!");
						default:
							break;
					}
				}
		}
		else
		{
			turnTimer -= dt;
		}

	}

	private void attack()
	{
		System.out.println("SQUADALAAAAA");
		playerTurn = false;
		//turnTimer = .1f;
		e1.takeDamage(1);
		hud.createTextbox(50, 8, e1.getName() + " TAKES " + 1 + " DAMAGE!");//need to put the battle menu back up after this
	}


	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}
}
