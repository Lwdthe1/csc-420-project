import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

public class View {
    private static JLabel imgLabel;
    private static JComboBox comboBox;
    private static JFrame frame;

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("SuperSwingMeditor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(new Dimension(700,700));
        //frame.setMinimumSize(new Dimension(500,500));
        //frame.setUndecorated(true);
        frame.setVisible(true);
        addComponentsToPane(frame.getContentPane());
        new ViewController(comboBox, imgLabel);
    }

    public static void addComponentsToPane(Container contentPane) {
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        contentPane.add(leftPanel, BorderLayout.WEST);
        contentPane.add(rightPanel, BorderLayout.EAST);

        leftPanel.setSize(new Dimension(100, frame.getHeight()));
        rightPanel.setSize(frame.getWidth() - leftPanel.getWidth(), frame.getHeight() - 50);

        //setup left panel
        comboBox = new JComboBox();
        comboBox.setSize(100, 150);
        leftPanel.add(comboBox);

        //setup right panel
        imgLabel = new JLabel();
        Dimension imgLabelDims = new Dimension(rightPanel.getWidth(), rightPanel.getHeight());
        imgLabel.setMinimumSize(imgLabelDims);
        imgLabel.setPreferredSize(imgLabelDims);
        imgLabel.setMaximumSize(imgLabelDims);
        imgLabel.setSize(imgLabelDims);
        rightPanel.add(imgLabel);

    }
}