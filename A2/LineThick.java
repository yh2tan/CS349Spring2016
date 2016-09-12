import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

class LineThick extends JInternalFrame implements IView{

    //Group of Buttons
    Lines TG[]= new Lines[4];
    ButtonGroup Tgroup;
    DrawModel dmodel;

    public LineThick(){
        super("", false, false, false, false);

        setLayout(new GridLayout(4, 1));

        Tgroup = new ButtonGroup();

        for (int i = 0 ; i < 4 ; i++){
            TG[i] = new Lines(i+1);
            this.add(TG[i]);
            Tgroup.add(TG[i]);
        }

        TG[0].setSelected(true);
    }

    public void addModel(DrawModel model){
        this.dmodel = model;
    }

    public void notifyView() {
        Tgroup.clearSelection();
        Tgroup.setSelected(TG[dmodel.lineThick-1].getModel(), true);
        TG[dmodel.lineThick-1].setSelected(!isSelected());
        repaint();
    }

    class Lines extends JToggleButton{

        Stroke stroke;
        int tag;

        public Lines (int s){
            super();

            stroke = new BasicStroke(s);
            tag = s;

            setPreferredSize(new Dimension(120,25));
            setBorder(null);

            this.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    Tgroup.clearSelection();
                    Tgroup.setSelected(getModel(), true);
                    setSelected(!isSelected());
                    dmodel.setStroke(tag);
                    repaint();
                }
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(stroke);
            g2.setColor(Color.black);
            g2.drawLine(10, 12, getWidth()-10, 12);

            if (isSelected()){
                setBorder(BorderFactory.createLoweredBevelBorder());
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.lightGray);
                g2.drawRect(2,2,getWidth()-6, getHeight()-6);
            }else{
                setBorder(null);
            }
        }
    }
}