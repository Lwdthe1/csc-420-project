package views.subviews;

import models.Publication;
import models.RequestToContribute;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

import static java.lang.String.format;

/**
 * Created by Andres on 10/23/16.
 */
public class RequestStatusButtonCellRenderer extends JPanel implements TableCellRenderer {

    private static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    private static Insets TOP_5_LEFT_PAD_15 = new Insets(5,15, 0, 0);
    public JButton contributeButton;
    public JLabel statusLabel;

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
            "    text-decoration: none !important;\n word-wrap: break-word; ";

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        RequestToContribute requestToContribute = (RequestToContribute) value;
        addContributeButton(constraints, requestToContribute);
        addStatusLabel(constraints, requestToContribute);

        requestToContribute.setFeedTableCell(this);
        return this;
    }

    private void addContributeButton(GridBagConstraints constraints, final RequestToContribute requestToContribute) {
        contributeButton = new JButton();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.insets = RIGHT_PAD_15;

        if (requestToContribute.isPending()) {
            if (requestToContribute.getPublication().currentUserRequested()) {
                contributeButton.setText("Retract");
                contributeButton.setEnabled(true);
            } else {
                contributeButton.setText("Contribute");
                contributeButton.setEnabled(true);
            }
            this.add(contributeButton, constraints);
        }
    }

    private void addStatusLabel(GridBagConstraints constraints, final RequestToContribute requestToContribute) {
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.insets = RIGHT_PAD_15;

        String text = "";
        if (requestToContribute.wasAccepted()) {
            text = "Accepted";
        } else if (requestToContribute.wasRejected()) {
            text = "Rejected";
        } else {
            text = "Pending";
        }
        String descriptionHTML = format("<html><body><p style='%s'>%s</p></body></html>", DESCRIPTIVE_TEXT_STYLE, text);
        statusLabel = new JLabel(descriptionHTML);
        this.add(statusLabel, constraints);
    }
}
