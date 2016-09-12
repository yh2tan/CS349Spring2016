import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import javax.swing.Box;

// The Sketch Panel is only used to container to adjust layout of the app.
//Thus, It doesnot offcially contains the model.
class SketchPanel extends JPanel {

    DrawView drawView;
    JPanel outterFrame;

    int xleftOver = 0;
    Component LeftPadding;

    private final double DRAWFRAMEX = 600.0 ;
    private final double DRAWFRAMEY = 450.0;

    public SketchPanel(DrawView paintboard){
        super();

        drawView = paintboard;

        this.setLayout(new GridBagLayout());
        this.setLocation(0,0);
        this.setSize(800,600 - 44);

        outterFrame = new JPanel() {

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.gray);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        outterFrame.setLayout(new FlowLayout());
        outterFrame.add(Box.createRigidArea(new Dimension(120, 0)));
        outterFrame.add(drawView);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        this.add(Box.createGlue(), gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;

        this.add(outterFrame, gbc);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void childreanResize(int x, int y, boolean isProportional){

        if (!isProportional)
            return;

        double xPortion = x/800.0;
        double yPortion = y/600.0;
        double min= xPortion < yPortion ? xPortion : yPortion;

        drawView.setPreferredSize(new Dimension ((int)(DRAWFRAMEX*min), (int)(DRAWFRAMEY*min)));

        drawView.rescale(min);
    }
}
