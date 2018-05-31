package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private boolean itemMenu;
	private Cursor cursor;
	private boolean enemyTurn;
	private boolean battleWon;
	private Player.State mapState;
	private Sprite background;

	private float animTimer;

	private Vector2 returnPos;

	public BattleScreen(GameInfo game, Vector2 playerPos, Player.State state)
	{
		itemMenu = false;
		background = new Sprite(new Texture("Battle/ForestBG.png"));
		//background.scale(2);
		background.setPosition(0,GameInfo.V_HEIGHT-32);
		m = Gdx.audio.newMusic(Gdx.files.internal("Music/Battle/Last Surprise.ogg"));
		m.setLooping(true);
		m.setVolume(0.2f);
		//m.play();

		animTimer = -1;
		returnPos = playerPos;
		mapState = state;
		//TODO: Take reference to MapScreen to transition back
		playerTurn = true;
		enemyTurn = false;
		cursor = new Cursor(-201, -77);
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
		hud.createBattleMenu(e1, b1);
	}

	public void setCamera()
	{
		gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
	}


	public void update(float dt)//delta time
	{
		m.play();
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
		game.hudBatch.setProjectionMatrix(gamecam.projection);

		game.batch.begin();
		background.draw(game.batch);
		if(!battleWon)
			e1.draw(game.batch);
		b1.draw(game.batch);
		game.batch.end();

		game.hudBatch.begin();
		hud.update(delta);
		hud.draw(game.hudBatch);

		if(playerTurn)
		{
			if(itemMenu && cursor.getPos() == 3)
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
					cursor.setPos(2);
			else
				cursor.setPos(1);
			cursor.draw(game.hudBatch);
		}

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

		else if(e1.getHealth() <= 0)
		{
			m.pause();
			m.dispose();
			m = Gdx.audio.newMusic(Gdx.files.internal("Music/Battle/Fanfare.ogg"));
			m.setVolume(0.3f);
			m.setLooping(true);
			hud.createTextbox(50, 8, "YOU WIN!\n\nGOT 10G!");
			incrementLine(3, 10);
			changeLine(2, "" + b1.health);
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
				System.out.println("YEEEET");
				b1.stopFlash();
				switch (cursor.getPos())
				{
					case 1:
						playerTurn = false;
						enemyTurn = true;
						if(!itemMenu)
						{
							attack();
						}
						else
						{
							hud.createTextbox(50, 8, "YOU GAIN " + 5 + " HP!");
							b1.health += 5;
							itemMenu = false;
						}
						break;
					case 2:
						if(!itemMenu)
						{
							cursor.setPos(1);
							hud.createTextbox(50, 8, " POTION xINF.\n\n EXIT");
							hud.finishText();
							itemMenu = true;
						}
						else
						{
							itemMenu = false;
							hud.createBattleMenu(e1, b1);
						}
						//heal();
						break;
					case 3:
						changeLine(2, "" + b1.health);
						battleWon = true;
						hud.createTextbox(50, 8, "GOT AWAY SAFELY!");
						cursor.hide();
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
				hud.createBattleMenu(e1, b1);
			}
		}

	}

	private void attack()
	{
		animTimer = 1.5f;
		//b1.stopFlash();
		System.out.println("SQUADALAAAAA");
		//turnTimer = .1f;
		e1.takeDamage(b1.wepIndex);
		hud.createTextbox(50, 8, e1.getName() + " TAKES " + b1.wepIndex + " DAMAGE!");
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}
	public void dispose()
	{
		super.dispose();
	}
}