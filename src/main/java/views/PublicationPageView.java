package views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by keithmartin on 12/4/16.
 */
public class PublicationPageView extends JPanel {

    private JLabel label;

    public PublicationPageView() {
        label = new JLabel("FUCK SWING");
        this.add(label, BorderLayout.CENTER);
    }

}
