package viewControllers;

import models.ChatMessage;
import models.Publication;
import models.RequestDecisionNotification;
import org.json.JSONObject;
import sun.applet.Main;
import utils.PublicationsService;
import utils.WebService.socketio.SocketEvent;
import utils.WebService.socketio.SocketListener;
import utils.WebService.socketio.SocketManager;
import viewControllers.interfaces.AppView;
import viewControllers.interfaces.AppViewController;
import views.appViews.HomeFeedView;
import views.subviews.NavBarView;
import views.PublicationPageView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * Created by keithmartin on 12/5/16.
 */
public class PublicationPageViewController implements SocketListener, AppViewController {
    private final PublicationPageView view;
    private final HomeFeedViewController homeFeedViewController;

    private SocketManager socketManger;
    private Semaphore setupViewWhileLoadingSemaphore = new Semaphore(1);
    private Publication publication;
    private ArrayList<ChatMessage> chatMessages;

    public PublicationPageViewController(HomeFeedViewController homeFeedViewController, Publication publication) {
        this.homeFeedViewController = homeFeedViewController;
        this.publication = publication;
        this.view = new PublicationPageView(this, homeFeedViewController.getView().getWidth(), homeFeedViewController.getView().getHeight());
        setupView();
        loadChatMessages();
    }

    @Override
    public NavigationController getNavigationController() {
        return homeFeedViewController.getNavigationController();
    }

    @Override
    public AppView getView() {
        return view;
    }

    public void setupView() {
        this.view.createAndShow();
        setAsApplicationVisibleView();
    }

    @Override
    public void transitionTo(AppViewController appViewController) {

    }

    public Publication getPublication() {
        return publication;
    }

    public void setAsApplicationVisibleView() {

    }

    private void loadChatMessages() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                chatMessages = PublicationsService.sharedInstance.getChatMessages(publication.getId());
                return true;
            }

            // Can safely update the GUI from this method.
            protected void done() {
                showChatMessages();
            }
        };
        worker.execute();
    }

    private void showChatMessages() {
        setupChatMessagesTable(chatMessages);
    }

    private void setupChatMessagesTable(ArrayList<ChatMessage> chatMessages) {
        DefaultTableModel model = new DefaultTableModel();
        view.getTable().setModel(model);
        model.addColumn("", chatMessages.toArray());
        model.addColumn("", chatMessages.toArray());
        //model.addColumn("", chatMessages.toArray());

        view.getTable().getColumnModel().getColumn(1).setPreferredWidth(400);
        view.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
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
