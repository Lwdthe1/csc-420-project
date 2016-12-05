package viewControllers;

import models.ChatMessage;
import models.Publication;
import org.apache.http.HttpException;
import org.json.JSONObject;
import utils.WebService.RestCaller;
import utils.WebService.socketio.SocketEvent;
import utils.WebService.socketio.SocketListener;
import utils.WebService.socketio.SocketManager;
import views.HomeFeedView;
import views.NavBarView;
import views.PublicationCell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static java.lang.String.format;

/**
 * Created by lwdthe1 on 9/5/16.
 */
public class HomeFeedViewController implements SocketListener {
    private final HomeFeedView homeFeedView;
    private ArrayList<Publication> publications = new ArrayList<>();


    private SocketManager socketManger;
    private Semaphore setupViewWhileLoadingSemaphore = new Semaphore(1);

    public HomeFeedViewController() {
        //acquire the lock here before starting load
        try {
            setupViewWhileLoadingSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadFeed();
        this.homeFeedView = new HomeFeedView();

        setupView();
        setupViewWhileLoadingSemaphore.release();
        startSocketIO();
    }

    private void setupView() {
        this.homeFeedView.createAndShow();
        setButtonHoverListeners();
    }

    private void loadFeed() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                fetchPublications();
                return true;
            }

            // Can safely update the GUI from this method.
            protected void done() {
                showPublications();
            }
        };
        worker.execute();
    }

    private void fetchPublications() {
        try {
            publications = (ArrayList<Publication>) RestCaller.getPublications();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showPublications() {
        try {
            //acquire the lock to assure the view is ready
            setupViewWhileLoadingSemaphore.acquire();

            Publication[] pubs = new Publication[publications.size()];
            for(int i=0; i<publications.size(); i++){
                pubs[i] = publications.get(i);
            }

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn(format("%d Publications Looking for Writers", pubs.length), pubs);
            homeFeedView.getTable().setModel(model);
            homeFeedView.getTable().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    Component component = homeFeedView.getTable().findComponentAt(evt.getPoint());
                    JLabel cellLabel = (JLabel) component;
                    if (cellLabel.getClientProperty("labelType") == "pubNameLabel") {
                        System.out.println("Swing is absolute bootycheeks");
                        homeFeedView.getFrame().getContentPane().removeAll();
                    }
                }
            });
        } catch (InterruptedException e) {
            System.out.printf("\nCouldn't show publications because: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setButtonHoverListeners() {
        final NavBarView navBarView = homeFeedView.getNavBarView();
        navBarView.getPublicationsTabButton().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navBarView.getPublicationsTabButton().setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                navBarView.getPublicationsTabButton().setForeground(Color.GRAY);
            }
        });
    }

    private void startSocketIO() {
        this.socketManger = new SocketManager();
        socketManger.listen(SocketEvent.CONNECTED, this);
        socketManger.setupAndConnect();
    }

    @Override
    public void onEvent(SocketEvent event, JSONObject payload) {
        switch(event) {
            case CONNECTED:
                registerForEvents();
                break;
            case DISCONNECTED:
                break;
            case NUM_CLIENTS:
                System.out.printf("\nNumber of active clients: %d\n", payload.get("value"));
                break;
            case CHAT_MESSAGE:
                ChatMessage chatMessage = new ChatMessage(payload);
                System.out.printf("\nReceived chat message: %s\n", chatMessage.getText());
                break;
        }
    }

    @Override
    public void onEvent(String event, JSONObject obj) {

    }

    @Override
    public void registerForEvents() {
        socketManger.listen(SocketEvent.NUM_CLIENTS, this);
        socketManger.listen(SocketEvent.CHAT_MESSAGE, this);
        sendChatMessage();
    }

    private void sendChatMessage() {
        socketManger.emit(SocketEvent.CHAT_MESSAGE, ChatMessage.createJSONPayload("pub1", "user1", "Hello there!"));
    }
}
