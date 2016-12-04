import models.Publication;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Andres on 10/23/16.
 */
public class PublicationCellRenderer extends JPanel implements TableCellRenderer {
    static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = (Publication) value;

        JLabel imageLabel = new JLabel();
        Dimension dimensions = new Dimension(30, 30);
        imageLabel.setSize(dimensions);
        imageLabel.setIcon(new ImageIcon(pub.getImage().getScaledInstance(
                30,
                30,
                Image.SCALE_SMOOTH)));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(imageLabel, constraints);

        JLabel pubNameLabel = new JLabel(pub.getName());
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(pubNameLabel, constraints);

        String description = "<html><body width='500px'>" +
                pub.getDescription() +
                "</body></html>";

        JLabel descriptionLabel = new JLabel(description);
        constraints.weightx = 0.7;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = LEFT_PAD_15;
        this.add(descriptionLabel, constraints);

        String metaInfo = pub.getContributorUsername() + " - " + "Requested " + pub.getPubIdTotalContributionRequests() + " times" + " - " + "viewed " + pub.getPubIdTotalVisits() + " times";

        JLabel metaInfoLabel = new JLabel(metaInfo);
        metaInfoLabel.setFont(metaInfoLabel.getFont().deriveFont(7.0f));
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipady = 5;
        constraints.insets = LEFT_PAD_15;
        this.add(metaInfoLabel, constraints);

        JButton contributeButton = new JButton("Contribute");
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(contributeButton, constraints);

        return this;
    }
}
