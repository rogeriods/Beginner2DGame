package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	// Generated serial ID
	private static final long serialVersionUID = 3587031526761099141L;

	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	final int tileSize = originalTileSize * scale; // 48x48 tile

	// In general with this parameters the screen will have 16 tiles to 12 tiles
	// distributed to a screen resolution 768x576 pixels
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768px
	final int screenHeight = tileSize * maxScreenRow; // 576px
	
	// FPS
	int FPS = 60;

	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	// Set player's default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);

		// Enabling this can improve game's rendering performance
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start(); // Automatically call run() method
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime); 
			lastTime = currentTime;
			
			if (delta >= 1) {
				update(); // Update character positions
				repaint(); // Draw the screen with the updated information
				delta--;
				drawCount++;
			}
			
			// Show FPS on terminal
			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	// This method move position of a player
	public void update() {
		if (keyH.upPressed == true) {
			playerY -= playerSpeed;	
		} else if (keyH.downPressed == true) {
			playerY += playerSpeed;
		} else if (keyH.leftPressed == true) {
			playerX -= playerSpeed;
		} else if (keyH.rightPressed == true) {
			playerX += playerSpeed;
		}		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; 
		g2.setColor(Color.white);
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		g2.dispose();		
	}

}

//Sleep method is not to accurate way to move
//public void run() {
//double drawInterval = 1000000000 / FPS; // 0.0166666 seconds
//double nextDrawTime = System.nanoTime() + drawInterval;
//
//while (gameThread != null) {
//	// Update character positions
//	update();
//	// Draw the screen with the updated information
//	repaint();
//	
//	// Pause game loop until sleep time is over
//	try {
//		double remainingTime = nextDrawTime - System.nanoTime();
//		remainingTime = remainingTime / 1000000;
//		
//		if (remainingTime < 0 ) {
//			remainingTime = 0;
//		}
//		
//		Thread.sleep((long) remainingTime);
//		
//		nextDrawTime += drawInterval;
//	} catch (InterruptedException e) {
//		e.printStackTrace();
//	}			
//}
//}
