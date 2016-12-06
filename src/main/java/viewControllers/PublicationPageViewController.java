package viewControllers;

import models.ChatMessage;
import models.Publication;
import models.RequestDecisionNotification;
import org.json.JSONObject;
import utils.PublicationsService;
import utils.WebService.socketio.SocketEvent;
import utils.WebService.socketio.SocketListener;
import utils.WebService.socketio.SocketManager;
import views.HomeFeedView;
import views.subviews.NavBarView;
import views.PublicationPageView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * Created by keithmartin on 12/5/16.
 */
public class PublicationPageViewController implements SocketListener, AppViewController {
    private final PublicationPageView view;
    private final MainApplication application;

    private SocketManager socketManger;
    private Semaphore setupViewWhileLoadingSemaphore = new Semaphore(1);
    private PublicationsService publicationsService;

    public PublicationPageViewController(MainApplication application) {
        this.application = application;
        //acquire the lock here before starting load
        try {
            setupViewWhileLoadingSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publicationsService = PublicationsService.sharedInstance;
        loadFeed();
        this.view = new PublicationPageView(application.getMainFrame().getWidth(), application.getMainFrame().getHeight());
        setupView();
        setupViewWhileLoadingSemaphore.release();
        startSocketIO();
    }


    @Override
    public AppView getView() {
        return view;
    }

    public void setupView() {
        this.view.createAndShow();
        setButtonHoverListeners();
        setAsApplicationVisibleView();
    }

    @Override
    public void setAsApplicationVisibleView() {
        this.application.navigate(null, this.view.getContentPane());
    }

    private void loadFeed() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                PublicationsService.sharedInstance.loadAll();
                return true;
            }

            // Can safely update the GUI from this method.
            protected void done() {
                showPublications();
            }
        };
        worker.execute();
    }




    private void showPublications() {

    }

    private void setButtonHoverListeners() {
        final NavBarView navBarView = application.getNavBarView();
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

    }

    @Override
    public void onEvent(String event, JSONObject obj) {

    }

    @Override
    public void registerForEvents() {
        socketManger.listen(SocketEvent.NUM_CLIENTS, this);
        socketManger.listen(SocketEvent.CHAT_MESSAGE, this);
        socketManger.listen(SocketEvent.NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION, this);
        sendChatMessage("Hey, World! I've registered to hear everything you have to say.");
    }

    private void sendChatMessage(String message) {
        socketManger.emit(SocketEvent.CHAT_MESSAGE, ChatMessage.createJSONPayload("eb297ea1161a", "user1", message));
    }
}
