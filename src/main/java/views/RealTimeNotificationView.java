package views;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import utils.AppUtils;
import utils.ImageUtils;
import utils.TimeUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Created by lwdthe1 on 10/23/16.
 */
public class RealTimeNotificationView {
    private  JPanel containerPanel;
    private  JLabel imageLabel;
    private  JLabel descriptionLabel, titleLabel;
    private  JButton actionButton;
    private Semaphore updateSemaphore = new Semaphore(1);

    public RealTimeNotificationView(int frameWidth) {
        containerPanel = new JPanel();
        setContainerPanel(frameWidth);

        setMediumIconImgLabel();
        setupTitleLabel();
        setupDescriptionLabel();
        setupActionButton();

        setLayout();
        addComponents();
    }

    private void setContainerPanel(int frameWidth) {
        containerPanel.setSize(new Dimension(frameWidth, 200));
        containerPanel.setBackground(Color.white);
        containerPanel.setVisible(false);
    }

    private void setMediumIconImgLabel() {
        imageLabel = new JLabel();
        imageLabel.setSize(new Dimension(32, 32));
    }

    private void setupTitleLabel() {
        titleLabel = new JLabel();
        titleLabel.setSize(new Dimension(10, 10));
        titleLabel.setText("Notification");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        titleLabel.setForeground(Color.black);
    }

    private void setupDescriptionLabel() {
        descriptionLabel = new JLabel();
        descriptionLabel.setSize(new Dimension(100, 100));
        descriptionLabel.setText("This is a real-time notification");
        descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.black);
    }

    private void setupActionButton() {
        actionButton = new JButton();
        actionButton.setSize(new Dimension(10, 10));
        actionButton.setText("View");
        actionButton.setForeground(Color.gray);
        actionButton.setBorderPainted(false);
    }


    private void setLayout() {
        containerPanel.setLayout(new MigLayout("", // Layout Constraints
                "[][][][]460[]", // Column constraints with default align
                "")); // Row constraints
    }

    private void addComponents() {
        containerPanel.add(imageLabel, "cell 0 0");
        containerPanel.add(titleLabel, "cell 1 0");
        containerPanel.add(descriptionLabel, "cell 1 1");
        containerPanel.add(actionButton, "cell 1 2");
    }

    public JPanel getContainer() {
        return containerPanel;
    }

    public JButton getPublicationsTabButton() {
        return actionButton;
    }

    public void updateNotification(final String notificationTitle, final String notificationDescription, final BufferedImage image) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                updateSemaphore.acquire();
                //wait 2 seconds to avoid rapid succession of flashing notifications
                try {
                    sleep(2000);
                } catch (InterruptedException e) {}
                return true;
            }

            // Can safely update the GUI from this method.
            protected void done() {
                update(notificationTitle, notificationDescription, image);
            }
        };
        worker.execute();

    }

    private void update(String notificationTitle, final String notificationDescription, final BufferedImage image) {
        containerPanel.setVisible(true);
        titleLabel.setText(notificationTitle);
        descriptionLabel.setText(notificationDescription);
        if(image != null) {
            imageLabel.setIcon(new ImageIcon(ImageUtils.scaleImage(image, 32, 32)));
            imageLabel.setVisible(true);
            hide();
        }
    }

    private void hide() {
        TimeUtils.setTimeout(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                containerPanel.setVisible(false);
                imageLabel.setVisible(false);
                updateSemaphore.release();
            }
        });
    }
}
