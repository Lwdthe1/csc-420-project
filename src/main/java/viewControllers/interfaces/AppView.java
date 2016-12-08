package viewControllers.interfaces;

import javax.swing.*;

/**
 * Created by lwdthe1 on 12/5/16.
 */
public interface AppView extends View {
    AppViewController getViewController();
    JPanel getContentPane();
}
