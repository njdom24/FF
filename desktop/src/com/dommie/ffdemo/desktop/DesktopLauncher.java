package com.dommie.ffdemo.desktop;

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
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.resizable = false;

		new LwjglApplication(new GameInfo(), config);
	}
}