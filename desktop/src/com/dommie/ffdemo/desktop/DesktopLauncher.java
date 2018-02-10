package com.dommie.ffdemo.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dommie.ffdemo.GameInfo;

public class DesktopLauncher {
	public static void main (String[] args)
	{
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 432;
        config.height = 240;

        //Ensures pixel-perfect (integer) scaling
		config.width = (LwjglApplicationConfiguration.getDesktopDisplayMode().width / com.dommie.ffdemo.GameInfo.V_WIDTH) * com.dommie.ffdemo.GameInfo.V_WIDTH;
		config.height = (LwjglApplicationConfiguration.getDesktopDisplayMode().width / com.dommie.ffdemo.GameInfo.V_WIDTH) * GameInfo.V_HEIGHT;
		config.resizable = false;
		//config.fullscreen = true;

		config.addIcon("Icon/128.png", Files.FileType.Internal);
		config.addIcon("Icon/32.png", Files.FileType.Internal);
		config.addIcon("Icon/16.png", Files.FileType.Internal);

		new LwjglApplication(new GameInfo(), config);
	}
}