package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PipePieces extends Entity{
	public int id;
	

	public PipePieces(double x, double y, int width, int height, BufferedImage sprite, int id) {
		super(x, y, width, height, 1, sprite);
		this.id = id;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX(),this.getY(), this.getWidth(), this.getHeight() ,null);
		
		}

}
