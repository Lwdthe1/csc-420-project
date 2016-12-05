package viewControllers;

import views.NavBarView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class MainApplication {
    private JFrame mainFrame;
    private NavBarView navBarView;


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

        addNavBar();
        addViewControllers();
    }

    private void addNavBar() {
        navBarView = new NavBarView(mainFrame.getWidth());
        mainFrame.getContentPane().add(navBarView.getContainer(), BorderLayout.NORTH);
    }

    private void addViewControllers() {
        new HomeFeedViewController(this);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public NavBarView getNavBarView() { return navBarView; }

    public void navigate(JPanel removedPanel, JPanel panel) {
        if (removedPanel != null) {
            mainFrame.getContentPane().remove(removedPanel);
        }
        mainFrame.getContentPane().add(panel);
        panel.setVisible(true);
    }
}
