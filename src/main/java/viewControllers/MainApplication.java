package viewControllers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class MainApplication {
    JFrame mainFrame;
    public MainApplication() {
        this.mainFrame = new JFrame("SuperSwingMeditor");
        //Create and set up the window.
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.

        mainFrame.setSize(new Dimension(900,900));
        mainFrame.setBackground(Color.white);

        //frame.setMinimumSize(new Dimension(500,500));
        //frame.setUndecorated(true);
        mainFrame.setVisible(true);

        addViewControllers();
    }

    private void addViewControllers() {
        new HomeFeedViewController(this);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void navigate(JPanel panel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        panel.setVisible(true);
    }
}
