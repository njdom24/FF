package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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
import com.dommie.ffdemo.sprites.Player;

public class BattleScreen extends GameScreen
{
	Enemy e1;
	Battler b1;
	private boolean playerTurn;
	private Cursor cursor;
	private boolean enemyTurn;
	private boolean battleWon;
	private Player.State mapState;

	private Music fanfare;
	private float animTimer;

	private Vector2 returnPos;

	public BattleScreen(GameInfo game, Vector2 playerPos, Player.State state)
	{
		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Battle/Last Surprise.ogg"));
		m.setLooping(true);
		m.setVolume(0.2f);
		m.play();
		fanfare = Gdx.audio.newMusic(Gdx.files.internal("Music/Battle/Fanfare.ogg"));
		fanfare.setLooping(true);
		fanfare.setVolume(0.3f);

		animTimer = -1;
		returnPos = playerPos;
		mapState = state;
		//TODO: Take reference to MapScreen to transition back
		playerTurn = true;
		enemyTurn = false;
		cursor = new Cursor(8, 16);
		b2dr = new Box2DDebugRenderer();
		atlas = new TextureAtlas("Battle/Players/BattleSprites.atlas");
		this.game = game;
		gamecam = new OrthographicCamera();
		battleWon = false;

		gamePort = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, com.dommie.ffdemo.GameInfo.V_HEIGHT, gamecam);
		renderer = new OrthogonalTiledMapRenderer(map);

		world = new World(new Vector2(0, 0), true);//sets gravity properties

		b1 = new Battler(world, "RedMage", this);
		//b1.b2body.setTransform(216, 120, 0);
		e1 = new Enemy (world, this, 2, "GOBLIN");
		hud = new Hud(gamecam);
		//hud.createTextbox(50, 8, "A " + e1.getName() + " APPEARS!");
		e1.b2body.setTransform(216, 120, 0);
		hud.createBattleMenu(e1);
	}

	public void setCamera()
	{
		gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
	}


	public void update(float dt)//delta time
	{
		boolean pressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
		//if(justPaused)
		//	justPaused = false;
		setCamera();
		//handleInput(dt);
		e1.update(dt);
		b1.update(dt);
		if (prevScreen != null) {
			prevScreen.dispose();
			prevScreen = null;
			System.out.println("disposed");
		}

		world.step(1 / 60f, 6, 2);
		cursor.update(dt);
		gamecam.update();
		renderer.setView(gamecam);

		if(animTimer < 0)
		{
			b1.b2body.setLinearVelocity(0,0);
			if (pressed)
			{
				if (playerTurn && hud.isFinished())
				{
					//hud.createBattleMenu(e1);
					//turnTimer = .1f;
				}
				if (!hud.isFinished())
					hud.finishText();
				else
					progBattle(dt);
			}
		}
		else//if the animation is still happening
		{
			b1.animate(animTimer, dt);
			animTimer -= dt;
		}

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
		if(!battleWon)
			e1.draw(game.batch);
		b1.draw(game.batch);
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
		if(battleWon)
		{
			Overworld m = new Overworld(game, returnPos.x, returnPos.y, mapState);
			m.setToDispose(this);
			b1.dispose();
			e1.dispose();
			changeMap(m);
		}

		else if(e1.getHealth() == 0)
		{
			m.pause();
			fanfare.play();
			hud.createTextbox(50, 8, "YOU WIN!");
			battleWon = true;
			b1.battleWon = true;
		}

		else
		{
			//System.out.println("Timer: " + turnTimer);
			System.out.println("PlayerTurn: " + playerTurn);

			if (playerTurn)
			{
				//if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				//{
				//turnTimer = 0.1f;
				playerTurn = false;
				enemyTurn = true;
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
				//}
			} else if (enemyTurn)//Enemy turn
			{
				//animTimer = 1;
				b1.takeDamage();
				hud.createTextbox(50, 8, "" + e1.getName() + " ATTACKS!\n\nYOU TAKE " + 1 + " DAMAGE!");
				enemyTurn = false;
			} else//RESTART PLAYER TURN
			{
				playerTurn = true;
				hud.createBattleMenu(e1);
			}
		}

	}

	private void attack()
	{
		animTimer = 1.5f;
		b1.stopFlash();
		System.out.println("SQUADALAAAAA");
		//turnTimer = .1f;
		e1.takeDamage(1);
		hud.createTextbox(50, 8, e1.getName() + " TAKES " + 1 + " DAMAGE!");//need to put the battle menu back up after this
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}
	public void dispose()
	{
		super.dispose();
		fanfare.dispose();
	}
}