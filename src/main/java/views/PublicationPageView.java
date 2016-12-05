package views;

import models.Publication;
import viewControllers.AppView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by keithmartin on 12/5/16.
 */
public class PublicationPageView implements AppView {
    private JLabel pubNameLabel;
    private JLabel aboutLabel;
    private JLabel aboutDetailLabel;
    private JLabel pubLogoLabel;
    private JLabel statsLabel;
    private JLabel requestedStatsLabel;
    private JLabel viewedStatsLabel;
    private JLabel relationLabel;
    private JLabel relationDescriptionLabel;
    private JLabel relationDetailLabel;
    private JPanel contentPane;



    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel(new BorderLayout());
        this.contentPane.setSize(new Dimension(this.contentPane.getWidth(), this.contentPane.getHeight()));

        addComponentsToPane();
    }

    public void addComponentsToPane() {

    }

    public void setComponents(Publication publication) {
        
    }



}
