package viewControllers;

import viewControllers.interfaces.AppView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class MainApplication {
    JFrame mainFrame;
    private JPanel currentVisibleView;

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
        setVisibleView(new HomeFeedViewController(this).getView());

    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setVisibleView(AppView panel) {
        if (currentVisibleView != null) {
            mainFrame.getContentPane().remove(currentVisibleView);
        }
        JPanel contentPane = panel.getContentPane();
        mainFrame.getContentPane().add(contentPane);
        contentPane.setVisible(true);

        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();   // This is required in some cases
        currentVisibleView = contentPane;
    }
}
