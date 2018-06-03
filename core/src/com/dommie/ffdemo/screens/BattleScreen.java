package com.dommie.ffdemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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

import java.util.Random;

public class BattleScreen extends GameScreen
{
	Enemy e1;
	Battler b1;
	private boolean playerTurn;
	private boolean itemMenu;
	private Cursor cursor;
	private boolean enemyTurn;
	private boolean battleWon;
	private boolean levelUp;
	private Player.State mapState;
	private Sprite background;
	private boolean gameOver;
	private Sprite youDied;
	private Sprite blackScreen;
	private Sound death;
	private Sound heal;
	private boolean played;
	private int potionCount;
	private int superPotionCount;
	private Random rnd;

	private float animTimer;

	private Vector2 returnPos;

	public BattleScreen(GameInfo game, Vector2 playerPos, Player.State state)
	{
		rnd = new Random();
		levelUp = false;
		potionCount = Integer.parseInt(getLine(4));
		superPotionCount = Integer.parseInt(getLine(5));
		death = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/YouDied.wav"));
		heal = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Battle/Healing.wav"));
		played = false;

		gameOver = false;
		youDied = new Sprite(new Texture("Battle/Death.png"));
		blackScreen = new Sprite(new Texture("Battle/Black.png"));
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
		e1 = new Enemy (world, this, pickEnemy());
		hud = new Hud(gamecam);
		//hud.createTextbox(50, 8, "A " + e1.getName() + " APPEARS!");
		e1.b2body.setTransform(216, 120, 0);
		hud.createBattleMenu(e1, b1);
	}

	private String pickEnemy()
	{
		String s;
		switch(rnd.nextInt(3) + 1)
		{
			case 1:
				s = "Goblin";
				break;
			case 2:
				s = "Wolf";
				break;
			default:
				s = "Unicorn";
				break;
		}
		return s;
	}

	public void setCamera()
	{
		gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
	}


	public void update(float dt)//delta time
	{
		if(!gameOver)
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
				else if(!gameOver)
					progBattle(dt);
			}
		}
		else//if the animation is still happening
		{
			if(!gameOver)
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

		if (!battleWon)
			e1.draw(game.batch);
		b1.draw(game.batch);
		game.batch.end();
		game.hudBatch.begin();
		hud.update(delta);

		hud.draw(game.hudBatch);

		if(playerTurn)
		{
			/*
			if(itemMenu && cursor.getPos() == 3)
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
					cursor.setPos(2);
			else
				cursor.setPos(1);
			*/
			cursor.draw(game.hudBatch);
		}

		game.hudBatch.end();

		game.batch.begin();
		if(gameOver)
		{
			if (animTimer <= 10)
			{
				if(!played)
				{
					death.play();
					played = true;
				}
				animTimer -= delta;
				//blackScreen.setAlpha(1);
				if (animTimer > 0)
				{
					float change = (10 - animTimer) / 10;
					youDied.setScale(change/4 + 0.75f);
					youDied.setAlpha(change);
					//if(change*10/12 <= 1)
						blackScreen.setAlpha(change*10 / 11);
				}
				blackScreen.draw(game.batch);
				youDied.draw(game.batch);
			}
			else
			{
				m.pause();
				m.stop();
				m.dispose();
			}
		}
		game.batch.end();
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
			int addGold = e1.giveGold();
			int addExp = e1.giveExp();
			String output = "YOU WIN!\n\nGOT " + addGold + "G!\n\nGOT " + addExp + " XP!";

			incrementLine(3, addGold);
			int exp = Integer.parseInt(getLine(6));
			exp += addExp;
			int level = Integer.parseInt(getLine(1));
			while(exp >= level*10)//Exp required to level up is 1*current level
			{
				levelUp = true;
				level++;
				exp -= level*10;
			}
			if(levelUp)
				output += "      LEVEL UP! " + level;
			hud.createTextbox(50, 8, output);

			changeLine(6, "" + exp);
			changeLine(1, "" + level);
			//saveValues();
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
				//cursor.playSound();
				switch (cursor.getPos())
				{
					case 1:
						if(!itemMenu)
						{
							attack();
						}
						else if(potionCount > 0 && b1.health < b1.maxHealth)
						{
							heal.play();
							playerTurn = false;
							enemyTurn = true;
							potionCount--;
							hud.createTextbox(50, 8, "YOU GAIN " + 5 + " HP!");
							b1.health += 5;
							if(b1.health > b1.maxHealth)
								b1.health = b1.maxHealth;
							itemMenu = false;
						}
						else
							cursor.incorrect();
						break;
					case 2:
						if(!itemMenu)
						{
							cursor.setPos(1);
							hud.createTextbox(50, 8, " POTION x" + potionCount + "\n\n SUPER POTION x" + superPotionCount + "\n\n EXIT");
							hud.finishText();
							itemMenu = true;
						}
						else if(superPotionCount > 0 && b1.health < b1.maxHealth)
						{
							heal.play();
							playerTurn = false;
							enemyTurn = true;
							superPotionCount--;
							hud.createTextbox(50, 8, "YOU GAIN " + 10 + " HP!");
							b1.health += 10;
							if(b1.health > b1.maxHealth)
								b1.health = b1.maxHealth;
							itemMenu = false;
						}
						else
							cursor.incorrect();
						//heal();
						break;
					case 3:
						if(!itemMenu)
						{
							changeLine(2, "" + b1.health);
							battleWon = true;
							hud.createTextbox(50, 8, "GOT AWAY SAFELY!");
							cursor.hide();
						}
						else
						{
							itemMenu = false;
							hud.createBattleMenu(e1, b1);
						}
					default:
						break;
				}
				//}
			} else if (enemyTurn)//Enemy turn
			{
				//animTimer = 1;
				b1.takeDamage(e1.damage);
				hud.createTextbox(50, 8, "" + e1.getName() + " ATTACKS!\n\nYOU TAKE " + e1.damage + " DAMAGE!");
				enemyTurn = false;
				if(b1.health <= 0)//PLAYER DIES
				{
					gameOver = true;
					animTimer = 12;//7
				}
			} else//RESTART PLAYER TURN
			{
				playerTurn = true;
				hud.createBattleMenu(e1, b1);
			}
		}

	}

	private void attack()
	{
		playerTurn = false;
		enemyTurn = true;
		animTimer = 1.5f;
		//b1.stopFlash();
		System.out.println("SQUADALAAAAA");
		//turnTimer = .1f;
		e1.takeDamage((int)(b1.wepIndex*1.5) + b1.level);
		hud.createTextbox(50, 8, e1.getName() + " TAKES " + ((int)(b1.wepIndex*1.5) + b1.level) + " DAMAGE!");
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}
	public void dispose()
	{
		super.dispose();
		saveValues();
		cursor.dispose();
	}

	private void saveValues()
	{
		changeLine(2, "" + b1.health);
		changeLine(4, "" + potionCount);
		changeLine(5, "" + superPotionCount);
	}
}