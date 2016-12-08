package viewControllers;

import viewControllers.interfaces.AppView;
import viewControllers.interfaces.ViewController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class MainApplication {
    private JFrame mainFrame;
    private JPanel currentVisibleView;
    private HomeFeedViewController homeFeedViewController;
    private UserProfileViewController userProfileController;

    public MainApplication() {
        this.mainFrame = new JFrame("SuperSwingMeditor");
        //Create and set up the window.
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainFrame.setSize(new Dimension(900,900));
        mainFrame.setBackground(Color.white);
        mainFrame.setVisible(true);

        moveToMainViewController();
    }

    private void moveToMainViewController() {
        homeFeedViewController = new HomeFeedViewController(this);
        setVisibleView(homeFeedViewController);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setVisibleView(ViewController viewController) {
        if (currentVisibleView != null) {
            mainFrame.getContentPane().remove(currentVisibleView);
        }
        JPanel contentPane = viewController.getView().getContentPane();
        mainFrame.getContentPane().add(contentPane);
        homeFeedViewController.viewWillAppear();
        contentPane.setVisible(true);

        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();   // This is required in some cases
        currentVisibleView = contentPane;
    }

    public HomeFeedViewController getHomeFeedViewController() {
        return homeFeedViewController;
    }

    public UserProfileViewController getUserProfileViewController() {
        if (userProfileController == null) {
            userProfileController = new UserProfileViewController(this);
        }
        return userProfileController;
    }
}
