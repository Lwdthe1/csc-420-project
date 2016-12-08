package views.subviews;

import javax.swing.*;

import models.CurrentUser;
import models.UserRestCallResult;
import models.UserSettingsRestCallResult;
import net.miginfocom.swing.MigLayout;
import org.apache.http.HttpException;
import utils.ImageUtils;
import utils.WebService.RestCaller;
import views.LoggedOutActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by keithmartin on 10/23/16.
 */
public class NavBarView {

    private final int MAX_PROFILE_ICON_SIZE = 35;

    private JPanel navBarPanel;
    private JLabel mediumIconImgLabel;
    private JLabel superMeditorLabel;
    private JButton publicationsTabButton;
    private JButton notificationsButton;
    private JButton profileButton;
    private JButton settingsButton;

    // login
    JTextField username = new JTextField();
    JTextField password = new JPasswordField();

    Object[] loginObjects = {
            "Username:", username,
            "Password:", password
    };

    // settings
    JCheckBox instantMessageCB = new JCheckBox();
    JCheckBox statusRequestCB = new JCheckBox();
    JPanel instantMessagePanel = new JPanel();
    JPanel statusRequestPanel = new JPanel();

    private ActionListener loggedInActionListener;
    private ActionListener loggedOutActionListener;


    public NavBarView(int frameWidth, LoggedOutActionListener loggedOutActionListener) {

        loggedOutActionListener.setNavBarView(this);
        this.loggedOutActionListener = loggedOutActionListener;

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
        settingsButton = new JButton();
        setSettingsButton();
        setUpSettingPanels();
        setActionListeners();
        setLayout();
        addComponents();
    }

    private void initSettings(){
        try {
            RestCaller.sharedInstance.getCurrentUserSettings();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
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
        notificationsButton.setMaximumSize(new Dimension(MAX_PROFILE_ICON_SIZE, MAX_PROFILE_ICON_SIZE));
        notificationsButton.setIcon(new ImageIcon("Images/Notifications.png", null));
        notificationsButton.setBorderPainted(false);
    }

    private void setSettingsButton() {
        settingsButton.setSize(new Dimension(100, 100));
        settingsButton.setIcon(new ImageIcon("Images/settings_icon.png", null));
        settingsButton.setBorderPainted(false);
        settingsButton.setEnabled(false);
        settingsButton.setVisible(false);
    }

    private void setProfileButton() {
        profileButton.setMaximumSize(new Dimension(100, 100));
        profileButton.setBorderPainted(false);
        profileButton.setIcon(new ImageIcon("Images/Profile.png", null));
    }

    private void setActionListeners(){
        loggedInActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Logout?", "logout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    CurrentUser.sharedInstance.logout();
                    toggleLoggedStatus();
                }
            }
        };

        profileButton.addActionListener(loggedOutActionListener);

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserSettingsRestCallResult userSettingsRestCallResult = RestCaller.sharedInstance.getCurrentUserSettings();
                    instantMessageCB.setSelected(userSettingsRestCallResult.getInstantMessage());
                    statusRequestCB.setSelected(userSettingsRestCallResult.getStatusRequest());
                    int option = JOptionPane.showConfirmDialog(null, getSettingsObjects(), "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        Boolean instantMessage = instantMessageCB.isSelected();
                        Boolean statusRequest = statusRequestCB.isSelected();

                        if(instantMessage != userSettingsRestCallResult.getInstantMessage()){
                            RestCaller.sharedInstance.updateCurrentUserSettings(UserSettingsRestCallResult.new_message_notification_instant,instantMessage);
                        }

                        if(statusRequest != userSettingsRestCallResult.getStatusRequest()){
                            RestCaller.sharedInstance.updateCurrentUserSettings(UserSettingsRestCallResult.contribution_request_decision,statusRequest);
                        }

                    }
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (HttpException e1) {
                    e1.printStackTrace();
                }
            }

        });

    }

    private void setLayout() {
        navBarPanel.setLayout(new MigLayout("", // Layout Constraints
                "[][][][][]440[]", // Column constraints with default align
                "")); // Row constraints
    }

    private void addComponents() {
        navBarPanel.add(mediumIconImgLabel, "cell 0 0");
        navBarPanel.add(superMeditorLabel, "cell 1 0");
        navBarPanel.add(publicationsTabButton, "cell 2 0");
        navBarPanel.add(notificationsButton, "cell 3 0");
        navBarPanel.add(settingsButton, "cell 4 0");
        navBarPanel.add(profileButton);
    }

    public JPanel getContainer() {
        return navBarPanel;
    }

    public JButton getPublicationsTabButton() {
        return publicationsTabButton;
    }

    public Object[] getSettingsObjects(){
        Object[] objects = new Object[2];
        objects[0] = instantMessagePanel;
        objects[1] = statusRequestPanel;
        return objects;
    }

    public void setUpSettingPanels() {
        instantMessagePanel.setLayout(new BoxLayout(instantMessagePanel,BoxLayout.LINE_AXIS));
        instantMessagePanel.add(new JLabel("Recieve instant message notifications"));
        instantMessagePanel.add(instantMessageCB);
        statusRequestPanel.setLayout(new BoxLayout(statusRequestPanel,BoxLayout.LINE_AXIS));
        statusRequestPanel.add(new JLabel("Status of Your Requests to Contribute"));
        statusRequestPanel.add(statusRequestCB);
    }

    public void toggleLoggedStatus(){
        settingsButton.setEnabled(!settingsButton.isEnabled());
        settingsButton.setVisible(!settingsButton.isVisible());
        boolean isLoggedIn = CurrentUser.sharedInstance.getIsLoggedIn();
        profileButton.removeActionListener(!isLoggedIn ? loggedInActionListener : loggedOutActionListener);
        profileButton.addActionListener(isLoggedIn ? loggedInActionListener : loggedOutActionListener);
        if(isLoggedIn){
            String imageUrl = CurrentUser.sharedInstance.getUser().getImageUrl();
            Image resizedImage =
                    ImageUtils.loadImage(imageUrl).getScaledInstance(MAX_PROFILE_ICON_SIZE, MAX_PROFILE_ICON_SIZE, Image.SCALE_FAST);
            ImageIcon icon = new ImageIcon(resizedImage);
            profileButton.setIcon(icon);
        } else {
            profileButton.setIcon(new ImageIcon("Images/Profile.png", null));
        }
    }

}
