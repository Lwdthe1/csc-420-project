package viewControllers;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import models.ChatMessage;
import models.CurrentUser;
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
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.awt.event.*;
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
    private PublicationsService publicationsService;

    public PublicationPageViewController(HomeFeedViewController homeFeedViewController, Publication publication) {
        this.homeFeedViewController = homeFeedViewController;
        this.publication = publication;
        this.publicationsService = homeFeedViewController.getPublicationsService();

        //acquire the lock here before starting load
        try {
            setupViewWhileLoadingSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publicationsService = PublicationsService.sharedInstance;
        loadChatMessages();
        this.view = new PublicationPageView(this, homeFeedViewController.getView().getWidth(), homeFeedViewController.getView().getHeight());
        setupView();
        setupViewWhileLoadingSemaphore.release();
        System.out.println();
        startSocketIO();
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
        //Check if user can chat or not

    }

    @Override
    public void transitionTo(AppViewController appViewController) {

    }

    @Override
    public void viewWillAppear() {
        view.refreshTable();
    }

    public Publication getPublication() {
        return publication;
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
        try {
            setupViewWhileLoadingSemaphore.acquire();
            setupChatMessagesTable(chatMessages);

//            if (!publication.currentUserIsContributor()) {
//                System.out.println("NOT CONTRIBUTOR");
//                view.getSendMessageButton().setEnabled(false);
//                view.getChatTextArea().setText("You must be a contributor of this publication to chat.");
//                view.getChatTextArea().setEnabled(false);
//            } else {
//                System.out.println("good to go");
                view.getSendMessageButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("SENDING MESSAGE: " + view.getChatTextArea().getText());
                        sendChatMessage(view.getChatTextArea().getText());
                    }
                });
            //}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                Publication chatPub = publicationsService.getById(chatMessage.getPublicationId());
                if (chatPub == null) {
                    break;
                } else if (chatPub.getId() == publication.getId()) {
                    DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
                    model.addRow(new Object[] {chatMessage, chatMessage});
                    view.refreshTable();
                }

                view.getRealTimeNotificationView().updateNotification("New Contributor Message",
                        format("A contributor said: %s", chatMessage.getText()),
                        chatPub.getImage()
                );
                break;
            case NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION:
                RequestDecisionNotification requestDecisionNotification = new RequestDecisionNotification(payload);
                System.out.printf("\nReceived requestDecisionNotification: %s\n", requestDecisionNotification);
                Publication requestPub = publicationsService.getById(requestDecisionNotification.getPublicationId());
                if (requestPub == null) break;

                for(int i = 0; i < 1; i++) { //THIS LOOP IS ONLY HERE FOR TESTING!
                    boolean requestApproved = requestDecisionNotification.getAccepted();
                    String title = requestApproved? "Request Approved" : "Request Denied";
                    view.getRealTimeNotificationView().updateNotification(title,
                            format("Your request to contribute to %s was %s",
                                    requestPub.getName(),
                                    requestApproved? "approved." : "denied."),
                            requestPub.getImage()
                    );
                }
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
        socketManger.listen(SocketEvent.NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION, this);
        //sendChatMessage("Hey, World! I've registered to hear everything you have to say.");
    }

    private void sendChatMessage(String message) {
        socketManger.emit(SocketEvent.CHAT_MESSAGE, ChatMessage.createJSONPayload(publication.getId(), "LincolnWDaniel", message));
    }
}
