package com.dommie.mariobros.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.dommie.mariobros.MarioBros;

public class Hud implements Disposable
{
	public Stage stage;
	private Viewport viewport;//separate from world viewport

	private Integer worldTimer;
	private float timeCount;
	private Integer score;

	Label countdownLabel;
	Label scoreLabel;
	Label timeLabel;
	Label levelLabel;
	Label worldLabel;
	Label marioLabel;
    //private Sprite s;
    private TextureRegion region;
    private Sprite[][] sprites;
    private OrthographicCamera gamecam;

	public Hud(SpriteBatch sb, OrthographicCamera o)
	{
        gamecam = o;
		worldTimer = 300;
		timeCount = 0;
		score = 0;

		viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		Table table = new Table();//used to organize labels
		table.top();//align at the top of the stage
		table.setFillParent(true);

		//countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));//3 digits in countdown timer. d = integer
		//scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		//timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		//levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		//worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		//marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		//top row
		table.add(marioLabel).expandX().padTop(10);
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);

		table.row();

		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(countdownLabel).expandX();

		stage.addActor(table);

        createTextbox(4,3);
	}

	public void createTextbox(int width, int height)
    {
        TextureAtlas atlas = new TextureAtlas("Text/Text.atlas");
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
                    sprites[i][j] = new Sprite(atlas.findRegion("Blank"));
            }
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                region = new TextureRegion(sprites[i][j].getTexture(), sprites[i][j].getRegionX(), sprites[i][j].getRegionY(), 16, 16);
                sprites[i][j].setBounds(0, 0, 16/MarioBros.PPM, 16/MarioBros.PPM);
                sprites[i][j].setRegion(region);
                sprites[i][j].setPosition((gamecam.position.x*100 - width*16/2 + j*16)/MarioBros.PPM, (gamecam.position.y*100 - MarioBros.V_HEIGHT/2 + height*16 - (i+1)*16)/MarioBros.PPM);
            }
    }

    public void draw(SpriteBatch sb){
        for(Sprite[] sprite : sprites)
            for(Sprite s : sprite)
                s.draw(sb);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        for(Sprite[] sprite : sprites)
            for(Sprite s : sprite)
                s.getTexture().dispose();
        //clears by resetting the sprite array
        createTextbox(0,0);
    }
}
