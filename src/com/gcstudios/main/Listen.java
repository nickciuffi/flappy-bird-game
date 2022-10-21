package com.gcstudios.main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Listen implements Runnable{
	
	private boolean isRunning = true;
	protected TargetDataLine line = null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				timer+=1000;
			}
			
		}
		
		
	}
	
	public void tick() {
		try {
		startListening();
		}
		catch(Exception e) {
			
		}
	}
	
	public void startListening() {
	    // Open a TargetDataLine for getting mic input level
	    AudioFormat format = new AudioFormat(42000.0f, 16, 1, true, true); // Get default line
	    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	    if (!AudioSystem.isLineSupported(info)) { // If no default line
	        //System.out.println("Pluga um microfone e grita pf");
	    }

	    // Obtain and open the line.
	    try {
	        line = (TargetDataLine) AudioSystem.getLine(info);
	        line.open(format);
	        line.start();
	    } catch (LineUnavailableException ex) {
	        System.out.println("The TargetDataLine is Unavailable.");
	    }

	    int level = 0; // Hold calculated RMS volume level
	    byte tempBuffer[] = new byte[6000]; // Data buffer for raw audio
	    try {
	        // Continually read in mic data into buffer and calculate RMS
	        while (true) {
	            // If read in enough, calculate RMS
	            if (line.read(tempBuffer, 0, tempBuffer.length) > 0) {
	                level = calculateRMSLevel(tempBuffer);
	                System.out.println(level);
	                Game.volume = level; // Update bar display
	            }
	        }
	    } catch (Exception e) {
	        System.err.println(e);
	        System.exit(0);
	    }
	}
	
	protected static int calculateRMSLevel(byte[] audioData) {
	    long lSum = 0;
	    for(int i = 0; i < audioData.length; i++)
	        lSum = lSum + audioData[i];

	    double dAvg = lSum / audioData.length;

	    double sumMeanSquare = 0d;
	    for(int j = 0; j < audioData.length; j++)
	        sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

	    double averageMeanSquare = sumMeanSquare / audioData.length;
	    return (int)(Math.pow(averageMeanSquare, 0.5d) + 0.5) - 50;
	}

}
