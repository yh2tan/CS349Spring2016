package yh2tan.portablesketch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by ED on 2016/07/07.
 */
public class Xline implements Xshape {

    // basic attributes
    private int color;
    private int thickness;

    // geometric attributes
    int x1;
    int y1;
    int x2;
    int y2;


    public Xline(int x, int y, int c, int t) {
        x1 = x;
        x2 = x;
        y1 = y;
        y2 = y;

        color = c;
        thickness = t;
    }

    public Xline(int[] info) {
        x1 = info[0];
        this.x2 = info[2];
        y1 = info[1];
        this.y2 = info[3];

        color = info[4];
        thickness = info[5];
    }

    public void fill(int color) {}
    public void setColor(int c) {
        this.color = c;
    }
    public void setThick(int thick) {
        thickness = thick;
    }
    public int getColor() {
        return color;
    }
    public int getThick() {
        return thickness;
    }

    // geometric attribute of the shape
    public void changeXY1(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

    public void changeXY2(int x, int y) {
        x2 = x;
        y2 = y;
    }

    public boolean contains(double x, double y) {

        int topX = Math.min(x1, x2);
        int topY = Math.min(y1, y2);
        int botX = Math.max(x1, x2);
        int botY = Math.max(y1, y2);

        // compute the linear equation
        double coeff = (x2 - x1 == 0 ? 0 : ((double)(y2 - y1)/(double)(x2 - x1)));
        double constant = y1 - coeff * x1;

        if (x2 - x1 == 0 && x == x1){
            return y < botY && y > topY;
        }

        if ( x > botX || x < topX || y > botY || y < topY)
            return false;

        // give few pixel of freedom
        return  Math.abs(y - coeff * x - constant) < (thickness+2)*4 + Math.abs(coeff*5);
    }

    // Control function
    public void confirm() {}
    public void drawShape(Canvas c) {
        Paint paint = new Paint();

        //Paint Outer Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4*(2+thickness));
        paint.setColor(MainActivity.color[this.color]);
        c.drawLine((float)x1, (float)y1, (float)x2, (float)y2, paint);
    }

    public void drawSelectedBorder(Canvas c) {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8 + (float) 4*(2+thickness));
        paint.setShader(DrawModel.selectedBorder);
        paint.setColor(Color.WHITE);
        c.drawLine((float)x1, (float)y1, (float)x2, (float)y2, paint);
        paint.setStrokeWidth(1);
        paint.setShader(null);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4*(2+thickness));
        paint.setColor(MainActivity.color[this.color]);
        c.drawLine((float)x1, (float)y1, (float)x2, (float)y2, paint);
    }

    public int[] getInfo(){
        int[] ar = {x1,y1,x2,y2,color,thickness};
        return ar;
    }
}