package views.subviews;

import models.Publication;
import utils.PublicationsService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.String.format;

/**
 * Created by Andres on 10/23/16.
 */
public class PublicationCellRenderer extends JPanel implements TableCellRenderer {
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
    private static final String DESCRIPTIVE_TEXT_STYLE = "color: #4c4c4c\n" +
            "    font-size: 9px;\n" +
            "    font-family: -apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,Oxygen,Ubuntu,Cantarell,\"Open Sans\",\"Helvetica Neue\",sans-serif;\n" +
            "    letter-spacing: 0;\n" +
            "    font-weight: 300;\n" +
            "    font-style: normal;\n" +
            "    text-rendering: optimizeLegibility;\n" +
            "    -webkit-font-smoothing: antialiased;\n" +
            "    -moz-osx-font-smoothing: grayscale;\n" +
            "    -moz-font-feature-settings: \"liga\" on;\n" +
            "    text-decoration: none !important;\n";
    private static final String META_TEXT_STYLE = "color: #7f7f7f\n" +
            "    font-size: 8px;\n" +
            "    font-family: -apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,Oxygen,Ubuntu,Cantarell,\"Open Sans\",\"Helvetica Neue\",sans-serif;\n" +
            "    letter-spacing: 0;\n" +
            "    font-weight: 300;\n" +
            "    font-style: normal;\n" +
            "    text-rendering: optimizeLegibility;\n" +
            "    -webkit-font-smoothing: antialiased;\n" +
            "    -moz-osx-font-smoothing: grayscale;\n" +
            "    -moz-font-feature-settings: \"liga\" on;\n" +
            "    text-decoration: none !important;\n";

    static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    static Insets TOP_5_LEFT_PAD_15 = new Insets(5,15, 0, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = (Publication) value;

        addImageLabel(constraints, pub);
        addNameLabel(constraints, pub);
        addDescriptionLabel(constraints, pub);
        addMetaInfoLabel(constraints, pub);
        addContributeButton(constraints, pub);

        return this;
    }

    private void addContributeButton(GridBagConstraints constraints, final Publication pub) {
        JButton contributeButton = new JButton("Contribute");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.insets = RIGHT_PAD_15;
        contributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(PublicationsService.sharedInstance.requestToContributeById(pub.getId(), "user1"));
            }
        });
        this.add(contributeButton, constraints);
    }

    private void addNameLabel(GridBagConstraints constraints, Publication pub) {
        String nameHTML = format("<html><body><p style='%s'>%s</p></body></html>", FEED_PUBLICATION_NAME_STYLE, pub.getName());
        JLabel pubNameLabel = new JLabel(nameHTML);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(pubNameLabel, constraints);
    }

    private void addDescriptionLabel(GridBagConstraints constraints, Publication pub) {
        String descriptionHTML = format("<html><body><p style='%s'>%s</p></body></html>", DESCRIPTIVE_TEXT_STYLE, pub.getDescription());

        JLabel descriptionLabel = new JLabel(descriptionHTML);
        constraints.weightx = 0.7;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = LEFT_PAD_15;
        this.add(descriptionLabel, constraints);
    }

    private void addMetaInfoLabel(GridBagConstraints constraints, Publication pub) {
        String metaInfo = pub.getContributorUsername() + " · " + "Requested " + pub.getPubIdTotalContributionRequests() + " times" + " · " + "viewed " + pub.getPubIdTotalVisits() + " times";
        String metaInfoHTML = format("<html><body><p style='%s'>%s</p></body></html>", META_TEXT_STYLE, metaInfo);


        JLabel metaInfoLabel = new JLabel(metaInfoHTML);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipady = 5;
        constraints.insets = TOP_5_LEFT_PAD_15;
        this.add(metaInfoLabel, constraints);
    }

    private void addImageLabel(GridBagConstraints constraints, Publication pub) {
        JLabel roundImageButton = new JLabel();

        Dimension dimensions = new Dimension(30, 30);
        roundImageButton.setSize(dimensions);
        roundImageButton.setIcon(new ImageIcon(pub.getImage().getScaledInstance(
                30,
                30,
                Image.SCALE_SMOOTH)));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(roundImageButton, constraints);
    }
}
