package views.subviews;

import models.Publication;
import utils.PublicationsService;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import static java.lang.String.format;

/**
 * Created by Andres on 10/23/16.
 */
public class PublicationContributeButtonCellRenderer extends JPanel implements TableCellRenderer, CellEditor {

    private static Insets LEFT_PAD_15 = new Insets(0,15, 0, 0);
    private static Insets RIGHT_PAD_15 = new Insets(0,0, 0, 15);
    private static Insets TOP_5_LEFT_PAD_15 = new Insets(5,15, 0, 0);
    public JButton contributeButton;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Publication pub = (Publication) value;
        addContributeButton(constraints, pub);

        pub.setHomeFeedTableCell(this);
        return this;
    }

    private void addContributeButton(GridBagConstraints constraints, final Publication pub) {
        contributeButton = new JButton();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.insets = RIGHT_PAD_15;
        
        if (pub.currentUserIsContributor()) {
            contributeButton.setText("Contributor");
            contributeButton.setEnabled(false);
            contributeButton.setBorder(BorderFactory.createEmptyBorder());
        } else if (pub.currentUserRequestWasRejected()) {
            contributeButton.setText("Rejected");
            contributeButton.setEnabled(false);
            contributeButton.setBorder(BorderFactory.createEmptyBorder());
        }  else if (pub.currentUserRequested()) {
            contributeButton.setText("Retract");
            contributeButton.setEnabled(true);
        } else {
            contributeButton.setText("Contribute");
            contributeButton.setEnabled(true);
        }

        this.add(contributeButton, constraints);
    }

    @Override
    public Object getCellEditorValue() {
        System.out.println("getCellEditorValue");
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        System.out.println("isCellEditable");
        return false;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        System.out.println("shouldSelectCell");
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        System.out.println("stopCellEditing");
        return false;
    }

    @Override
    public void cancelCellEditing() {
        System.out.println("cancelCellEditing");
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        System.out.println("isCellEditable");
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        System.out.println("isCellEditable");
    }
}
