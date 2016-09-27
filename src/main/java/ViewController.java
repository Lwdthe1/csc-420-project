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
    private JLabel imgLabel;
    private JComboBox comboBox = new JComboBox();
    private ArrayList<Publication> publications = new ArrayList<>();

    private int lastSelectedIndex = -1;

    public ViewController(JComboBox comboBox, JLabel imgLabel) {
        this.comboBox = comboBox;
        this.imgLabel = imgLabel;
        showSuperMeditorLogo();
        populatePublications();
        populateComboBox();
    }

    private void populatePublications() {
        try {
            publications = (ArrayList<Publication>) RestCaller.getPublications();
            System.out.println(publications.size() + " publications loaded from server.");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSuperMeditorLogo() {
        displayImage(Publication.getSuperMeditorLogoImage());
    }

    private void populateComboBox() {
        for (Publication euCountry: publications) {
            comboBox.addItem(euCountry.getName());
        }

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBox.getSelectedIndex();

                lastSelectedIndex = index;
                Publication country = publications.get(index);
                displayImage(country.getImage());
            }
        });

        imgLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comboBox.showPopup();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void displayImage(BufferedImage img) {
        imgLabel.setIcon(new ImageIcon(img.getScaledInstance(
                imgLabel.getWidth(),
                imgLabel.getHeight(),
                Image.SCALE_SMOOTH)
        ));
    }

}
