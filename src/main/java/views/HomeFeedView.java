package views;

import viewControllers.AppView;
import views.subviews.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView implements AppView {
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

        final TableButton buttons12 = new TableButton();
        buttons12.addHandler(new TableButton.TableButtonPressedHandler() {

            @Override
            public void onButtonPress(int row, int column) {
                // TODO Auto-generated method stub
                System.out.println("CLICKED!!!!");
            }
        });

        table = new JTable(){
            public TableCellRenderer getCellRenderer(int row, int column ) {
                switch(column) {
                    case 0:
                        return new PublicationImageCellRenderer();
                    case 1:
                        return new PublicationTextCellRenderer();
                    case 2:
                        buttons12.addHandler(new TableButton.TableButtonPressedHandler() {

                            @Override
                            public void onButtonPress(int row, int column) {
                                // TODO Auto-generated method stub
                                System.out.println("CLICKED!!!!");
                            }
                        });
                        return buttons12;
                    case 3:
                        return new EmptyCellRenderer();
                    default:
                        return null;
                }
            }

            public boolean isCellEditable(int row, int column) {
                switch(column) {
                    case 0:
                    case 2:
                        return true;
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

    public RealTimeNotificationView getRealTimeNotificationView() {
        return realTimeNotificationView;
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}