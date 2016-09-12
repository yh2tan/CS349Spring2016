package yh2tan.portablesketch;

import android.graphics.LinearGradient;
import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ED on 2016/07/07.
 */
public class DrawModel {

    // required attributes
    int color = 0;
    int thickness = 0;
    int tool = 0;

    // focus( to keep track of the first tap when user triggers event )
    int focusX = 0;
    int focusY = 0;

    // store flag (use to indicate if the movement requires storing)
    boolean stored = false;

    static LinearGradient selectedBorder;

    // shapes and actions
    ArrayDeque<Xshape> loShape;
    Xshape selected;

    // undo/redo operation
    ArrayDeque<ArrayDeque<int[]>> undoStack;
    ArrayDeque<ArrayDeque<int[]>> redoStack;

    // main activity
    MainActivity android;

    // contructor
    public DrawModel(MainActivity a) {
        loShape = new ArrayDeque<Xshape>();
        undoStack = new ArrayDeque<ArrayDeque<int[]>>();
        redoStack = new ArrayDeque<ArrayDeque<int[]>>();
        android = a;

        //used to sketch the border
        selectedBorder = new LinearGradient(0,0,10,10,Color.GRAY,Color.WHITE, Shader.TileMode.REPEAT);
    }

    // set attribute functions
    public void setColor(int c) {
        Log.d("DrawModel", String.format("%d",c));
        color = c;
        Log.d("DrawModel", String.format("%b",selected == null));
        if (selected != null) {
            makeChanges();
            selected.setColor(color);
        }
        android.notifyView();
    }

    public void setThickness(int t) {

        thickness = t;
        if (selected != null){
            makeChanges();
            selected.setThick(thickness);
        }
        android.notifyView();
    }

    public void setTool(int t) {
        tool = t;
        if (tool != 0)
            selected = null;
        android.notifyView();
    }

    public void createShape(int x, int y) {
        Xshape newshape;

        if (tool == 1)
            newshape = new Xline(x, y, color, thickness);
        else if (tool == 2)
            newshape = new Xrect(x, y, color, thickness);
        else if (tool == 3)
            newshape = new Xcircle(x, y, color, thickness);
        else
            return;

        makeChanges();
        selected = newshape;
        loShape.addFirst(selected);
        android.notifyView();
    }

    // change the current selected shape only on lower coordinates
    public void resizeSelected(int x, int y) {
        selected.changeXY2(x, y);
        android.notifyView();
    }

    // light unleash resets the relative focus
    public void lightUnleash() {
        focusX = 0;
        focusY = 0;

        stored = false;
        android.notifyView();
    }

    // heavy unleash confirms the shape
    public void heavyUnleash() {
        selected = null;
        android.notifyView();
    }

    // onSelected determines whether the point xy is within the selected xshape
    public boolean onSelected(int x, int y) {
        return selected != null ? false : selected.contains((double) x, (double) y);
    }

    public void select(int x, int y) {

        if (loShape.isEmpty())
            return;

        for (Xshape s : loShape) {
            if (s.contains(x, y)) {
                selected = s;
                color = s.getColor();
                thickness = s.getThick();
                focusX = x;
                focusY = y;
                android.notifyView();
                android.setButtonState();
                return;
            }
        }

        selected = null;
        android.notifyView();
    }

    public void removeShape(int x, int y){
        for (Xshape s : loShape){
            if (s.contains(x,y)){
                makeChanges();
                loShape.remove(s);
                android.notifyView();
                return;
            }
        }

        android.notifyView();
    }

    public Xshape getSelected() {
        return selected;
    }

    public ArrayDeque<Xshape> getAllShape() {
        return new ArrayDeque<Xshape>(loShape);
    }


    public void moveShape(int x, int y) {
        if (!stored){
            stored = true;
            makeChanges();
        }
        selected.changeXY1(x - focusX, y - focusY);
        focusX = x;
        focusY = y;
        android.notifyView();
    }

    public void setFilled(int x, int y) {
        for (Xshape s : loShape) {
            if (s.contains(x, y)) {
                makeChanges();
                s.fill(color);
                android.notifyView();
                return;
            }
        }

        android.notifyView();
    }

    public void clear() {
        selected = null;
        loShape = new ArrayDeque<Xshape>();
        android.notifyView();
        undoStack = new ArrayDeque<ArrayDeque<int[]>>();
        redoStack = new ArrayDeque<ArrayDeque<int[]>>();
    }

    // undo/redo
    public void makeChanges(){
        undoStack.addFirst(getCopy());
        redoStack.clear();
        // for memory purpose we only store up to 5 objects
        if (undoStack.size() > 10)
            undoStack.pollLast();
    }

    public void undo(){
        selected = null;

        if (undoStack.isEmpty())
            return;

        redoStack.addFirst(getCopy());
        loShape = fetchArray(undoStack.pollFirst());
        android.notifyView();
    }

    public void redo(){
        selected = null;

        if (redoStack.isEmpty())
            return;

        undoStack.addFirst(getCopy());
        loShape = fetchArray(redoStack.pollFirst());
        android.notifyView();
    }

    public ArrayDeque<int[]> getCopy(){
        ArrayDeque<int[]> newAr = new ArrayDeque<int[]>();

        for (Xshape s: loShape){
            newAr.addLast(s.getInfo());
        }

        return newAr;
    }

    public ArrayDeque<Xshape> fetchArray(ArrayDeque<int[]> infos){
        ArrayDeque<Xshape> newDeq = new ArrayDeque<Xshape>();

        for (int[] i:infos){
            if (i.length == 6){
                newDeq.addLast(new Xline(i));
            }else if (i.length == 7){
                newDeq.addLast(new Xrect(i));
            }else if (i.length == 8){
                newDeq.addLast(new Xcircle(i));
            }else {
                Log.d("Fetch Array", "Error array size");
            }
        }

        return newDeq;
    }
}
