package views.subviews;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Created by keithmartin on 12/6/16.
 */
public class PublicationChatView  {
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton sendMessageButton;
    private JTextArea chatTextArea;


    public PublicationChatView(int width, int height) {
        this.width = width;
        this.height = height;
        createAndShow();
    }

    public JTable getTable() {
        return table;
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel();
        this.contentPane.setLayout(new MigLayout());
        addComponentsToPane();
    }

    public void addComponentsToPane() {
        createAndAddScrollableTable();
        addChatTextArea();
        addSendMessageButton();
    }

    private void createAndAddScrollableTable() {

        table = new JTable(){

            public TableCellRenderer getCellRenderer(int row, int column ) {
                switch(column) {
                    case 0:
                        return new PublicationImageCellRenderer();
                    case 1:
                        return new PublicationTextCellRenderer();
                    default:
                        return null;
                }
            }
        };

        table.setRowHeight(100);

        scrollPane = new JScrollPane(table);
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
//            }
//        });
        contentPane.add(scrollPane, "cell 0 0, growx, growy");
    }

    private void addCurrentUserImage() {
        //TODO: Will add once I get the current user class merged.
        //JLabel currentUserImage = new JLabel()
    }

    private void addChatTextArea() {
        chatTextArea = new JTextArea();
        chatTextArea.setLineWrap(true);
        JScrollPane chatTextScrollingArea = new JScrollPane(chatTextArea);
        contentPane.add(chatTextScrollingArea, "cell 0 1, growx, growy");
    }

    private void addSendMessageButton() {
        sendMessageButton = new JButton("Send");
        contentPane.add(sendMessageButton, "cell 1 1");
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JScrollPane getScrollPane() { return scrollPane; }

    public void refreshTable() {
        table.repaint();
    }

    public JButton getSendMessageButton() {
        return sendMessageButton;
    }

    public JTextArea getChatTextArea() {
        return chatTextArea;
    }
}
