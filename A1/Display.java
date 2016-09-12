import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Display extends JPanel implements KeyListener, ActionListener {
	
	//variables and objects
	private int fps;
	private int speed;
	private int score = 0;
	private Game game;
	
	private ArrayList<Integer> foods;
	private ArrayList<Integer> snake;
	private int powerUps;
	private Timer gameThread;

	// used to calculate real FPS
    private long timeStart;
    private double realfps;
    
    static Color Basic = new Color(210, 210, 210);
    static Color Deep = new Color(35, 35, 35);
	static Color Rampage = new Color(110, 15, 15);
	
	// constructor
	public Display(int fps, int speed){
		super();
		this.fps = fps;
		this.speed = speed;
		
		realfps = (double) fps;
		
		game = new Game(fps, speed);
		foods = null;
		snake = null;
		powerUps = 0;
		
		gameThread = new Timer( 1000/fps , this);
		addKeyListener(this); // add Event Handler to this Panel.
		setFocusable(false);
	}

	public void begin(){
		gameThread.start();
        game.start();
        repaint();
	}

	public void end(){
		gameThread.stop();
        game.clean(); // clean the board
        update();
	}
	
	// Draw method paints the current JPanel
	public void paintComponent(Graphics g){
		super.paintComponent(g); // removes outed graphics

		Color base = game.hadSteroid() > 0 ? Rampage:Deep;
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(base);
        g2.fillRect(0, 0, 800, 580);
		
	    String fps = String.format("FPS: %d", (int)realfps);
	    String speed= String.format("Speed: %s",
				game.hadSteroid() > 0 ? "RAMPAGED": Integer.toString(this.speed));
	    String score = String.format("Score: %d", this.score);

	    // printing texts
	    g2.setColor(Color.white);
	    g2.setFont(new Font("Arial", Font.PLAIN, 18));
	    g2.drawString(fps, 650, 550);
	    g2.drawString(speed, 50, 550);
	    g2.drawString(score, 350, 550);

		//paint powerups
		if (powerUps != 0){

			int type = powerUps/10000;
			int x = Game.decodeX(powerUps%10000);
			int y = Game.decodeY(powerUps%10000);

			if (type == 0)
				SplashScreen.paintSteroid(g2, x, y, base);
			else if (type == 1){
				SplashScreen.paintLaxation(g2, x, y, base);
			}
		}

		//paint snake
		if (snake != null) {
			for (Integer i : snake) {
				int x = Game.decodeX(i.intValue());
				int y = Game.decodeY(i.intValue());

				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.white);
				g2.fillRect(40 + x * 20, 40 + y * 20, 20, 20);
				g2.setColor(base);
				g2.drawRect(40 + x * 20, 40 + y * 20, 20, 20);
			}
		}
		
		//Trim head
		if (game.started()){
			int x = Game.decodeX(game.getHead());
			int y = Game.decodeY(game.getHead());
			g2.setColor(Basic);
			g2.fillRect(40+x*20, 40+y*20, 20, 20);
			g2.setColor(Deep);
			g2.drawRect(40+x*20, 40+y*20, 20, 20);
		}
		
		//paint food
		if (foods != null) {
			for (Integer i : foods) {
				int x = Game.decodeX(i.intValue());
				int y = Game.decodeY(i.intValue());

				SplashScreen.paintFood(g2, x, y);
			}
		}

		//paint the pause frame when paused:
		if (gamePaused()){
			//Frame
			g2.setColor(Deep);
			g2.fillRect(275, 200, 250, 100);
			g2.setColor(Color.white);
			g2.drawRect(275, 200, 250, 100);

			g2.drawString("Paused", 370, 225);

			// Addtitoinal Instructions:
			g2.setFont( new Font("Arial", Font.PLAIN, 16));
			g2.drawString("Press R to return to menu", 310, 255);
			g2.drawString("Press P to unpause", 310, 285);
		}

		//paint the pause frame when paused:
		if (game.gameStop()){
			//Frame
			g2.setColor(Deep);
			g2.fillRect(275, 200, 250, 100);
			g2.setColor(Color.white);
			g2.drawRect(275, 200, 250, 100);

			g2.drawString("GAME OVER!", 345	, 225);
			String Score = String.format("Score: %d", this.score);
			g2.drawString(Score, 310, 255);

			// Addtitoinal Instructions:
			g2.setFont( new Font("Arial", Font.PLAIN, 16));
			g2.drawString("Press R to return to menu", 310, 285);
		}
	
		// Draw the borderline
		g2.setStroke(new BasicStroke(1));
	    g2.setColor(Color.white);
	    g2.drawRect(20, 20, 760, 480);
	    g2.setColor(Color.white);
	    g2.drawRect(40, 40, 720, 440);
	}

	public void increaseSpeed(){
		if (speed < 10){
			speed += 1;
			game.addSpeed(1);
		}
	}

	public void decreaseSpeed(){
		if (speed > 1){
			speed -= 1;
			game.addSpeed(-1);
		}
	}
	
	public void update(){
		//Update game status
		foods = game.getFoods();
		snake = game.getSnake();
		score = game.getScore();
		powerUps = game.getPower();
		repaint();

		if (game.gameStop()){
			gameThread.stop();
			repaint();
		}
	}

	//Determine if the game is paused
	public boolean gamePaused(){
		return game.gamePaused();
	}

	public boolean isGameOver() {
		return game.gameStop();
	}

	// Main Game loop
	public void actionPerformed(ActionEvent e){
		timeStart = System.currentTimeMillis();
		game.gameRun();
		update();
		
		realfps = 1000.0/(System.currentTimeMillis() - timeStart + 1000.0/fps); // realFPS calculation
	}
	
	// Keyboard Event
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
	
		if (key == KeyEvent.VK_P) {
            game.togglePause();
        }else if (key == KeyEvent.VK_UP){
			if (!game.gamePaused())
				game.snakeMove('u');
		}else if (key == KeyEvent.VK_DOWN){
			if (!game.gamePaused())
				game.snakeMove('d');
		}else if (key == KeyEvent.VK_LEFT){
			if (!game.gamePaused())
				game.snakeMove('l');
		}else if (key == KeyEvent.VK_RIGHT){
			if (!game.gamePaused())
				game.snakeMove('r');
		}else if (key == KeyEvent.VK_Q){
			game.addSteroid();
		}else if (key == KeyEvent.VK_W){
			game.addLaxation();
		}else if (key == KeyEvent.VK_X){
			increaseSpeed();
		}else if (key == KeyEvent.VK_Z) {
			decreaseSpeed();
		}
	}

	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
}
