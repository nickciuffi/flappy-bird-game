package com.gcstudios.world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Pipe;
import com.gcstudios.entities.Player;
import com.gcstudios.main.Game;

public class World {
	
	public static int TILE_SIZE = 16;

	public World(){
		
	}
	
	public static GraphicsConfiguration getDefaultConfiguration() {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    return gd.getDefaultConfiguration();
	}
	
	public static BufferedImage rotate(BufferedImage image, double angle) {
	    int w = image.getWidth(), h = image.getHeight();
	    GraphicsConfiguration gc = getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(w, h);
	    Graphics2D g = result.createGraphics();
	    g.rotate(Math.toRadians(angle), w / 2, h / 2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    return result;
	}
	
	public static void restart() {
		Game.player = new Player(40,Game.HEIGHT/2-16,16,16, 1, Game.spritesheet.getSprite(16, 0,16,16));
		
		Game.entities = new ArrayList<Entity>();
		Game.pipes = new ArrayList<Pipe>();
		
		Game.entities.add(Game.player);
		Game.pontos = 0;
		Game.generateLimiters();
	}
	
	
}
