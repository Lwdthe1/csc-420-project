package views.subviews;

import viewControllers.AppView;
import viewControllers.HomeFeedViewController;
import viewControllers.PublicationPageViewController;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by keithmartin on 12/6/16.
 */
public class PublicationChatView implements AppView {
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;

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
        this.contentPane = new JPanel(new BorderLayout());
        this.contentPane.setSize(new Dimension(width, height));

        addComponentsToPane();
    }

    public void addComponentsToPane() {
        createAndAddScrollableTable();
    }

    private void createAndAddScrollableTable() {
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);

        panel.setSize(new Dimension(width, height));
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(new Dimension(panel.getWidth(), panel.getHeight()));
        panel.add(scrollPane);
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
