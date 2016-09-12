import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.File;

class Menu extends JMenuBar implements IView{

    //draw model
    DrawModel dmodel;

    // Menu Widgets
    JMenu file;
    JMenu view;
    JMenu drawView;
    JMenuItem fileItem[];
    JMenuItem viewItem[];

    // Use for Saving/Loading file
    JFileChooser fileChooser;

    ButtonGroup dv;
    MainFrame mainFrame;

    public Menu(MainFrame main){
        super();

        // create a reference to the main display
        mainFrame = main;

        // file
        file = new JMenu("File");
        fileItem = new JMenuItem[3];

        fileItem[0] = new JMenuItem("New");
        fileItem[1] = new JMenuItem("Save");
        fileItem[2] = new JMenuItem("Load");

        fileChooser = new JFileChooser();

        // We use file extension ximage:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Custom Image","ximage");
        fileChooser.setFileFilter(filter);

        for (JMenuItem i:fileItem){
            file.add(i);
            i.addActionListener(new FileHandler());
        }

        this.add(file);

        //view
        view = new JMenu("View");
        viewItem = new JMenuItem[3];

        //sub-menu for image format
        drawView = new JMenu("Draw View Options");
        dv = new ButtonGroup();

        viewItem[0] = new JRadioButtonMenuItem("Full Size");
        viewItem[1] = new JRadioButtonMenuItem("Fit to Window");
        //add button groups
        drawView.add(viewItem[0]);
        drawView.add(viewItem[1]);
        dv.add(viewItem[0]);
        dv.add(viewItem[1]);
        viewItem[0].setSelected(true);


        viewItem[2] = new JCheckBoxMenuItem("Lock Contents", true);
        view.add(drawView);
        view.add(viewItem[2]);

        this.add(view);

        ViewRadioButton rblistener = new ViewRadioButton();

        viewItem[0].addActionListener(rblistener);
        viewItem[1].addActionListener(rblistener);

        // add listener to Lock Contents button
        viewItem[2].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if  (viewItem[2].isSelected()){
                    mainFrame.lockComponents();
                }else {
                    mainFrame.unlockComponents();
                }
            }
        }); // end of addItemListener
    }

    public void notifyView(){}

    public void addModel(DrawModel model){
        this.dmodel = model;
    }

    class ViewRadioButton implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if ("Full Size".equals(e.getActionCommand())){
                mainFrame.setProportionalDisplay(false);
            }else {
                mainFrame.setProportionalDisplay(true);
            }
        }
    }

    class FileHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if ("New".equals(e.getActionCommand())){
                newFlow();
            }else if ("Save".equals(e.getActionCommand())) {
                saveFlow();
            }else{
                loadFlow();
            }
        }
    }

    // save action
    public void saveFlow(){
        int result = fileChooser.showSaveDialog(mainFrame);

        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            dmodel.saveFile(selectedFile);
        }
    }

    // load actoin
    public void loadFlow(){
        String message = "Do you want to save?";
        int option = JOptionPane.showConfirmDialog(mainFrame, message, "alert", JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == 0){
            saveFlow();
        }else if (option == 2){
            return ;
        }

        int result = fileChooser.showOpenDialog(mainFrame);

        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            dmodel.loadFile(selectedFile);
        }
    }

    // new action
    public void newFlow(){
        String message = "Do you want to save?";
        int option = JOptionPane.showConfirmDialog(mainFrame, message, "alert", JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == 0){
            saveFlow();
        }else if (option == 2){
            return ;
        }

        dmodel.clear();
    }
}
