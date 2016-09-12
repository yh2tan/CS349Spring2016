import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;

class Xrectangle extends Xshape {
    int color;
    boolean filled = false;
    Shape shape;
    int thick;

    int fill = -1;

    // internal data
    double x;
    double y;
    double dx;
    double dy;



    // create Xrectangle, unscaled
    public Xrectangle (int c, int t, int x, int y){
        shape = new Rectangle(new Point(x,y), new Dimension(0, 0));
        color = c;
        thick = t;

        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
    }

    public Xrectangle (int c, int t, int x, int y, int w, int h, int f){
        shape = new Rectangle(new Point(x,y), new Dimension(w, h));
        color = c;
        thick = t;

        this.x = x;
        this.y = y;
        dx = w;
        dy = h;

        filled  = (f >= 0);
        fill = f;
    }

    public void setFill(int c){
        filled = true;
        fill = c;
    }

    public void setColor(int c){
        color = c;
    }

    public void setThick(int t){
        thick = t;
    }

    public int getThick(){
        return thick;
    }

    public int getColor(){
        return color;
    }

    public boolean contains(double x, double y, double scale){
        return shape.contains(x/scale,y/scale);
    }

    // change origin
    public void changeXY1(double x, double y){
        this.x += x;
        this.y += y;
    }

    // change delta
    public void changeXY2(double x, double y){
        dx = x - this.x;
        dy = y - this.y;
    }

    // reconstruct shape
    public void confirm(){
        x= (x < (x + dx) ? x : x + dx);
        y= (y < (y + dy) ? y : y + dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        shape = new Rectangle(new Point((int)x,(int)y), new Dimension((int)dx, (int)dy));
    }

    public void draw(Graphics2D g2, double scale){

        int xTop = (int) (x*scale < (x*scale + dx*scale) ? x*scale : x*scale + dx*scale);
        int yTop = (int) (y*scale < (y*scale + dy*scale) ? y*scale : y*scale + dy*scale);

        g2.setStroke(new BasicStroke(thick));

        if (filled) {
            g2.setColor(ColorPallet.colors[fill]);
            g2.fillRect((int) xTop, (int) yTop , (int) Math.abs(dx * scale), (int) (Math.abs(dy * scale)));
        }

        g2.setColor(ColorPallet.colors[color]);
        g2.drawRect((int)xTop,(int)yTop,(int)Math.abs(dx*scale),(int)Math.abs(dy*scale));
    }

    public void paintSelectedBorder(Graphics2D g2, double scale) {

        int xTop = (int) (x*scale < (x*scale + dx*scale) ? x*scale : x*scale + dx*scale) - 3;
        int yTop = (int) (y*scale < (y*scale + dy*scale) ? y*scale : y*scale + dy*scale) - 3;

        g2.setStroke(new BasicStroke(4));
        g2.setPaint(DrawView.SelectedBorder);
        g2.drawRect((int) xTop, (int) yTop,(int)((Math.abs(dx)+6)*scale), (int)((Math.abs(dy)+6)*scale));
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.gray);
        g2.drawRect((int) xTop-2, (int) yTop-2, (int)((Math.abs(dx)+10)*scale), (int)((Math.abs(dy)+10)*scale));
    }

    public void saveFile(PrintWriter f){
        String content = String.format("%d %d %d %d %d %d %d %d", 1, (int)x, (int)y, (int)dx, (int)dy, color, fill, thick);
        f.println(content);
    }

    public void printCoord(){
        System.out.printf("%f, %f, %f, %f\n", x, y, x+dx, y+dy );
    }
}