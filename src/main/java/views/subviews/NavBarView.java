package views.subviews;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

/**
 * Created by keithmartin on 10/23/16.
 */
public class NavBarView {

    private  JPanel navBarPanel;
    private  JLabel mediumIconImgLabel;
    private  JLabel superMeditorLabel;
    private  JButton publicationsTabButton;
    private JButton notificationsButton;
    private JButton profileButton;

    public NavBarView(int frameWidth) {
        navBarPanel = new JPanel();
        setNavBarPanel(frameWidth);
        mediumIconImgLabel = new JLabel();
        setMediumIconImgLabel();
        superMeditorLabel = new JLabel();
        setSuperMeditorLabel();
        publicationsTabButton = new JButton();
        setPublicationsTabButton();
        notificationsButton = new JButton();
        setNotificationsButton();
        profileButton = new JButton();
        setProfileButton();
        setLayout();
        addComponents();
    }

    private  void setNavBarPanel(int frameWidth) {
        navBarPanel.setSize(new Dimension(frameWidth, 300));
        navBarPanel.setBackground(Color.white);
    }

    private void setMediumIconImgLabel() {
        mediumIconImgLabel.setSize(new Dimension(100, 100));
        mediumIconImgLabel.setIcon(new ImageIcon("Images/SuperMeditorLogo.png", null));
    }

    private void setSuperMeditorLabel() {
        superMeditorLabel.setSize(new Dimension(100, 100));
        superMeditorLabel.setText("SuperMeditor");
        superMeditorLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        superMeditorLabel.setForeground(Color.black);
    }

    private void setPublicationsTabButton() {
        publicationsTabButton.setSize(new Dimension(100, 100));
        publicationsTabButton.setText("Publications");
        publicationsTabButton.setForeground(Color.gray);
        publicationsTabButton.setBorderPainted(false);
    }

    private void setNotificationsButton() {
        notificationsButton.setSize(new Dimension(100, 100));
        notificationsButton.setIcon(new ImageIcon("Images/Notifications.png", null));
        notificationsButton.setBorderPainted(false);
    }

    private void setProfileButton() {
        profileButton.setSize(new Dimension(100, 100));
        profileButton.setIcon(new ImageIcon("Images/Profile.png", null));
        profileButton.setBorderPainted(false);
    }

    private void setLayout() {
        navBarPanel.setLayout(new MigLayout("", // Layout Constraints
                "[][][][]460[]", // Column constraints with default align
                "")); // Row constraints
    }

    private void addComponents() {
        navBarPanel.add(mediumIconImgLabel, "cell 0 0");
        navBarPanel.add(superMeditorLabel, "cell 1 0");
        navBarPanel.add(publicationsTabButton, "cell 2 0");
        navBarPanel.add(notificationsButton, "cell 3 0");
        navBarPanel.add(profileButton);
    }

    public JPanel getContainer() {
        return navBarPanel;
    }

    public JButton getPublicationsTabButton() {
        return publicationsTabButton;
    }
}