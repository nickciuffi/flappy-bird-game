package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gcstudios.world.Tile;

public class Entity {
	

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected double speed;
	
	public int depth;

	protected BufferedImage sprite;
	
	public static Random rand = new Random();
	
	public int animationFrames = 0, animationFramesMax = 5, curAnimation = 0, animationMax = 2;
	
	
	public Entity(double x,double y,int width,int height,double speed,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick(){}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	public static boolean isColliding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),e1.getWidth(),e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}
	
	
	public boolean isCollisionTileEntity(Tile t, Entity e) {
		Rectangle tile = new Rectangle(t.x, t.y, 16, 16);
		Rectangle entitie = new Rectangle((int)e.x, (int)e.y, 16, 16);
		if(tile.intersects(entitie)) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX(),this.getY(),null);
		}
	
}
