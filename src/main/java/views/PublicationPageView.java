package views;

import models.Publication;
import viewControllers.AppView;
import viewControllers.PublicationPageViewController;

import javax.swing.*;
import java.awt.*;

import static java.lang.String.format;

/**
 * Created by keithmartin on 12/5/16.
 */
public class PublicationPageView implements AppView {
    private static final String FEED_PUBLICATION_NAME_STYLE = "color: #1abc9c;\n" +
            "    font-size: 10px;\n" +
            "    font-family: -apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,Oxygen,Ubuntu,Cantarell,\"Open Sans\",\"Helvetica Neue\",sans-serif;\n" +
            "    letter-spacing: 0;\n" +
            "    font-weight: 500;\n" +
            "    font-style: normal;\n" +
            "    text-rendering: optimizeLegibility;\n" +
            "    -webkit-font-smoothing: antialiased;\n" +
            "    -moz-osx-font-smoothing: grayscale;\n" +
            "    -moz-font-feature-settings: \"liga\" on;\n" +
            "    text-decoration: none !important;\n" +
            "    cursor: pointer !important;";
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

    private static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private static Insets LEFT_PAD_20 = new Insets(0,20, 0, 0);
    private static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    private static Insets TOP_5_LEFT_PAD_20 = new Insets(5,20, 0, 0);
    private static final Insets TOP_PAD_5 = new Insets(5, 0, 0, 0);

    private int width;
    private int height;

    private PublicationPageViewController publicationPageViewController;


    public PublicationPageView(PublicationPageViewController publicationPageViewController, int width, int height) {
        this.width = width;
        this.height = height;
        this.publicationPageViewController = publicationPageViewController;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel(new BorderLayout());
        this.contentPane.setSize(new Dimension(this.contentPane.getWidth(), this.contentPane.getHeight()));
        GridBagLayout gridbagLayout = new GridBagLayout();
        this.contentPane.setLayout(gridbagLayout);
        this.contentPane.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = publicationPageViewController.getPublication();
        addComponentsToPane(constraints, pub);
    }

    public void addComponentsToPane(GridBagConstraints constraints, Publication pub) {
        addPubNameLabel(constraints, pub);
    }

    public void addPubNameLabel(GridBagConstraints constraints, Publication pub) {
        String nameHTML = format("<html><body><p style='%s'>%s</p></body></html>", FEED_PUBLICATION_NAME_STYLE, pub.getName());
        JLabel pubNameLabel = new JLabel(nameHTML);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_20;
        this.contentPane.add(pubNameLabel, constraints);
    }

    public void setComponents(Publication publication) {

    }

    public JPanel getContentPane() {
        return contentPane;
    }



}
