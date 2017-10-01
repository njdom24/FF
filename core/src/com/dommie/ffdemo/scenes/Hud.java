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

    private int width;
    private int height;

	public Hud(SpriteBatch sb, OrthographicCamera o)
	{
        gamecam = o;

		viewport = new FitViewport(com.dommie.ffdemo.GameInfo.V_WIDTH, GameInfo.V_HEIGHT, new OrthographicCamera());

		Table table = new Table();//used to organize labels
		table.top();//align at the top of the stage
		table.setFillParent(true);
        //createTextbox(4,3);


	}

	//call this with constructor later when a method to write text without creating a new box is made
	public void createTextbox(int width, int height, String text)//TODO: Create overloaded method for user-specified origin coordinates
    {
		this.width = width;
		this.height = height;

		if(region != null)
		    dispose();

		letters = new Texture("Text/Chars.png");//move to constructor

		int curChar = 0;

        atlas = new TextureAtlas("Text/Textbox.atlas");//move to constructor
        sprites = new Sprite[height][width];
        for(int i = 0; i < height; i++)
            for(int j = 0; j< width; j++)
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
                else
				{
					sprites[i][j] = new Sprite(atlas.findRegion("Blank"));

					if(curChar < text.length())
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
					}
					//put text sprites
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

        region.getTexture().dispose();
        atlas.dispose();
        letters.dispose();
    }

}
