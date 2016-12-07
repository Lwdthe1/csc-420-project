package views;

import models.Publication;
import models.User;
import viewControllers.AppView;
import viewControllers.PublicationPageViewController;
import views.subviews.PublicationChatView;

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

    private JLabel nameLabel;
    private JLabel aboutLabel;
    private JLabel descriptionLabel;
    private JLabel logoLabel;
    private JLabel statsLabel;
    private JLabel requestedStatsLabel;
    private JLabel viewedStatsLabel;
    private JLabel relationLabel;
    private JLabel relationDescriptionLabel;
    private JLabel contributorLabel;
    private JPanel contentPane;

    private static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private static Insets LEFT_PAD_20 = new Insets(0,20, 0, 0);
    private static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    private static Insets TOP_5_LEFT_PAD_20 = new Insets(5,20, 0, 0);
    private static Insets TOP_10_LEFT_PAD_20 = new Insets(10,20, 0, 0);
    private static Insets TOP_20_LEFT_PAD_20 = new Insets(20, 20, 0, 0);
    private static Insets TOP_30_LEFT_PAD_20 = new Insets(30, 20, 0, 0);
    private static final Insets TOP_PAD_5 = new Insets(5, 0, 0, 0);

    private int width;
    private int height;

    private PublicationChatView publicationChatView;

    private PublicationPageViewController publicationPageViewController;


    public PublicationPageView(PublicationPageViewController publicationPageViewController, int width, int height) {
        this.width = width;
        this.height = height;
        this.publicationPageViewController = publicationPageViewController;
        this.publicationChatView = new PublicationChatView(width, height);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel(new BorderLayout());
        System.out.println(width);
        System.out.println(height);
        this.contentPane.setSize(new Dimension(width, height));
        GridBagLayout gridbagLayout = new GridBagLayout();
        this.contentPane.setLayout(gridbagLayout);
        this.contentPane.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = publicationPageViewController.getPublication();
        addComponentsToPane(constraints, pub);
    }

    private void addComponentsToPane(GridBagConstraints constraints, Publication pub) {
        //addNameLabel(constraints, pub);
//        addAboutLabel(constraints);
//        addDescriptionLabel(constraints, pub);
//        addLogoLabel(constraints, pub);
//        addStatsLabel(constraints);
//        addRequestedStatsLabel(constraints, pub);
//        addViewedStatsLabel(constraints, pub);
//        addRelationLabel(constraints);
//        addRelationDescriptionLabel(constraints);
//        addContributerLabel(constraints, pub);
        addChatTable(constraints);
    }

    private void addNameLabel(GridBagConstraints constraints, Publication pub) {
        String nameHTML = format("<html><body><p style='%s'>%s</p></body></html>", FEED_PUBLICATION_NAME_STYLE, pub.getName());
        nameLabel = new JLabel(nameHTML);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.contentPane.add(nameLabel, constraints);
    }

    private void addAboutLabel(GridBagConstraints constraints) {
        aboutLabel = new JLabel("About");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = TOP_10_LEFT_PAD_20;
        this.contentPane.add(aboutLabel, constraints);
    }

    private void addDescriptionLabel(GridBagConstraints constraints, Publication pub) {
        descriptionLabel = new JLabel(pub.getDescription());
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.insets = TOP_5_LEFT_PAD_20;
        this.contentPane.add(descriptionLabel, constraints);
    }

    private void addLogoLabel(GridBagConstraints constraints, Publication pub) {
        logoLabel = new JLabel();
        Dimension dimensions = new Dimension(30, 30);
        logoLabel.setSize(dimensions);
        logoLabel.setIcon(new ImageIcon(pub.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = LEFT_PAD_15;
        this.contentPane.add(logoLabel, constraints);
    }

    private void addStatsLabel(GridBagConstraints constraints) {
        statsLabel = new JLabel("Stats");
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.insets = TOP_20_LEFT_PAD_20;
        this.contentPane.add(statsLabel, constraints);
    }

    private void addRequestedStatsLabel(GridBagConstraints constraints, Publication pub) {
        requestedStatsLabel = new JLabel("Requested " + pub.getPubIdTotalContributionRequests() + " times.");
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.insets = TOP_5_LEFT_PAD_20;
        this.contentPane.add(requestedStatsLabel, constraints);
    }

    private void addViewedStatsLabel(GridBagConstraints constraints, Publication pub) {
        viewedStatsLabel = new JLabel("Viewed " + pub.getPubIdTotalVisits() + " times. " + pub.getPubIdTotalVisitsByCurrentUser() + " by you.");
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.insets = TOP_5_LEFT_PAD_20;
        this.contentPane.add(viewedStatsLabel, constraints);
    }

    private void addRelationLabel(GridBagConstraints constraints) {
        relationLabel = new JLabel("Relation");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.insets = TOP_30_LEFT_PAD_20;
        this.contentPane.add(relationLabel, constraints);
    }

    private void addRelationDescriptionLabel(GridBagConstraints constraints) {
        relationDescriptionLabel = new JLabel("Your relationship with this publication.");
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.insets = TOP_30_LEFT_PAD_20;
        this.contentPane.add(relationDescriptionLabel, constraints);
    }

    private void addContributerLabel(GridBagConstraints constraints, Publication pub) {
        contributorLabel = new JLabel(pub.getContributorRole());
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.insets = TOP_5_LEFT_PAD_20;
        this.contentPane.add(contributorLabel, constraints);
    }

    private void addChatTable(GridBagConstraints constraints) {
        constraints.insets = new Insets(0, 0, 0, 0);
        this.contentPane.add(publicationChatView.getContentPane(), constraints);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JTable getTable() { return publicationChatView.getTable(); }

}
