package views.subviews;

import models.Publication;
import models.RequestToContribute;
import utils.TextUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static java.lang.String.format;

/**
 * Created by Andres on 10/23/16.
 */
public class PublicationTextCellRenderer extends JPanel implements TableCellRenderer {
    private static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private static Insets LEFT_PAD_20 = new Insets(0,20, 0, 0);
    private static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    private static Insets TOP_5_LEFT_PAD_20 = new Insets(5,20, 0, 0);
    private static final Insets TOP_PAD_5 = new Insets(5, 0, 0, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        GridBagLayout gridbagLayout = new GridBagLayout();
        this.setLayout(gridbagLayout);
        this.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = null;
        if (value instanceof Publication) {
            pub = (Publication) value;
        } else if (value instanceof RequestToContribute) {
            pub = ((RequestToContribute) value).getPublication();
        }

        if (pub != null) {
            addNameLabel(constraints, pub);
            addDescriptionLabel(constraints, pub);
            addMetaInfoLabel(constraints, pub);
        }

        return this;
    }

    private void addNameLabel(GridBagConstraints constraints, Publication pub) {
        String nameHTML = format("<html><body><p style='%s'>%s</p></body></html>", TextUtils.FEED_PUBLICATION_NAME_STYLE, pub.getName());
        JLabel pubNameLabel = new JLabel(nameHTML);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_20;
        this.add(pubNameLabel, constraints);
    }

    private void addDescriptionLabel(GridBagConstraints constraints, Publication pub) {
        String description = pub.getDescription();
        String description1stLine = description, description2ndLine = "";
        if (description.split(" ").length > 9) {
            description1stLine = description.substring(0, description.length()/2);
            description2ndLine = description.substring(description.length()/2, description.length() - 1);
        }

        String descriptionHTML = format("<html><body><p style='%s'>%s<br>%s</p></body></html>", TextUtils.DESCRIPTIVE_TEXT_STYLE, description1stLine, description2ndLine);

        JLabel descriptionLabel = new JLabel(descriptionHTML);
        constraints.weightx = 0.7;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = LEFT_PAD_20;
        this.add(descriptionLabel, constraints);
    }

    private void addMetaInfoLabel(GridBagConstraints constraints, Publication pub) {
        String metaInfo = pub.getContributorUsername() + " · " + "Requested " + pub.getPubIdTotalContributionRequests() + " times" + " · " + "viewed " + pub.getPubIdTotalVisits() + " times";
        String metaInfoHTML = format("<html><body><p style='%s'>%s</p></body></html>", TextUtils.META_TEXT_STYLE, metaInfo);

        JLabel metaInfoLabel = new JLabel(metaInfoHTML);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = TOP_5_LEFT_PAD_20;
        this.add(metaInfoLabel, constraints);
    }

}
