import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;

class Xline extends Xshape{
    int color;
    boolean filled;
    Shape shape;
    int thick;

    // internal data
    double x1;
    double y1;
    double x2;
    double y2;


    // create Xrectangle, unscaled
    public Xline (int c, int t, int x, int y){
        shape = new Line2D.Double(x, y, x, y);
        color = c;
        thick = t;

        this.x1 = x;
        this.x2 = 0;
        this.y1 = y;
        this.y2 = 0;
    }

    // create Xrectangle, unscaled
    public Xline (int c, int t, int x, int y, int x1, int y1){
        shape = new Line2D.Double(x, y, x1, y1);
        color = c;
        thick = t;

        this.x1 = x;
        this.x2 = x1;
        this.y1 = y;
        this.y2 = y1;
    }

    public boolean contains(double x, double y, double scale){

        x = x/scale;
        y = y/scale;

        double maxX = (x1 > x1 + x2 ? x1 : x2+x1);
        double maxY = (y1 > y1 + y2 ? y1 : y2+y1);
        double minX = (x1 > x1 + x2 ? x2+x1 : x1);
        double minY = (y1 > y1 + y2 ? y2+y1 : y1);

        if ( x > maxX || x < minX || y > maxY || y < minY)
            return false;

        // compute the linear equation

            double coeff = (x2 == 0 ? 0 : y2 / x2);
            double constant = y1 - coeff * x1;


        System.out.printf("%f\n",coeff);
        System.out.printf("%f\n",Math.abs(y - coeff * x - constant));

        return ((int) Math.abs(y - coeff * x - constant)) < Math.abs(thick + 2*coeff); // give a 2 pixel freedom
    }

    // change origin
    public void changeXY1(double x, double y){
        x1 += x;
        y1 += y;
    }

    // change delta
    public void changeXY2(double x, double y) {
        x2 = x - x1;
        y2 = y - y1;
    }

    public void confirm(){
        shape = new Line2D.Double(x1, y1, x2, y2);
    }

    public void setFill(int c){
        color = c;
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

    public void draw(Graphics2D g2, double scale){
        g2.setStroke(new BasicStroke(thick));
        g2.setColor(ColorPallet.colors[color]);
        g2.drawLine((int)(x1*scale),(int)(y1*scale),(int)(x1*scale+x2*scale),(int)(y1*scale+y2*scale));
    }

    public void paintSelectedBorder(Graphics2D g2, double scale) {
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(thick+4));
        g2.drawLine((int)(x1*scale),(int)(y1*scale),(int)(x1*scale+x2*scale),(int)(y1*scale+y2*scale));

        g2.setStroke(new BasicStroke(thick+2));
        g2.setPaint(DrawView.SelectedBorder);
        g2.drawLine((int)(x1*scale),(int)(y1*scale),(int)(x1*scale+x2*scale),(int)(y1*scale+y2*scale));

        draw(g2, scale);
    }

    public void saveFile(PrintWriter f){
        String content = String.format("%d %d %d %d %d %d %d %d",0, (int)x1, (int)y1, (int)x2, (int)y2, color, -1, thick);
        f.println(content);
    }

    public void printCoord(){}
}

