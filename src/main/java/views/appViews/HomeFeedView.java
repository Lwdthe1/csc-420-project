package views.appViews;

import models.Publication;
import viewControllers.interfaces.AppView;
import viewControllers.HomeFeedViewController;
import viewControllers.interfaces.AppViewController;
import viewControllers.interfaces.ViewController;
import views.subviews.*;

import viewControllers.interfaces.AppView;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView implements AppView {
    private final HomeFeedViewController appViewController;
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;
    private RealTimeNotificationView realTimeNotificationView;

    public HomeFeedView(HomeFeedViewController appViewController, int width, int height) {
        this.appViewController = appViewController;
        this.width = width;
        this.height = height;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShow() {
        this.contentPane = new JPanel(new BorderLayout());
        this.contentPane.setSize(new Dimension(this.contentPane.getWidth(), this.getWidth()));
        realTimeNotificationView = new RealTimeNotificationView(this.getWidth());

        addComponentsToPane();
    }

    @Override
    public AppViewController getViewController() {
        return appViewController;
    }

    public void addComponentsToPane() {
        createAndAddScrollableTable();
        contentPane.add(appViewController.getNavigationController().getView().getContentPane(), BorderLayout.NORTH);
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
                        handlePublicationImageButtonClick(row, column);
                        return false;
                    case 2:
                        handleActionButtonCellClicked(row, column);
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

    private void handleActionButtonCellClicked(int row, int column) {
        Object value = table.getValueAt(row, column);
        if (value instanceof Publication) {
            Publication publication = (Publication) value;
            JButton contributeButton = publication.getHomeFeedTableCell().contributeButton;
            if (contributeButton.isEnabled()) {
                appViewController.publicationContributeCellClicked(publication, row, column);
            }
        }
    }

    private void handlePublicationImageButtonClick(int row, int column) {
        Object value = table.getValueAt(row, column);
        if (value instanceof Publication) {
            appViewController.publicationImageButtonClicked((Publication) value);
        }

    }

    public RealTimeNotificationView getRealTimeNotificationView() {
        return realTimeNotificationView;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void onContributeRequestSuccess(int row, int column) {
        ((AbstractTableModel) table.getModel()).fireTableCellUpdated(row, column);
    }

    public void refreshTable() {
        table.repaint();// faster than ((AbstractTableModel) table.getModel()).fireTableDataChanged()
    }
}