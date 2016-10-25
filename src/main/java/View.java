import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class View {
    private static JLabel imgLabel;
    private static JTable table;
    private static JFrame frame;
    private static NavBarView navBarView;

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
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
        new ViewController(table,navBarView);
    }

    public static void addComponentsToPane(Container contentPane) {
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