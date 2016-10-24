import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

/**
 * Created by keithmartin on 10/23/16.
 */
public class NavBarView {

    private static JPanel navBarPanel;
    private static JLabel mediumIconImgLabel;
    private static JLabel superMeditorLabel;
    private static JButton publicationsTabButton;
    private static JButton advertiseTabButton;
    private static JButton notificationsButton;
    private static JButton profileButton;

    public NavBarView(int frameWidth) {
        navBarPanel = new JPanel();
        setNavBarPanel(frameWidth);
        mediumIconImgLabel = new JLabel();
        setMediumIconImgLabel();
        superMeditorLabel = new JLabel();
        setSuperMeditorLabel();
        publicationsTabButton = new JButton();
        setPublicationsTabButton();
        advertiseTabButton = new JButton();
        setAdvertiseTabButton();
        notificationsButton = new JButton();
        setNotificationsButton();
        profileButton = new JButton();
        setProfileButton();
        setLayout();
        addComponents();
    }

    private static void setNavBarPanel(int frameWidth) {
        navBarPanel.setSize(new Dimension(frameWidth, 300));
        navBarPanel.setBackground(Color.white);
    }

    private static void setMediumIconImgLabel() {
        mediumIconImgLabel.setSize(new Dimension(100, 100));
        mediumIconImgLabel.setIcon(new ImageIcon("Images/SuperMeditorLogo.png", null));
    }

    private static void setSuperMeditorLabel() {
        superMeditorLabel.setSize(new Dimension(100, 100));
        superMeditorLabel.setText("SuperMeditor");
        superMeditorLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        superMeditorLabel.setForeground(Color.black);
    }

    private static void setPublicationsTabButton() {
        publicationsTabButton.setSize(new Dimension(100, 100));
        publicationsTabButton.setText("Publications");
        publicationsTabButton.setForeground(Color.gray);
        publicationsTabButton.setBorderPainted(false);
    }

    private static void setAdvertiseTabButton() {
        advertiseTabButton.setSize(new Dimension(100, 100));
        advertiseTabButton.setText("Advertise Mine");
        advertiseTabButton.setForeground(Color.gray);
        advertiseTabButton.setBorderPainted(false);
    }

    private static void setNotificationsButton() {
        notificationsButton.setSize(new Dimension(100, 100));
        notificationsButton.setIcon(new ImageIcon("Images/Notifications.png", null));
        notificationsButton.setBorderPainted(false);
    }

    private static void setProfileButton() {
        profileButton.setSize(new Dimension(100, 100));
        profileButton.setIcon(new ImageIcon("Images/Profile.png", null));
        profileButton.setBorderPainted(false);
    }

    private static void setLayout() {
        navBarPanel.setLayout(new MigLayout("", // Layout Constraints
                "[][][][][]700[]", // Column constraints with default align
                "")); // Row constraints
    }

    private static void addComponents() {
        navBarPanel.add(mediumIconImgLabel, "cell 0 0");
        navBarPanel.add(superMeditorLabel, "cell 1 0");
        navBarPanel.add(publicationsTabButton, "cell 2 0");
        navBarPanel.add(advertiseTabButton, "cell 3 0");
        navBarPanel.add(notificationsButton, "cell 4 0");
        navBarPanel.add(profileButton);
    }

    public JPanel getNavBarPanel() {
        return navBarPanel;
    }

    public JButton getPublicationsTabButton() {
        return publicationsTabButton;
    }

    public JButton getAdvertiseTabButton() {
        return advertiseTabButton;
    }
}
