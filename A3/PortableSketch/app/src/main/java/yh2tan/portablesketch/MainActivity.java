package yh2tan.portablesketch;

import java.util.Arrays;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity implements IView {

    //Loging
    String logId = this.getClass().getSimpleName();

    // Model
    DrawModel model;

    // list of components
    DrawView drawview;

    // RadioGroups
    RadioButtonTable toolBar;
    RadioButtonTable colorPallet;
    RadioGroup lineThickness;

    // Utilities
    Button undo;
    Button redo;
    Button clear;
    View.OnClickListener util;

    // Constants
    final int toolID[] = {
            R.id.Selection, R.id.Line, R.id.Rectangle, R.id.Circle, R.id.Paint, R.id.Eraser
    };

    final int tool0[] = {
            R.drawable.select0, R.drawable.line0, R.drawable.rect0, R.drawable.circle0,
            R.drawable.paint0, R.drawable.erase0
    };

    final int tool1[] = {
            R.drawable.select1, R.drawable.line1, R.drawable.rect1, R.drawable.circle1,
            R.drawable.paint1, R.drawable.erase1
    };

    final int colorID[] = {
            R.id.black, R.id.white, R.id.color3, R.id.color4, R.id.color5,
            R.id.color6, R.id.color7, R.id.color8, R.id.color9, R.id.color10
    };

    static int color[] = {
            Color.BLACK, Color.WHITE, Color.BLUE, Color.RED, Color.YELLOW,
            Color.GREEN, Color.CYAN, Color.DKGRAY, Color.LTGRAY, Color.MAGENTA
    };

    final int lineID[] = {
            R.id.lineThick0,  R.id.lineThick1,  R.id.lineThick2,  R.id.lineThick3,
    };

    RadioButton colorButton[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new DrawModel(this);

        // draw view
        drawview = (DrawView) findViewById(R.id.drawview);
        drawview.setModel(model);
        drawview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0) { // selection
                        model.select(x, y);
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.createShape(x, y);
                        return true;
                    } else if (model.tool == 4){
                        model.setFilled(x, y);
                        return true;
                    } else if (model.tool == 5){
                        model.removeShape(x,y);
                        return true;
                    }
                }else if (e.getAction() == MotionEvent.ACTION_UP){
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0) { // selection
                        model.lightUnleash();
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.heavyUnleash();
                        return true;
                    } else if (model.tool == 4){
                        return true;
                    } else if (model.tool == 5){
                        return true;
                    }
                }else if (e.getAction() == MotionEvent.ACTION_MOVE){
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0 && model.getSelected() != null) { // selection
                        model.moveShape(x,y);
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.resizeSelected(x,y);
                        return true;
                    } else if (model.tool == 4){
                        return true;
                    } else if (model.tool == 5){
                        return true;
                    }
                }

                return false;
            }
        });

        // radiobuttons
        toolBar = (RadioButtonTable) findViewById(R.id.toolbar);
        toolBar.setActivity(this);
        toolBar.setActiveRadioButton( (RadioButton) findViewById(R.id.Selection));

        // color pallets
        colorPallet = (RadioButtonTable) findViewById(R.id.colorPallet);
        colorPallet.setActivity(this);
        colorPallet.setActiveRadioButton( (RadioButton) findViewById(R.id.black));

        colorButton = new RadioButton[colorID.length];

        // make 10 color button (change colors of the content programatically)
        for (int i = 0; i < colorID.length; i++){
            colorButton[i] = (RadioButton) findViewById(colorID[i]);
            // set the correct color
            GradientDrawable rect = (GradientDrawable) colorButton[i].getBackground();
            rect.setColor(color[i]);
        }

        // Line thickness button
        lineThickness = (RadioGroup) findViewById(R.id.thickselect);

        lineThickness.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                model.setThickness(getIndex(lineID, checkedId));
            }
        });

        // uilities
        undo = (Button) findViewById(R.id.undo);
        redo = (Button) findViewById(R.id.redo);
        clear = (Button) findViewById(R.id.clear);
        util = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Button) view).getId() == undo.getId()){
                    model.undo();
                }else if (((Button) view).getId() == redo.getId()){
                    model.redo();
                }else if (((Button) view).getId() == clear.getId()){
                    model.clear();
                }
            }
        };

        undo.setOnClickListener(util);
        redo.setOnClickListener(util);
        clear.setOnClickListener(util);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);


        // draw view
        drawview = (DrawView) findViewById(R.id.drawview);
        drawview.setModel(model);
        drawview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0) { // selection
                        model.select(x, y);
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.createShape(x, y);
                        return true;
                    } else if (model.tool == 4){
                        model.setFilled(x, y);
                        return true;
                    } else if (model.tool == 5){
                        model.removeShape(x,y);
                        return true;
                    }
                }else if (e.getAction() == MotionEvent.ACTION_UP){
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0) { // selection
                        model.lightUnleash();
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.heavyUnleash();
                        return true;
                    } else if (model.tool == 4){
                        return true;
                    } else if (model.tool == 5){
                        return true;
                    }
                }else if (e.getAction() == MotionEvent.ACTION_MOVE){
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    // calls upon the model
                    if (model.tool == 0 && model.getSelected() != null) { // selection
                        model.moveShape(x,y);
                        return true;
                    } else if (model.tool >= 1 && model.tool <= 3){
                        model.resizeSelected(x,y);
                        return true;
                    } else if (model.tool == 4){
                        return true;
                    } else if (model.tool == 5){
                        return true;
                    }
                }

                return false;
            }
        });

        // radiobuttons
        int ct = model.tool;
        toolBar = (RadioButtonTable) findViewById(R.id.toolbar);
        toolBar.setActivity(this);
        toolBar.setActiveRadioButton( (RadioButton) findViewById(R.id.Selection));

        // color pallets
        int cc = model.color;
        colorPallet = (RadioButtonTable) findViewById(R.id.colorPallet);
        colorPallet.setActivity(this);
        colorPallet.setActiveRadioButton((RadioButton) findViewById(R.id.black));

        colorButton = new RadioButton[colorID.length];

        // make 10 color button (change colors of the content programatically)
        for (int i = 0; i < colorID.length; i++){
            colorButton[i] = (RadioButton) findViewById(colorID[i]);
            // set the correct color
            GradientDrawable rect = (GradientDrawable) colorButton[i].getBackground();
            rect.setColor(color[i]);
        }

        // Line thickness button
        lineThickness = (RadioGroup) findViewById(R.id.thickselect);
        lineThickness.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                model.setThickness(getIndex(lineID, checkedId));
            }
        });

        // uilities
        undo = (Button) findViewById(R.id.undo);
        redo = (Button) findViewById(R.id.redo);
        clear = (Button) findViewById(R.id.clear);
        util = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Button) view).getId() == undo.getId()){
                    model.undo();
                }else if (((Button) view).getId() == redo.getId()){
                    model.redo();
                }else if (((Button) view).getId() == clear.getId()){
                    model.clear();
                }
            }
        };

        undo.setOnClickListener(util);
        redo.setOnClickListener(util);
        clear.setOnClickListener(util);

        // set correct state for each bar
        lineThickness.check(lineID[model.thickness]);
        toolBar.setActiveRadioButton( (RadioButton)findViewById(toolID[model.tool]));
        colorPallet.setActiveRadioButton( (RadioButton)findViewById(colorID[model.color]));
    }

    public void notifyView(){
        drawview.invalidate();
    }
    public void setButtonState(){
        colorPallet.setActiveRadioButton((RadioButton)findViewById(colorID[model.color]));
        lineThickness.check(lineID[model.thickness]);
    }

    // Modify the model
    public void makeChange(){

        // set Tool Selection
        int toolid = getIndex(toolID,toolBar.getCheckedRadioButtonId());
        model.setTool(toolid);

        // set Color selection
        int colorid = getIndex(colorID, colorPallet.getCheckedRadioButtonId());
        model.setColor(colorid);
        // set Line Thickness
    }

    public void changeBackGroundImage(int id, boolean turnOn){
        // check if the request item belongs to tool
        if (include(toolID, id)){
            RadioButton bt = (RadioButton) findViewById(id);
            int index = getIndex(toolID, id);
            bt.setBackgroundResource(turnOn ? tool1[index] : tool0[index]);
        }
    }

    public int getIndex(int array[], int val){
        int j = 0;
        for (int i:array){
            if (i == val)
                return j;
            j++;
        }
        return -1;
    }

    public boolean include(int array[], int val){
        for (int i:array){
            if (i == val)
                return true;
        }

        return false;
    }
}
