import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

class Tools extends JInternalFrame implements IView{

    //Group of Buttons
    ToolButtons TG[]= new ToolButtons[6];
    ButtonGroup Tgroup;
    private String text[] = {
        "select", "erase", "line", "rect", "circle", "fill",
    };

    DrawModel dmodel;

    public Tools(){
        super("", false, false, false, false);

        setLayout(new GridLayout(3, 2));

        Tgroup = new ButtonGroup();

        for (int i = 0 ; i < 6 ; i++){
            TG[i] = new ToolButtons(text[i],i);
            this.add(TG[i]);
            Tgroup.add(TG[i]);
        }

        TG[0].setSelected(true);
    }

    public void notifyView() {}

    public void addModel(DrawModel model){
        this.dmodel = model;
    }

    class ToolButtons extends JToggleButton{

        Image black;
        Image blue;

        String tag;
        int tool;

        public ToolButtons(String name, int tool){
            super();

            tag = name;
            this.tool = tool;

            // create two static icons for the two state
            try {
                black = ImageIO.read(new File("black/" + name + ".png"));
                blue = ImageIO.read(new File("blue/" + name + ".png"));
            }catch (IOException e){

            }

            setPreferredSize(new Dimension(40,40));
            setBorder(BorderFactory.createRaisedBevelBorder());

            this.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    Tgroup.clearSelection();
                    Tgroup.setSelected(getModel(), true);
                    setSelected(!isSelected());
                    dmodel.setDrawTool(tool);
                    repaint();
                }
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(250, 250, 250));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(isSelected()?blue:black, 3 , 3 , null);
            setBorder(isSelected()? BorderFactory.createLoweredBevelBorder():BorderFactory.createRaisedBevelBorder());
        }
    }



}