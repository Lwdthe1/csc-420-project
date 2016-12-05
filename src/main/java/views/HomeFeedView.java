package views;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView {
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;
    private NavBarView navBarView;
    private RealTimeNotificationView realTimeNotificationView;

    public HomeFeedView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public JTable getTable() {
        return table;
    }

    public NavBarView getNavBarView() {
        return navBarView;
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel(new BorderLayout());
        this.contentPane.setSize(new Dimension(this.contentPane.getWidth(), this.contentPane.getHeight()));

        navBarView = new NavBarView(contentPane.getWidth());
        realTimeNotificationView = new RealTimeNotificationView(contentPane.getWidth());

        addComponentsToPane();
    }

    public void addComponentsToPane() {
        createAndAddScrollableTable();
        contentPane.add(navBarView.getContainer(), BorderLayout.NORTH);
        contentPane.add(realTimeNotificationView.getContainer(), BorderLayout.SOUTH);
    }

    private void createAndAddScrollableTable() {
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);

        panel.setSize(new Dimension(width, height));

        table = new JTable(){
            public TableCellRenderer getCellRenderer(int row, int column ) {
                return new PublicationCellRenderer();
            }
        };

        table.setRowHeight(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        panel.add(scrollPane);
    }

    public RealTimeNotificationView getRealTimeNotificationView() {
        return realTimeNotificationView;
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}