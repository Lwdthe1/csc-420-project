package views.subviews;

import models.ChatMessage;
import models.Publication;
import models.User;
import models.RequestToContribute;
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

        Image buttonImage = null;
        if (value instanceof Publication) {
            buttonImage = ((Publication) value).getImage();
        } else if (value instanceof RequestToContribute) {
            buttonImage = ((RequestToContribute) value).getPublication().getImage();
        } else if (value instanceof ChatMessage) {
            buttonImage = ((ChatMessage) value).getImage();
        }

        if (buttonImage != null) {
            addImageButton(buttonImage);
        }
        return this;
    }

    private void addImageButton(Image buttonImage) {
            JButton imageButton = new JButton();
            Dimension dimensions = new Dimension(30, 30);
            imageButton.setSize(dimensions);
            imageButton.setIcon(new ImageIcon(buttonImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            imageButton.setBorder(BorderFactory.createEmptyBorder());

            this.enableInputMethods(true);
            this.add(imageButton, BorderLayout.EAST);
    }
}
