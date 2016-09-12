import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JSketch {

    public static void main (String[] args){
        MainFrame frame = new MainFrame();
        DrawModel m = new DrawModel();
        frame.addModel(m);
    }
}
