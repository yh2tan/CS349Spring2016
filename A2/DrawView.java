import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.Point2D;

class DrawView extends JPanel implements IView{

    DrawModel model;
    double scale = 1;
    static LinearGradientPaint SelectedBorder;

    public DrawView() {
        super();

        // create a repeating stripe of gray and white
        Point2D start = new Point2D.Double(0, 0);
        Point2D end = new Point2D.Double(10, 10);
        float[] dist = {0.0f, 0.5f};
        Color[] colors = {Color.gray, Color.white};
        SelectedBorder= new LinearGradientPaint(start, end, dist, colors, MultipleGradientPaint.CycleMethod.REPEAT);

        DrawInteraction mouseEvent = new DrawInteraction();
        addMouseListener(mouseEvent);
        addMouseMotionListener(mouseEvent);
    }

    public void notifyView(){
        repaint();
    }

    public void addModel(DrawModel model){
        this.model = model;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // white background
        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());

        //print shapes
        Xshape selected = model.getSelected();
        ArrayDeque<Xshape> shapes = model.getAllShape();

        while (!shapes.isEmpty()){
            Xshape shape = shapes.pollLast();
            shape.draw(g2, scale);
        }

        if (selected != null)
            selected.paintSelectedBorder(g2, scale);
    }

    public void rescale(double s){
        scale = s;
    }

    class DrawInteraction extends MouseAdapter{

        boolean pressed = false;
        int currentTool = 0;

        public void mousePressed(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            pressed = true;

            currentTool = model.getTool();

            if (currentTool == 0){
                model.selectShape((double)x, (double)y, scale);
            }else if (currentTool == 1){
                model.removeShape((double) x, (double) y, scale);
            }else if (currentTool >=  2 && currentTool <= 4){
                // create a abstractShape centered at (0,0) with no length
                model.createShape(x, y, scale);
            }else if (currentTool == 5){
                model.setFilled((double)x, (double)y, scale);
            }
        }

        public void mouseReleased(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            currentTool = model.getTool();
            pressed = false;

            if (currentTool == 0){
                model.lightUnleash();
            }else if (currentTool == 1){
                // no event happens
            }else if (currentTool >=  2 && currentTool <= 4){
                // unleash the shape
                model.unleash();
            }else if (currentTool == 5) {
                // No event happens
            }
        }

        public void mouseDragged(MouseEvent e){

            int x = e.getX();
            int y = e.getY();

            currentTool = model.getTool();

            if (currentTool == 0 && model.getSelected() != null) {
                model.moveShape(x, y, scale);
            } else if (currentTool == 1) {
                // no event happens
            } else if (currentTool >= 2 && currentTool <= 4) {
                model.setCurrentSize((double) x, (double) y, scale);
            } else if (currentTool == 5) {
                // No event happens
            }
        }
    }
}