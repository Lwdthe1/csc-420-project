package views.subviews;

import models.ChatMessage;
import models.Publication;
import models.User;
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

        addImageButton(value);
        return this;
    }

    private void addImageButton(final Object object) {
        if (object instanceof Publication) {
            final Publication pub = (Publication) object;
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
        } else if (object instanceof ChatMessage) {
            final ChatMessage chatMessage = (ChatMessage) object;
            JLabel imageLabel = new JLabel();
            Dimension dimensions = new Dimension(30, 30);
            imageLabel.setSize(dimensions);
            imageLabel.setIcon(new ImageIcon(chatMessage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            imageLabel.setBorder(BorderFactory.createEmptyBorder());

            this.add(imageLabel, BorderLayout.EAST);
        }
    }
}
