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
public class PublicationImageCellRenderer extends JPanel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        if (value instanceof  Publication) {
            Publication pub = (Publication) value;
            addContributeButton(pub);
        }
        return this;
    }

    private void addContributeButton(final Publication pub) {
        JButton imageButton = new JButton();
        Dimension dimensions = new Dimension(30, 30);
        imageButton.setSize(dimensions);
        imageButton.setIcon(new ImageIcon(pub.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        imageButton.setBorder(BorderFactory.createEmptyBorder());

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(PublicationsService.sharedInstance.requestToContributeById(pub.getId(), "user1"));
            }
        });

        this.enableInputMethods(true);
        this.add(imageButton, BorderLayout.EAST);
    }
}
