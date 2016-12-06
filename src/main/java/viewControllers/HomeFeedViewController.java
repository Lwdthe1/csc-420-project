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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * Created by lwdthe1 on 9/5/16.
 */
public class HomeFeedViewController implements SocketListener, AppViewController {
    private final HomeFeedView view;
    private final MainApplication application;

    private SocketManager socketManger;
    private Semaphore setupViewWhileLoadingSemaphore = new Semaphore(1);
    private PublicationsService publicationsService;
    private ArrayList<Publication> publications;

    public HomeFeedViewController(MainApplication application) {
        this.application = application;
        //acquire the lock here before starting load
        try {
            setupViewWhileLoadingSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publicationsService = PublicationsService.sharedInstance;
        loadFeed();
        this.view = new HomeFeedView(this, application.getMainFrame().getWidth(), application.getMainFrame().getHeight());
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
        this.application.navigate(this.view.getContentPane());
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
        try {
            //acquire the lock to assure the view is ready
            setupViewWhileLoadingSemaphore.acquire();
            publications = publicationsService.getAll();
            publications.sort(new Comparator<Publication>() {
                @Override
                public int compare(Publication o1, Publication o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });

            setupPublicationsTable(publications);
            sendChatMessage(format("Just wanted y'all to know I'm viewing %d publications that are looking for Writers", publications.size()));
        } catch (InterruptedException e) {
            System.out.printf("\nCouldn't show publications because: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupPublicationsTable(ArrayList<Publication> publications) {
        DefaultTableModel model = new DefaultTableModel();
        view.getTable().setModel(model);

        model.addColumn("", publications.toArray());
        model.addColumn(format("%d Publications Looking for Writers", publications.size()), publications.toArray());
        model.addColumn("", publications.toArray());

        view.getTable().getColumnModel().getColumn(1).setPreferredWidth(400);
        view.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void setButtonHoverListeners() {
        final NavBarView navBarView = view.getNavBarView();
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
                Publication chatPub = publicationsService.getById(chatMessage.getPublicationId());
                if (chatPub == null) break;

                view.getRealTimeNotificationView().updateNotification("New Contributor Message",
                        format("A contributor said: %s", chatMessage.getText()),
                        chatPub.getImage()
                );
                try {
                    sleep(1 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendChatMessage("This is another test realtime message " + Math.random());
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
                                    requestApproved? "approved." + i : "denied." + i),
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
        sendChatMessage("Hey, World! I've registered to hear everything you have to say.");
    }

    private void sendChatMessage(String message) {
        socketManger.emit(SocketEvent.CHAT_MESSAGE, ChatMessage.createJSONPayload("eb297ea1161a", "user1", message));
    }

    public void publicationContributeCellClicked(int index) {
        System.out.printf("%s contribute cell clicked.", publications.get(index).getName());
        System.out.println(PublicationsService.sharedInstance.requestToContributeById(publications.get(index).getId(),"user" + index));
    }

    public void publicationImageButtonClicked(int index) {
        System.out.printf("%s image button clicked.", publications.get(index).getName());
        //TODO(keith) move to publication page.
    }
}
