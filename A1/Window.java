import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by ED on 2016/05/10.
 */
public class Window extends JFrame implements KeyListener {

    Display game; // the game, including the keyboard handler
    SplashScreen ss; // the splash screen
    private boolean gameStarted = false;

    public Window(Display game){
        super("Snake");

        this.game = game;
        ss = new SplashScreen();

        // start with splash screen
        this.add(ss);
        this.addKeyListener(this);

        // Frame Configs
        this.setSize(800, 600);
        this.setResizable(false);
        this.setVisible(true);
        this.setFocusable(true);
    }

    public void gameStart(){
        gameStarted = true;
        remove(ss);
        add(game);
        addKeyListener(game);
        validate();
        game.begin();
    }

    public void gameEnd(){
        gameStarted = false;
        game.end();
        remove(game);
        removeKeyListener(game);
        add(ss);
        validate();
        ss.repaint();
    }

    // Keyboard Event
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_S){
            if (!gameStarted) {
                gameStart();
            }
        }else if (key == KeyEvent.VK_R){
            if (gameStarted && (game.gamePaused()) || game.isGameOver()) {
                gameEnd();
            }
        }
    }

    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
}
