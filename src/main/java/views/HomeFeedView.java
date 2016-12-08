package views;

import models.Publication;
import viewControllers.AppView;
import viewControllers.HomeFeedViewController;
import views.subviews.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView implements AppView {
    private final HomeFeedViewController homeFeedViewController;
    private int width;
    private int height;
    private JPanel contentPane;
    private JTable table;
    private NavBarView navBarView;
    private RealTimeNotificationView realTimeNotificationView;
    private LoggedOutActionListener loggedOuatAL;

    public HomeFeedView(HomeFeedViewController homeFeedViewController, int width, int height) {
        this.homeFeedViewController = homeFeedViewController;
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

        loggedOuatAL = new LoggedOutActionListener(this);
        navBarView = new NavBarView(contentPane.getWidth(), loggedOuatAL);
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
                        registerPublicationImageButtonClick(row, column);
                        return false;
                    case 2:
                        registerPublicationContributeCellClicked(row, column);
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

    private void registerPublicationContributeCellClicked(int row, int column) {
        Object value = table.getValueAt(row, column);
        if (value instanceof Publication) {
            Publication publication = (Publication) value;
            JButton contributeButton = publication.getHomeFeedTableCell().contributeButton;
            if (contributeButton.isEnabled()) {
                homeFeedViewController.publicationContributeCellClicked(publication, row, column);
            }
        }
    }

    private void registerPublicationImageButtonClick(int row, int column) {
        Object value = table.getValueAt(row, column);
        if (value instanceof Publication) {
            homeFeedViewController.publicationImageButtonClicked((Publication) value);
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

    public void refreshTable(int row) {
        table.repaint();// faster than ((AbstractTableModel) table.getModel()).fireTableDataChanged()
    }
}