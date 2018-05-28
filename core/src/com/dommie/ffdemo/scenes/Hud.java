package com.dommie.ffdemo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dommie.ffdemo.GameInfo;
import com.dommie.ffdemo.sprites.Enemy;

public class Hud implements Disposable
{
	private Viewport viewport;//separate from world viewport

    //private Sprite s;
    private TextureRegion region;
    private Sprite[][] sprites;
    private OrthographicCamera gamecam;
    private TextureAtlas atlas;
    private String message;
	private String[] messages;
	private Texture letters;
	private float time;

    private int width;
    private int height;

    private int messageIndex;
    private int curChar;
    private int curX;
    private int curY;
    private boolean done;
    private boolean playText;
    float scale;
    //float offset;

	private Sound textSound;

	public Hud(OrthographicCamera o)
	{
		//String[] s1 = {" "};
		this(o, null);
	}

	public Hud(OrthographicCamera o, String[] texts)
	{
		textSound = Gdx.audio.newSound(Gdx.files.internal("Music/SFX/Text/text.wav"));
		playText = true;
		scale = LwjglApplicationConfiguration.getDesktopDisplayMode().width /com.dommie.ffdemo.GameInfo.V_WIDTH;
		//offset = com.dommie.ffdemo.GameInfo.V_WIDTH/2 - (LwjglApplicationConfiguration.getDesktopDisplayMode().width - (com.dommie.ffdemo.GameInfo.V_WIDTH * scale));
		//System.out.println("OFFSETTTT: " + offset);
		//scale = 1;
		messages = texts;
		curChar = 0;
		done = false;
        gamecam = o;
        curX = 1;
        curY = 1;
		letters = new Texture("Text/Chars.png");
		atlas = new TextureAtlas("Text/Textbox.atlas");

		viewport = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, GameInfo.V_HEIGHT, gamecam);

		Table table = new Table();//used to organize labels
		table.top();//align at the top of the stage
		table.setFillParent(true);
        //createTextbox(4,3);
		createTextbox(0,0, "");
		done = true;
	}

	public void update(float dt)
	{
		if(!done)
		{
			if (message.charAt(curChar) == '\n') {
				curChar++;
				curX = 1;
				curY++;
				time = 0;
				dt = 0;
			}
			time += dt;
			if (time >= 0.1)//Update next character
			{
				char toPlace = message.charAt(curChar);
				curChar++;

				if(toPlace != ' ' && playText)
				{
					textSound.play();
					//System.out.println("Played");
				}

				if (toPlace >= 65 && toPlace <= 74)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 65), 0, 8, 8));//Row 1 (A-J)
				else if (toPlace >= 75 && toPlace <= 85)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 75), 8, 8, 8));//Row 2 (K-U)
				else if (toPlace >= 86 && toPlace <= 90)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 86), 16, 8, 8));//Row 3 (V-Z)
				else if (toPlace >= 97 && toPlace <= 101)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * ((toPlace - 97) + 5), 16, 8, 8));//Row 3 (a-e)
				else if (toPlace >= 102 && toPlace <= 111)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 102), 24, 8, 8));//Row 4 (f-o)
				else if (toPlace >= 112 && toPlace <= 121)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 112), 32, 8, 8));//Row 5 (p-y)
				else if (toPlace == 122)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 0, 40, 8, 8));//Row 6 (z)
				else if (toPlace == 45)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8, 40, 8, 8));//Row 6 (-)
				else if (toPlace == 34)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 16, 40, 8, 8));//Row 6 (")
				else if (toPlace == 33)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 24, 40, 8, 8));//Row 6 (!)
				else if (toPlace == 63)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 32, 40, 8, 8));//Row 6 (?)
				else if (toPlace == 39)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 40, 40, 8, 8));//Row 6 (')
				else if (toPlace == 44)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 48, 40, 8, 8));//Row 6 (,)
				else if (toPlace == 46)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 56, 40, 8, 8));//Row 6 (.)
				else if (toPlace >= 48 && toPlace <= 57)
					sprites[curY][curX] = new Sprite(new TextureRegion(letters, 8 * (toPlace - 48), 48, 8, 8));//Row 7 (0-9)

				//sprites[curY][curX].setPosition(((int)(gamecam.position.x+0.1)/16 - width*4 + curX * 8 - 16), ((int)(gamecam.position.y+0.1)/16 - (curY+2) * 8));
				sprites[curY][curX].setScale(scale);
				//sprites[curY][curX].setPosition(((com.dommie.ffdemo.GameInfo.V_WIDTH/2 - width * 8 / 2) + scale + curX * 8) * scale, (height * 8 - (curY + 0.65f) * 8) * scale);
				sprites[curY][curX].setPosition(((com.dommie.ffdemo.GameInfo.V_WIDTH/2 - width * 8 / 2) + curX * 8 + 2) * scale, (height * 8 - (curY) * 8) * scale - 20);

				if (curChar == message.length())
					done = true;
				else//move position
				{
					if (curX == width - 2)//end of line
					{
						curX = 1;
						curY++;
					} else
						curX++;
				}
				time = 0;
			}
		}
	}

	public void displayMessage()
	{
		createTextbox(23, 5, messages[messageIndex++]);
	}

	public boolean advanceText()
	{
		if(messageIndex < messages.length)
		{
			displayMessage();
			return true;
		}
		messageIndex = 0;
		return false;
	}

	public boolean isFinished()
	{
		return done;
	}

	public void finishText()
	{
		playText = false;
		while(!done)
			update(5);
		playText = true;
	}

	public void quitText()
	{
		System.out.println("QUIT TEXT");
		done = true;

		//textSound.play();
		destroy();

		sprites = new Sprite[0][0];
	}


	//call this with constructor later when a method to write text without creating a new box is made
	public void createTextbox(int width, int height, String text)//TODO: Create overloaded method for user-specified origin coordinates
    {
    	curChar = 0;
    	done = false;
    	curX = 1;
    	curY = 1;
    	message = text;
		this.width = width;
		this.height = height;

		destroy();

		letters = new Texture("Text/Chars.png");
        atlas = new TextureAtlas("Text/Textbox.atlas");
        sprites = new Sprite[height][width];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                if(i == 0 && j == 0)
                    sprites[i][j] = new Sprite(atlas.findRegion("UL"));
                else if(i == 0 && j == width-1)
                    sprites[i][j] = new Sprite(atlas.findRegion("UR"));
                else if(i == height-1 && j == 0)
                    sprites[i][j] = new Sprite(atlas.findRegion("DL"));
                else if(i == height-1 && j == width-1)
                    sprites[i][j] = new Sprite(atlas.findRegion("DR"));
                else if(i == 0)
                    sprites[i][j] = new Sprite(atlas.findRegion("U"));
                else if(i == height-1)
                    sprites[i][j] = new Sprite(atlas.findRegion("D"));
                else if(j == 0)
                    sprites[i][j] = new Sprite(atlas.findRegion("L"));
                else if(j == width-1)
                    sprites[i][j] = new Sprite(atlas.findRegion("R"));
                else//sprite to be displayed is not a border
				{
					sprites[i][j] = new Sprite(atlas.findRegion("Blank"));
				}
            }
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                region = new TextureRegion(sprites[i][j].getTexture(), sprites[i][j].getRegionX(), sprites[i][j].getRegionY(), 8, 8);
                sprites[i][j].setBounds(0, 0, 8, 8);
                sprites[i][j].setScale(scale);
                sprites[i][j].setRegion(region);
				//sprites[i][j].setPosition(((int)(gamecam.position.x+0.1)/16 - width*4 + j * 8 - 16), ((int)(gamecam.position.y+0.1)/16 - (i+2) * 8));
                sprites[i][j].setPosition(((com.dommie.ffdemo.GameInfo.V_WIDTH/2 - width*8/2) + j*8 + 2) * scale, (height*8 -(i)*8) * scale - 20);
            }
    }

    public void createBattleMenu(Enemy e)
	{
		createTextbox(50, 8, " FIGHT           " + e.getName() + "\n\n ITEM              " + e.getHealth() + "\n\n RUN");
		finishText();
	}


    public void draw(SpriteBatch sb)
    {
        for(Sprite[] sprite : sprites)
            for(Sprite s : sprite)
                s.draw(sb);
    }

    private void destroy()
	{
		atlas.dispose();
		letters.dispose();
	}

    @Override
    public void dispose()
    {
        /*
        for(Sprite[] sprite : sprites)
            for(Sprite s : sprite)
                s.getTexture().dispose();
        */

        //region.getTexture().dispose();
        destroy();
        textSound.dispose();
    }

}
