import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;

abstract class Xshape {


    abstract boolean contains(double x, double y, double scale);
    abstract void draw(Graphics2D g, double scale);
    abstract void paintSelectedBorder(Graphics2D g2, double scale);
    abstract void changeXY1(double x, double y);
    abstract void changeXY2(double x, double y);
    abstract void confirm();
    abstract void printCoord();
    abstract void setFill(int c);
    abstract void setColor(int c);
    abstract void setThick(int t);
    abstract void saveFile(PrintWriter f);

    abstract int getThick();
    abstract int getColor();

}
