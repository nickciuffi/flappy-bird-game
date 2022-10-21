package com.gcstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.gcstudios.entities.Entity;
import com.gcstudios.entities.FlyLimiters;
import com.gcstudios.entities.Pipe;
import com.gcstudios.entities.PipePieces;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.UI;
import com.gcstudios.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private Thread listen;
	private boolean isRunning = true;
	public static final int WIDTH = 160;
	public static final int HEIGHT = 200;
	public static final int SCALE = 3;
	public static String gameState = "NORMAL";
	private int framesPipe = 0, framesPipeMax = 100;
	public int numPipes = 0;
	public static int pontos = 0;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Pipe> pipes;
	
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Runnable listenClass = new Listen();
	public static int volume = 0;
	
	
	
	public UI ui;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(40,HEIGHT/2-16,16,16, 1, Game.spritesheet.getSprite(16, 0,16,16));
		
		ui = new UI();
		entities = new ArrayList<Entity>();
		pipes = new ArrayList<Pipe>();
		
		entities.add(player);
		world = new World();
		generateLimiters();
		
	}
	
	public void initFrame(){
		frame = new JFrame("Flappy-Plane");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		listen = new Thread(listenClass);
		listen.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		checkVolume();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			e.tick();
			if(e.animationFrames >= e.animationFramesMax) {
				e.animationFrames = 0;
				if(e.curAnimation >= e.animationMax) {
					e.curAnimation = 0;
				}
				else {
					e.curAnimation++;
				}
				
			}
			e.animationFrames++;
			
		}
		
		generatePipes();
		destroyPipes();
		for(int i = 0; i < pipes.size(); i++) {
			Pipe pi = pipes.get(i);
			pi.tick();
		}
	}
	public void generatePipes() {
		
		framesPipe++;
		if(framesPipe >= framesPipeMax) {
		numPipes++;
		pipes.add(new Pipe(1, numPipes));
		framesPipe = 0;
		}
		
	
	}
	
	public void destroyPipes() {
		for(int i = 0; i < pipes.size(); i++) {
			Pipe pi = pipes.get(i);
			System.out.println(entities.size());
			if(pi.timeDie > 200) {
				for(int x = 0; x < entities.size(); x++) {
					Entity e = entities.get(x);
					if(e instanceof PipePieces && ((PipePieces) e).id == pi.id) {
						entities.remove(e);					
						}
				}
				
			}
			if(pi.timeDie > 210) {
				pipes.remove(i);
			}
			}
		}
	
	
	public static void generateLimiters(){
		for(int i = 0; i < (int)(WIDTH/16); i++) {
			entities.add(new FlyLimiters(i*16, HEIGHT-16, 16, 16, 1, spritesheet.getSprite(0, 48, 16, 16)));
			entities.add(new FlyLimiters(i*16, 0, 16, 16, 1, spritesheet.getSprite(0, 48, 16, 16)));
		}
	}
	
	public void checkVolume() {
		if(volume > 5) {
			player.realForce -= player.jumpForce/8; 
		}
	}
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(7,0,44));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			e.render(g);
		}
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		
		ui.render(g);
		
		bs.show();
	}
	
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
			player.realForce -= player.jumpForce;   
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {	
		player.realForce -= player.jumpForce;   
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
