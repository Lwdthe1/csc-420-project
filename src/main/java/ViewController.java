import models.Publication;
import org.apache.http.HttpException;
import utils.WebService.RestCaller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by lwdthe1 on 9/5/16.
 */
public class ViewController {
    private final JTable table;
    private ArrayList<Publication> publications = new ArrayList<>();

    private int lastSelectedIndex = -1;

    public ViewController(JTable table) {
        this.table = table;
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


    private void populateComboBox() {
        Publication[] pubs = new Publication[publications.size()];

        for(int i=0; i<publications.size(); i++){
            pubs[i] = publications.get(i);
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Publications",pubs);
        table.setModel(model);

    }

}
