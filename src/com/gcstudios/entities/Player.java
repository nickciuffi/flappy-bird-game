package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;
import com.gcstudios.world.World;

public class Player extends Entity{
	
	public int jumpForce = 9, jumpForceMax = -6;
	public int framesAply = 0, framesAplyMax = 2;
	public int framesGravitySlow = 0, framesGravitySlowMax = 10, gravitySlowMax = 5;
	public int gravityForceMax = 2;
	public int realForce = 0;
	public int angle = 0, angleMax = 80, angleMin = -90;
	
	public Player(int x, int y, int width, int height,double speed, BufferedImage sprite) {
		
		super(x, y, width, height,speed,sprite);
		
	}
	
	public void tick(){
		
		framesAply++;
		if(framesAply>=framesAplyMax) {
		gravity();
		framesAply = 0;
		}
		angle = (realForce)*10;
		if(angle >= angleMax) {
			angle = angleMax;
		}
		else if(angle <= angleMin) {
			angle = angleMin;
		}
		
		checkCollisionPipe();
		
	}
	public void gravity() {
		if(realForce<gravityForceMax) {
		realForce++;
		}
		else {
			gravitySlow();
		}
		if(realForce<jumpForceMax) {
			realForce = jumpForceMax;
		}
		if(!isWall(new Entity(this.getX(), getY()+(int)realForce, 16, 16, 1, null))) {
		y = getY()+(int)realForce;
		}
		else {
			if(this.getY()>Game.HEIGHT/2) {
			this.setY((Game.HEIGHT-32));
			}
			else {
				this.setY(16);
			}
			angle = 0;
			realForce = 0;
		}
	}
	private void gravitySlow() {
		framesGravitySlow++;
		if(framesGravitySlow>=framesGravitySlowMax) {
			if(realForce<gravitySlowMax) {
			realForce++;
			}
			framesGravitySlow = 0;
		}
		}
	
	private void checkCollisionPipe() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof PipePieces){
				if(isColliding(e, new Entity(this.getX()+2, this.getY()+2, 12, 12, 1, null))) {
				World.restart();
				}
				else if(e.x == this.getX()) {
					Game.pontos++;
					return;
				}
			}
	}
	}
	
	public boolean isWall(Entity player){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof FlyLimiters){
				if(isColliding(e, player)) {
					return true;
				}
			}
				}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(World.rotate(sprite, angle),this.getX(),this.getY(),null);
	}
	
}
