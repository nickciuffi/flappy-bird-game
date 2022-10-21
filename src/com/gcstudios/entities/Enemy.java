package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

public class Enemy extends Entity{
	
	private BufferedImage[] enemyImg = new BufferedImage[3];
	
	public Enemy(int x, int y, int width, int height,int speed, boolean isActiveStart, int atuation) {
		super(x, y, width, height,speed,null);
		
		for(int i = 0; i < 3; i++) {
			enemyImg[i] = Game.spritesheet.getSprite(32+(i*16), 32,16,16);
		}
		
	}

	public void tick(){
		
	}
	
	
	public void render(Graphics g) {
		
		g.drawImage(enemyImg[curAnimation],this.getX(),this.getY(),null);
		
	}
	
	
}
