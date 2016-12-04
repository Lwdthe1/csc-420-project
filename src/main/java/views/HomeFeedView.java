package views;

import views.NavBarView;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HomeFeedView {
    private JFrame frame;
    private JTable table;
    private NavBarView navBarView;

    public JTable getTable() {
        return table;
    }

    public JFrame getFrame() {
        return frame;
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
        //Create and set up the window.
        frame = new JFrame("SuperSwingMeditor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.

        frame.setSize(new Dimension(900,900));
        frame.setBackground(Color.white);

        //frame.setMinimumSize(new Dimension(500,500));
        //frame.setUndecorated(true);
        frame.setVisible(true);
        navBarView = new NavBarView(frame.getWidth());

        addComponentsToPane(frame.getContentPane());
    }

    public void addComponentsToPane(Container contentPane) {
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.WEST);

        panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));

        table = new JTable(){
            public TableCellRenderer getCellRenderer(int row, int column ) {
                return new PublicationCellRenderer();
            }
        };

        table.setRowHeight(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        panel.add(scrollPane);
        contentPane.add(navBarView.getNavBarPanel(), BorderLayout.NORTH);
    }

}