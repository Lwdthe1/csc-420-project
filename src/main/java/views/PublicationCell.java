package views;

import models.Publication;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

/**
 * Created by Andres on 10/23/16.
 */
public class PublicationCell extends JPanel implements TableCellRenderer {
    static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private JLabel imageLabel;
    private JLabel pubNameLabel;
    private JLabel descriptionLabel;
    private JLabel metaInfoLabel;
    private JButton contributeButton;
    private Publication pub;

    public PublicationCell() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        imageLabel = new JLabel();
        Dimension dimensions = new Dimension(30, 30);
        imageLabel.setSize(dimensions);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(imageLabel, constraints);

        pubNameLabel = new JLabel();
        pubNameLabel.putClientProperty("labelType", "pubNameLabel");

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = LEFT_PAD_15;
        this.add(pubNameLabel, constraints);

        descriptionLabel = new JLabel();
        constraints.weightx = 0.7;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = LEFT_PAD_15;
        this.add(descriptionLabel, constraints);


        metaInfoLabel = new JLabel();
        metaInfoLabel.setFont(metaInfoLabel.getFont().deriveFont(7.0f));
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipady = 5;
        constraints.insets = LEFT_PAD_15;
        this.add(metaInfoLabel, constraints);

        contributeButton = new JButton("Contribute");
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(contributeButton, constraints);
    }

    private void updateCellPanelUI(boolean isSelected, JTable table, Publication pub) {
        imageLabel.setIcon(new ImageIcon(pub.getImage().getScaledInstance(
                30,
                30,
                Image.SCALE_SMOOTH)));

        pubNameLabel.setText(pub.getName());
        pubNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        String description = "<html><body width='500px'>" +
                pub.getDescription() +
                "</body></html>";

        descriptionLabel.setText(description);

        String metaInfo = pub.getContributorUsername() + " - " + "Requested " + pub.getPubIdTotalContributionRequests() + " times" + " - " + "viewed " + pub.getPubIdTotalVisits() + " times";
        metaInfoLabel.setText(metaInfo);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        pub = (Publication) value;
        updateCellPanelUI(isSelected, table, pub);
        return this;
    }
}
