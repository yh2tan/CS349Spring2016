import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import javax.swing.Box;

class MainFrame extends JFrame implements IView{

    //Draw Model
    DrawModel model;

    //Menus
    Menu menubar;

    //Views:
    JDesktopPane desktop; // used to organize dockability, does not use IView
    Tools toolFrame;
    ColorPallet color;
    LineThick thickness;
    DrawView image;

    //MousMotionListeners
    MouseMotionListener[] toolLock;
    MouseMotionListener[] colorLock;
    MouseMotionListener[] lineLock;

    //Additional Attribute
    int cWidth;
    int cHeight;

    //sketch panel
    SketchPanel sketchPanel;

    //Resizing Attributes
    boolean proportionalDisplay; // used to specifies view mode

    public MainFrame(){

        // create windows
        super("JSketch");

        // create menu item: file
        menubar = new Menu(this);

        //Internal Frames(View Models)
        toolFrame = new Tools();
        toolFrame.setVisible(true);
        toolFrame.setSize(120, 180);

        color = new ColorPallet();
        color.setVisible(true);
        color.setSize(120, 200);

        thickness = new LineThick();
        thickness.setVisible(true);
        thickness.setSize(120, 150);

        // draw viwe is a JPanel
        image = new DrawView();
        image.setPreferredSize(new Dimension (600, 450) );
        image.setMinimumSize(new Dimension (600, 450) );
        //image.setLocation(150, 50);

        // Create the desktop
        desktop = new CustomDesktop();

        this.setContentPane(desktop);
        this.setJMenuBar(menubar);

        // Lock toolbars
        toolLock = (MouseMotionListener[]) toolFrame.getListeners(MouseMotionListener.class);
        colorLock = (MouseMotionListener[]) color.getListeners(MouseMotionListener.class);
        lineLock = (MouseMotionListener[]) thickness.getListeners(MouseMotionListener.class);

        lockComponents();

        // Set windows configuration
        proportionalDisplay = false;
        cHeight = 600;
        cWidth = 800;

        this.setSize(800,600);
        this.setMinimumSize(new Dimension(800,600)); // create minimum size
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addComponentListener(new ResizeEventHandler());
    }

    public void addModel(DrawModel m){
        model = m;
        model.setDrawCanvas(image);
        model.addIViews(color);
        model.addIViews(thickness);

        // below classes uses IView
        image.addModel(m);
        menubar.addModel(m);
        thickness.addModel(m);
        color.addModel(m);
        toolFrame.addModel(m);
    }

    public void setProportionalDisplay(boolean b){
        image.setPreferredSize(new Dimension (600, 450));
        image.rescale(1);
        this.setSize(800, 600);
        proportionalDisplay = b;
    }

    public void notifyView(){}

    // remove all InnerFrame MouseMotionListeners to lock the components
    public void lockComponents(){

        for (MouseMotionListener listener : toolLock){
            toolFrame.removeMouseMotionListener(listener);
        }

        for (MouseMotionListener listener : colorLock){
            color.removeMouseMotionListener(listener);
        }

        // Thickness pallet
        for (MouseMotionListener listener : lineLock)
            thickness.removeMouseMotionListener(listener);

        //reposition the components:
        toolFrame.setLocation(0,0);
        color.setLocation(0, 180);
        thickness.setLocation(0,380);
    }

    // retrieve all InnerFrame MousMotionListener to support draggable content
    public void unlockComponents(){

        // Toolbar
        for (MouseMotionListener listener : toolLock)
            toolFrame.addMouseMotionListener(listener);

        // Color pallet
        for (MouseMotionListener listener : colorLock)
            color.addMouseMotionListener(listener);

        // Thickness pallet
        for (MouseMotionListener listener : lineLock)
            thickness.addMouseMotionListener(listener);
    }

    //Resizes the JInnerFrames
    public void childrenResize(){
        sketchPanel.childreanResize(getWidth(), getHeight(), proportionalDisplay);
    }


    // Override Component Listener to perform resizing actions
    class ResizeEventHandler extends ComponentAdapter {

        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            childrenResize();
        }
    }

    // Inner Classes to Support undockable Tool bars
    // Desktop Inner Class
    class CustomDesktop extends JDesktopPane{

        public CustomDesktop(){
            super();

            sketchPanel = new SketchPanel(image);

            this.setLayout(new BorderLayout());
            this.add(toolFrame);
            this.add(color);
            this.add(thickness);
            this.add(sketchPanel, BorderLayout.CENTER);
            this.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        }

        // make a grey background
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.gray);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
