package yh2tan.portablesketch;

import android.graphics.Color;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by ED on 2016/07/07.
 */
public class Xrect implements Xshape {

    // basic attributes
    private int color;
    private int fill;
    private int thickness;

    // geometric attributes
    int x1;
    int y1;
    int x2;
    int y2;


    public Xrect(int x, int y, int c, int t) {
        x1 = x;
        x2 = x;
        y1 = y;
        y2 = y;

        color = c;
        thickness = t;

        fill = -1;
    }

    public Xrect(int[] info) {
        x1 = info[0];
        this.x2 = info[2];
        y1 = info[1];
        this.y2 = info[3];

        color = info[4];
        thickness = info[5];
        fill = info[6];
    }

    public void fill(int color) {
        fill = color;
    }
    public void setColor(int color) {
        this.color = color;
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

        return x >= topX && x <= botX && y >= topY && y <= botY;
    }

    // Control function
    public void confirm() {}
    public void drawShape(Canvas c) {
        Paint paint = new Paint();

        if (fill >= 0){
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MainActivity.color[fill]);
            c.drawRect((float)Math.min(x1,x2),(float)Math.min(y1,y2),
                    (float)Math.max(x1,x2),(float)Math.max(y1,y2),paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(MainActivity.color[color]);
        paint.setStrokeWidth((float) 4*(2+thickness));
        c.drawRect((float)Math.min(x1,x2),(float)Math.min(y1,y2),
                (float)Math.max(x1,x2),(float)Math.max(y1,y2),paint);
    }

    public void drawSelectedBorder(Canvas c) {
        Paint paint = new Paint();

        int off = 4+2*(2+thickness);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setShader(DrawModel.selectedBorder);
        paint.setColor(Color.WHITE);
        c.drawRect((float)Math.min(x1,x2)-off,(float)Math.min(y1,y2)-off,
                    (float)Math.max(x1,x2)+off,(float)Math.max(y1,y2)+off,paint);
        paint.setStrokeWidth(1);
        paint.setShader(null);
        paint.setColor(Color.GRAY);
        c.drawRect((float)Math.min(x1,x2)-off-2,(float)Math.min(y1,y2)-off-2,
                (float)Math.max(x1,x2)+off+2,(float)Math.max(y1,y2)+off+2,paint);

    }

    public int[] getInfo(){
        int[] ar = {x1,y1,x2,y2,color,thickness,fill};
        return ar;
    }
}
