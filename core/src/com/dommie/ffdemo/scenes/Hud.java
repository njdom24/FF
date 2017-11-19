package com.dommie.ffdemo.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dommie.ffdemo.GameInfo;

public class Hud implements Disposable
{
	private Viewport viewport;//separate from world viewport

    //private Sprite s;
    private TextureRegion region;
    private Sprite[][] sprites;
    private OrthographicCamera gamecam;
    private TextureAtlas atlas;
    private String message;//eventually make array
	private Texture letters;
	private float time;

    private int width;
    private int height;

    private int curChar;
    private int curX;
    private int curY;
    private boolean done;

	public Hud(SpriteBatch sb, OrthographicCamera o)
	{
		curChar = 0;
		done = false;
        gamecam = o;
        curX = 1;
        curY = 1;
		letters = new Texture("Text/Chars.png");
		atlas = new TextureAtlas("Text/Textbox.atlas");

		viewport = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, GameInfo.V_HEIGHT, new OrthographicCamera());

		Table table = new Table();//used to organize labels
		table.top();//align at the top of the stage
		table.setFillParent(true);
        //createTextbox(4,3);


	}

	public void update(float dt)
	{
		if(!done)
		{
			if (message.charAt(curChar) == '\n') {
				curChar++;
				curX = 1;
				curY++;
			}
			time += dt;
			if (time >= 0.1)//Update next character
			{
				char toPlace = message.charAt(curChar);
				curChar++;

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

				sprites[curY][curX].setPosition(((com.dommie.ffdemo.GameInfo.V_WIDTH - width * 8) / 2 + curX * 8), (height * 8 - (curY + 1) * 8));

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

	//call this with constructor later when a method to write text without creating a new box is made
	public void createTextbox(int width, int height, String text)//TODO: Create overloaded method for user-specified origin coordinates
    {
    	done = false;
    	curX = 1;
    	curY = 1;
    	message = text;
		this.width = width;
		this.height = height;

		dispose();

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

					/*
					if(curChar < text.length())//sprite to be displayed is a character
					{
						if(text.charAt(curChar) >= 65 && text.charAt(curChar) <= 74)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 65), 0, 8, 8));//Row 1 (A-J)
						else if(text.charAt(curChar) >= 75 && text.charAt(curChar) <= 85)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 75), 8, 8, 8));//Row 2 (K-U)
						else if(text.charAt(curChar) >= 86 && text.charAt(curChar) <= 90)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 86), 16, 8, 8));//Row 3 (V-Z)
						else if(text.charAt(curChar) >= 97 && text.charAt(curChar) <= 101)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*((text.charAt(curChar) - 97)+5), 16, 8, 8));//Row 3 (a-e)
						else if(text.charAt(curChar) >= 102 && text.charAt(curChar) <= 111)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 102), 24, 8, 8));//Row 4 (f-o)
						else if(text.charAt(curChar) >= 112 && text.charAt(curChar) <= 121)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 112), 32, 8, 8));//Row 5 (p-y)
						else if(text.charAt(curChar) == 122)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 0, 40, 8, 8));//Row 6 (z)
						else if(text.charAt(curChar) == 45)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8, 40, 8, 8));//Row 6 (-)
						else if(text.charAt(curChar) == 34)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 16, 40, 8, 8));//Row 6 (")
						else if(text.charAt(curChar) == 33)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 24, 40, 8, 8));//Row 6 (!)
						else if(text.charAt(curChar) == 63)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 32, 40, 8, 8));//Row 6 (?)
						else if(text.charAt(curChar) == 39)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 40, 40, 8, 8));//Row 6 (')
						else if(text.charAt(curChar) == 44)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 48, 40, 8, 8));//Row 6 (,)
						else if(text.charAt(curChar) == 46)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 56, 40, 8, 8));//Row 6 (.)
						else if(text.charAt(curChar) >= 48 && text.charAt(curChar) <= 57)
							sprites[i][j] = new Sprite(new TextureRegion(letters, 8*(text.charAt(curChar) - 48), 48, 8, 8));//Row 7 (0-9)


						curChar++;

					}*/
				}
            }
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                region = new TextureRegion(sprites[i][j].getTexture(), sprites[i][j].getRegionX(), sprites[i][j].getRegionY(), 8, 8);
                sprites[i][j].setBounds(0, 0, 8, 8);
                sprites[i][j].setRegion(region);
                sprites[i][j].setPosition(((com.dommie.ffdemo.GameInfo.V_WIDTH - width*8)/2 + j*8), (height*8 -(i+1)*8));
            }
    }

    public void draw(SpriteBatch sb)
    {
        for(Sprite[] sprite : sprites)
            for(Sprite s : sprite)
                s.draw(sb);
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
        atlas.dispose();
        letters.dispose();
    }

}
