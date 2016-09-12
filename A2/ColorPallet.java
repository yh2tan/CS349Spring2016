import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

class ColorPallet extends JInternalFrame implements IView{

    //Group of Buttons
    ColorBox TG[]= new ColorBox[15];
    ButtonGroup Tgroup;
    static Color colors [] = {
            Color.black, Color.white,
            Color.blue, Color.cyan, Color.green,
            Color.magenta, Color.orange, Color.pink,
            Color.red, Color.yellow,
            new Color(216, 13, 131),
            new Color(65, 157, 223),
            new Color(90, 130, 234),
            new Color(46, 156, 240),
            new Color(149, 222, 33)
    };

    DrawModel dmodel;

    public ColorPallet(){
        super("", false, false, false, false);

        setLayout(new GridLayout(5, 3));

        Tgroup = new ButtonGroup();

        for (int i = 0 ; i < 15 ; i++){
            TG[i] = new ColorBox(colors[i], i);
            this.add(TG[i]);
            Tgroup.add(TG[i]);
        }

        TG[0].setSelected(true);
    }

    public void notifyView() {
        Tgroup.clearSelection();
        Tgroup.setSelected(TG[dmodel.color].getModel(), true);
        TG[dmodel.color].setSelected(!isSelected());
        repaint();
    }

    public void addModel(DrawModel model){
        this.dmodel = model;
    }

    class ColorBox extends JToggleButton{

        Color c;
        int tag;

        public ColorBox( Color c, int num){
            super();

            this.c = c;
            tag = num;

            setPreferredSize(new Dimension(30,30));
            setBorder(BorderFactory.createLoweredBevelBorder());

            this.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    Tgroup.clearSelection();
                    Tgroup.setSelected(getModel(), true);
                    setSelected(!isSelected());
                    dmodel.setColor(tag);
                    repaint();
                }
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(c);
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (isSelected()){
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.lightGray);
                g2.drawRect(2,2,getWidth()-6, getHeight()-6);
            }
        }
    }
}