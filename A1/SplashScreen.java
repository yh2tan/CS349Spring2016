import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by ED on 2016/05/10.
 */
public class SplashScreen extends JPanel {

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        // background Coloring
        g2.setColor(Display.Deep);
        g2.fillRect(0, 0, 800, 580);

        // Title
        g2.setColor(Color.lightGray);
        g2.setFont(new Font("phosphate", Font.BOLD, 100));
        g2.drawString("SNAKE", 260, 140);

        // SubTitle
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString( "Created by: Ye Hua (Edward) Tan", 265, 190 );
        g2.drawString( "Uwaterloo ID: 20532547", 310, 220);

        // Instructions:
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString( "Basic Instruction:", 150, 280);
        g2.drawString( "- Use Arrow Keys to move the snake.", 200, 310);
        g2.drawString( "- Press S to start", 200, 330);
        g2.drawString( "- Press P to pause/resume the game", 200, 350);
        g2.drawString( "- Press R to return back to main menu during pause.", 200, 370);
        g2.drawString( "PowerUps:", 150, 400);

        g2.drawString( "Steroid: makes you rampage, double the speed and the score", 220, 435);
        g2.drawString( "Laxation: instant diet, removes some weight(length)", 220, 475);
        g2.drawString( "* See readme for full detail!!", 315, 530 );
        paintSteroid(g2, 7 , 19, Display.Deep);
        paintLaxation(g2, 7, 21, Display.Deep);
;    }

    // paint the food component
    public static void paintFood(Graphics2D g2, int x, int y){
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(40+x*20, 40+y*20, 20, 20);
    }

    // paint the food component
    public static void paintSteroid(Graphics2D g2, int x, int y, Color base){
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(40+x*20, 40+y*20, 20, 20);
        // trims
        g2.setColor(Color.white);
        g2.drawLine(40+x*20+10, 43+y*20, 40+x*20+10, 37+y*20+20);
        g2.drawLine(40+x*20+3 , 40+y*20+10, 40+x*20+17, 40+y*20+10);
        g2.setColor(base);
        g2.drawLine(40+x*20+10, 45+y*20, 40+x*20+10, 35+y*20+20);
        g2.drawLine(40+x*20+5 , 40+y*20+10, 40+x*20+15, 40+y*20+10);
    }

    // paint the food component
    public static void paintLaxation(Graphics2D g2, int x, int y, Color base){
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(40+x*20, 40+y*20, 20, 20);
        // trims
        g2.setColor(base);
        g2.drawLine(40+x*20+10, 43+y*20, 40+x*20+10, 37+y*20+20);
        g2.drawLine(40+x*20+3 , 40+y*20+10, 40+x*20+17, 40+y*20+10);
        g2.setColor(Color.red);
        g2.drawLine(40+x*20+10, 45+y*20, 40+x*20+10, 35+y*20+20);
        g2.drawLine(40+x*20+5 , 40+y*20+10, 40+x*20+15, 40+y*20+10);
    }
}
