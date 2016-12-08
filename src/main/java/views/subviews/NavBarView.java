package views.subviews;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import viewControllers.NavigationController;
import viewControllers.interfaces.AppViewController;
import viewControllers.interfaces.View;
import viewControllers.interfaces.ViewController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by keithmartin on 10/23/16.
 */
public class NavBarView implements View {
    private NavigationController viewController;

    private  JPanel contentPane;
    private  JLabel mediumIconImgLabel;
    private  JLabel superMeditorLabel;
    private  JButton publicationsTabButton;

    private JButton profileButton;
    private int width;
    private final int height;

    public NavBarView(int frameWidth) {
        this.width = frameWidth;
        this.height = 300;
    }

    private  void setupContentPane() {
        contentPane = new JPanel();
        contentPane.setSize(new Dimension(this.width, height));
        contentPane.setBackground(Color.white);
    }

    private void setMediumIconImgLabel() {
        mediumIconImgLabel = new JLabel();
        mediumIconImgLabel.setSize(new Dimension(100, 100));
        mediumIconImgLabel.setIcon(new ImageIcon("Images/SuperMeditorLogo.png", null));
    }

    private void setSuperMeditorLabel() {
        superMeditorLabel = new JLabel();
        superMeditorLabel.setSize(new Dimension(100, 100));
        superMeditorLabel.setText("SuperMeditor");
        superMeditorLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        superMeditorLabel.setForeground(Color.black);
    }

    private void setPublicationsTabButton() {
        publicationsTabButton = new JButton();
        publicationsTabButton.setSize(new Dimension(100, 100));
        publicationsTabButton.setText("Publications");
        publicationsTabButton.setForeground(Color.gray);
        publicationsTabButton.setBorderPainted(false);
    }

    private void setProfileButton() {
        profileButton = new JButton();
        profileButton.setSize(new Dimension(100, 100));
        profileButton.setIcon(new ImageIcon("Images/Profile.png", null));
        profileButton.setBorderPainted(false);
    }

    private void setLayout() {
        contentPane.setLayout(new MigLayout(// Layout Constraints
                "",
                "[][][]500[]", // Column constraints
                "[]20[]")); // Row constraints // Row constraints
    }

    private void addComponents() {
        contentPane.add(mediumIconImgLabel, "cell 0 0");
        contentPane.add(superMeditorLabel, "cell 1 0");
        contentPane.add(publicationsTabButton, "cell 2 0");
        contentPane.add(profileButton, "pushx, growx");
    }

    public JButton getPublicationsTabButton() {
        return publicationsTabButton;
    }

    public JButton getProfileButton() {
        return profileButton;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void createAndShow() {
        setupContentPane();
        setMediumIconImgLabel();
        setSuperMeditorLabel();
        setPublicationsTabButton();
        setProfileButton();
        setLayout();
        addComponents();
        setButtonHoverListeners();
    }

    @Override
    public ViewController getViewController() {
        return this.viewController;
    }

    @Override
    public JPanel getContentPane() {
        return this.contentPane;
    }

    private void setButtonHoverListeners() {
        publicationsTabButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                publicationsTabButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                publicationsTabButton.setForeground(Color.GRAY);
            }
        });
    }
}
