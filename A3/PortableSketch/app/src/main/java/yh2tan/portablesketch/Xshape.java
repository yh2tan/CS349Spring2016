package yh2tan.portablesketch;
import android.graphics.Canvas;

/**
 * Created by ED on 2016/07/07.
 */
abstract interface Xshape {
    // basic attribute of the shape
    void fill(int color); // change the current filled color
    void setColor(int color); // change the current color of the border
    void setThick(int thick); // set line thickness
    int getColor();
    int getThick();

    // geometric attribute of the shape
    void changeXY1(int dx, int dy);
    void changeXY2(int dx, int dy);
    boolean contains(double x, double y); //check if the points are contained by the shape

    int[] getInfo();

    // Control function
    void drawShape(Canvas c); // this function is called during render
    void drawSelectedBorder(Canvas c); // this function should be called when the shape is selected
}
