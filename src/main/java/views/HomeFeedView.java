package views;

import viewControllers.AppView;
import viewControllers.HomeFeedViewController;
import views.subviews.*;

import viewControllers.AppView;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView implements AppView {
    private final HomeFeedViewController homeFeedViewController;
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;
    private RealTimeNotificationView realTimeNotificationView;

    public HomeFeedView(HomeFeedViewController homeFeedViewController, int width, int height) {
        this.homeFeedViewController = homeFeedViewController;
        this.width = width;
        this.height = height;
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
        this.contentPane.setSize(new Dimension(this.contentPane.getWidth(), this.contentPane.getHeight()));

        realTimeNotificationView = new RealTimeNotificationView(contentPane.getWidth());
        addComponentsToPane();
    }

    public void addComponentsToPane() {
        createAndAddScrollableTable();
        contentPane.add(realTimeNotificationView.getContainer(), BorderLayout.SOUTH);
    }

    private void createAndAddScrollableTable() {
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);

        panel.setSize(new Dimension(width, height));
        table = new JTable(){

            public TableCellRenderer getCellRenderer(int row, int column ) {
                switch(column) {
                    case 0:
                        return new PublicationImageCellRenderer();
                    case 1:
                        return new PublicationTextCellRenderer();
                    case 2:
                        return new PublicationContributeButtonCellRenderer();
                    case 3:
                        return new EmptyCellRenderer();
                    default:
                        return null;
                }
            }

            public boolean isCellEditable(int row, int column) {
                switch(column) {
                    case 0:
                        registerPublicationImageButtonClick(row);
                        return false;
                    case 2:
                        registerPublicationContributeCellClicked(row);
                        return false;
                    default:
                        return false;
                }
            }
        };


        table.setRowHeight(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        panel.add(scrollPane);
    }

    private void registerPublicationContributeCellClicked(int row) {
        homeFeedViewController.publicationContributeCellClicked(row);
    }

    private void registerPublicationImageButtonClick(int row) {
        homeFeedViewController.publicationImageButtonClicked(row);
    }

    public RealTimeNotificationView getRealTimeNotificationView() {
        return realTimeNotificationView;
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}