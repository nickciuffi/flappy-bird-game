package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

public class Pipe{
	
	public int x = Game.WIDTH, y = 0;
	public int speed;
	BufferedImage pipe = Game.spritesheet.getSprite(0, 0, 16, 16);
	BufferedImage pipe_end = Game.spritesheet.getSprite(0, 16, 16, 16);
	BufferedImage pipe_start = Game.spritesheet.getSprite(0, 32, 16, 16);
	private int alturaPassagem, tamanhoPassagem = 56;
	public int id;
	public int timeDie = 0;
	
	public Pipe(int speed, int id) {
		this.speed = speed;
		this.id = id;
		alturaPassagem = Entity.rand.nextInt(200 - 64 - tamanhoPassagem)+32;
		
		Game.entities.add(new PipePieces(this.x, 16, 16, alturaPassagem, pipe, id));
				
		Game.entities.add(new PipePieces(this.x, alturaPassagem, 16, 16, pipe_end, id));
			
		Game.entities.add(new PipePieces(this.x, alturaPassagem + tamanhoPassagem, 16, Game.HEIGHT - alturaPassagem - tamanhoPassagem -16, pipe, id));
			
		Game.entities.add(new PipePieces(this.x, alturaPassagem + tamanhoPassagem, 16, 16, pipe_start, id));
			
		}
	
	public void tick() {
		timeDie++;
		x-=speed;
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof PipePieces && ((PipePieces) e).id == this.id) {
				e.x = x;
			}
		}
		
	}
	

}
