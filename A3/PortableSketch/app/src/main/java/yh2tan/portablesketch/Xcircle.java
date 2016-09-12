package yh2tan.portablesketch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by ED on 2016/07/07.
 */
public class Xcircle implements Xshape{
    // basic attributes
    private int color;
    private int fill;
    private int thickness;

    // geometric attributes
    int x1;
    int y1;
    int x2;
    int y2;

    // additional attributes for geometric calculation
    double centerX;
    double centerY;
    double radius;

    public Xcircle(int x, int y, int c, int t) {
        x1 = x;
        x2 = x;
        y1 = y;
        y2 = y;

        color = c;
        thickness = t;

        fill = -1;
    }

    public Xcircle(int[] info) {
        x1 = info[0];
        this.x2 = info[2];
        y1 = info[1];
        this.y2 = info[3];

        color = info[4];
        thickness = info[5];
        fill = info[6];

        centerX = (x1+x2)/2.0;
        centerY = (y1+y2)/2.0;
        radius = Math.max(x1,x2) - centerX;
    }

    public void fill(int color) {
        fill = color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public void setThick(int thick) {
        this.thickness = thick;
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
        x2 += dx;
        y1 += dy;
        y2 += dy;
        centerX += dx;
        centerY += dy;
    }
    public void changeXY2(int x, int y) {
        // to maintain the circle, we only add on dx to keep its radius

        y2 += (y-y2 > 0 ? 1 : -1)*Math.abs(x-x2);
        x2 = x;

        centerX = (x1+x2)/2.0;
        centerY = (y1+y2)/2.0;
        radius = Math.max(x1,x2) - centerX;
    }

    public boolean contains(double x, double y) {

        double xdist = x - centerX;
        double ydist = y - centerY;

        return xdist*xdist + ydist*ydist < radius*radius;
    }

    // Control function
    public void confirm() {

    }
    public void drawShape(Canvas c) {
        Paint paint = new Paint();

        if (fill >= 0){
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MainActivity.color[fill]);
            c.drawCircle((float)centerX, (float)centerY, (float)radius, paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4*(2+thickness));
        paint.setColor(MainActivity.color[color]);
        c.drawCircle((float)centerX, (float)centerY, (float)radius, paint);
    }

    public void drawSelectedBorder(Canvas c) {
        Paint paint = new Paint();

        int off = 4+2*(thickness+2);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setShader(DrawModel.selectedBorder);
        paint.setColor(Color.WHITE);
        c.drawCircle((float)centerX, (float)centerY, (float)radius+off, paint);;
        paint.setStrokeWidth(1);
        paint.setShader(null);
        paint.setColor(Color.GRAY);
        c.drawCircle((float)centerX, (float)centerY, (float)radius+off+2, paint);;
    }

    public int[] getInfo(){
        int[] ar = {x1,y1,x2,y2,color,thickness,fill, (int)radius};
        return ar;
    }


}
