package blockSnakePackage;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_HEIGHT = 600,
					SCREEN_WIDTH = 600,
					UNIT_SIZE = 25,
					GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE,
					DELAY = 75;
	
	final int x [] =new int [GAME_UNITS];
	final int y [] =new int [GAME_UNITS];
	
	int snakeBody = 6;
	int appleCount = 0;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			//debugger grid
			/*
			for(int i = 0; i< SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE );
			}
			*/
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < snakeBody; i++) {
				if(i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else {
					g.setColor(new Color(45, 200, 0));
					 if(appleCount <= 3) {
							g.setColor(new Color(45, 200, 0));
						}
						else if(appleCount <= 5) {
							g.setColor(Color.BLUE);
						}
						else if(appleCount <= 8) {
							g.setColor(Color.CYAN);
						}
						else if(appleCount > 9) {
							g.setColor(new Color(random.nextInt(255), random.nextInt(255),random.nextInt(255)));
						}
					
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				g.setColor(Color.RED);
				g.setFont(new Font("Impact", Font.BOLD, 35));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score:" + appleCount, (SCREEN_WIDTH - metrics.stringWidth("Score:" + appleCount))/2, 
							g.getFont().getSize());
			}
		}else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = snakeBody; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX && (y[0]==appleY))) {
			snakeBody++;
			appleCount++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		for(int i = snakeBody; i>0; i--) {
			if((x[0] == x[i] && (y[0]==y[i]))) {
				
				running = false;
				//gameOver(null);
			}
		}
		//check to see if head touches border
		if(x[0]<0 || x[0]>SCREEN_WIDTH-1 || y[0]<0 || y[0]>SCREEN_HEIGHT-1) {
			running = false;
			//gameOver(null);
		}
		if(!running) {
			timer.stop();
		}
		
	}
	public void gameOver(Graphics g) {
		if (appleCount == 0) {
			g.setColor(Color.GRAY);
		}else if(appleCount >= 3) {
			g.setColor(Color.RED);
		}
		else if(appleCount >= 5) {
			g.setColor(Color.CYAN);
		}
		else if(appleCount > 10) {
			g.setColor(Color.GREEN);
		}
		g.setFont(new Font("Impact", Font.BOLD, 35));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score:" + appleCount, (SCREEN_WIDTH - metrics2.stringWidth("Score:" + appleCount))/2, 
					g.getFont().getSize());
		
		//game over text
		g.setColor(Color.RED);
		g.setFont(new Font("Impact", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2, 
					SCREEN_HEIGHT/2);
	
		
	}
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_HEIGHT,SCREEN_WIDTH));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdpt());
		
		startGame();
		
	}
	public class myKeyAdpt extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction ='L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction ='R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction ='U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction ='D';
				}
				break;
			}
		}
	}
	

}
