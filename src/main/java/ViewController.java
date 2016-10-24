import models.Publication;
import org.apache.http.HttpException;
import utils.WebService.RestCaller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by lwdthe1 on 9/5/16.
 */
public class ViewController {
    private NavBarView navBarView;

    public ViewController(NavBarView navBarView) {
        this.navBarView = navBarView;
        setButtonHoverListeners();
    }

    private void setButtonHoverListeners() {
        navBarView.getPublicationsTabButton().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navBarView.getPublicationsTabButton().setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                navBarView.getPublicationsTabButton().setForeground(Color.GRAY);
            }
        });

        navBarView.getAdvertiseTabButton().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navBarView.getAdvertiseTabButton().setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                navBarView.getAdvertiseTabButton().setForeground(Color.GRAY);
            }
        });
    }


}
