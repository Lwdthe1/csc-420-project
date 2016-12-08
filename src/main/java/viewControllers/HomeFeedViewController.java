package viewControllers;

import models.ChatMessage;
import models.CurrentUser;
import models.Publication;
import models.RequestDecisionNotification;
import org.json.JSONObject;
import utils.PublicationsService;
import utils.WebService.socketio.SocketEvent;
import utils.WebService.socketio.SocketListener;
import utils.WebService.socketio.SocketManager;
import viewControllers.interfaces.AppView;
import viewControllers.interfaces.AppViewController;
import views.appViews.HomeFeedView;
import views.subviews.PublicationContributeButtonCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final NavigationController navigationController;

    private SocketManager socketManger;
    private Semaphore setupViewWhileLoadingSemaphore = new Semaphore(1);
    private PublicationsService publicationsService;
    private ArrayList<Publication> publications;

    public HomeFeedViewController(MainApplication application) {
        this.application = application;
        this.navigationController = new NavigationController(application);
        //acquire the lock here before starting load
        try {
            setupViewWhileLoadingSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publicationsService = PublicationsService.sharedInstance;
        loadCurrentUserRequestsToContribute();
        this.view = new HomeFeedView(this, application.getMainFrame().getWidth(), application.getMainFrame().getHeight());
        setupView();
        setupViewWhileLoadingSemaphore.release();
        startSocketIO();
    }

    @Override
    public NavigationController getNavigationController() {
        return this.navigationController;
    }

    @Override
    public AppView getView() {
        return view;
    }

    public void setupView() {
        this.view.createAndShow();
    }

    @Override
    public void transitionTo(AppViewController appViewController) {
        this.getNavigationController().moveTo(appViewController);
    }

    @Override
    public void viewWillAppear() {
        if (publications != null && !publications.isEmpty()) {
            view.refreshTable();
        }
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


    private void loadCurrentUserRequestsToContribute() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                PublicationsService.sharedInstance.loadAll();
                return true;
            }

            // Can safely update the GUI from this method.
            protected void done() {
                loadFeed();
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

    private void startSocketIO() {
        this.socketManger = SocketManager.sharedInstance;
        socketManger.listen(SocketEvent.CONNECTED, this);
        socketManger.setupAndConnect();
        registerForEvents();
    }

    @Override
    public void onEvent(SocketEvent event, JSONObject payload) {
        switch(event) {
            case NUM_CLIENTS:
                System.out.printf("\nNumber of active clients: %d\n", payload.get("value"));
                break;
            case CHAT_MESSAGE:
                updateRealtimeNotificationWithNewChatMessage(payload);
                break;
            case NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION:
                updateRealtimeNotificationWithNewRequestDecision(payload);
                break;
        }
    }

    private void updateRealtimeNotificationWithNewChatMessage(JSONObject payload) {
        ChatMessage chatMessage = new ChatMessage(payload);
        Publication chatPub = publicationsService.getById(chatMessage.getPublicationId());
        if (chatPub == null) return;

        view.getRealTimeNotificationView().updateNotification("New Contributor Message",
                format("A contributor said: %s", chatMessage.getText()),
                chatPub.getImage(), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //TODO(keith) move to publication's page and show most recent chat.
                    }
                }
        );
    }

    private void updateRealtimeNotificationWithNewRequestDecision(JSONObject payload) {
        RequestDecisionNotification requestDecisionNotification = new RequestDecisionNotification(payload);
        System.out.printf("\nReceived requestDecisionNotification: %s\n", requestDecisionNotification);
        Publication requestPub = publicationsService.getById(requestDecisionNotification.getPublicationId());
        if (requestPub == null) return;


        boolean requestApproved = requestDecisionNotification.getAccepted();
        CurrentUser.sharedInstance.getRequestToContributeByPubId(requestPub.getId()).updateAccepted(requestApproved);
        requestPub.setCurrentUserIsContributor(requestApproved);
        requestPub.setCurrentUserRequestWasRejected(!requestApproved);
        view.refreshTable();

        String title = requestApproved? "Request Approved" : "Request Denied";
        view.getRealTimeNotificationView().updateNotification(title,
                format("Your request to contribute to %s was %s",
                        requestPub.getName(),
                        requestApproved ? "approved." : "denied."),
                requestPub.getImage(), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //TODO(keith) move to publication's page
                    }
                }
        );
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
        socketManger.emit(SocketEvent.CHAT_MESSAGE, ChatMessage.createJSONPayload("eb297ea1161a", "LincolnWDaniel", message));
    }

    public void publicationContributeCellClicked(Publication index) {

    }

    public void publicationImageButtonClicked(Publication publication) {
        System.out.printf("%s image button clicked.", publication.getName());
        //TODO(keith) move to publication page.
        //navigationController.moveTo();
    }

    public void publicationContributeCellClicked(Publication publication, int row, int column) {
        PublicationContributeButtonCellRenderer homeFeedTableCell = publication.getHomeFeedTableCell();
        JButton contributeButton = homeFeedTableCell.contributeButton;
        if (contributeButton.getText() == "Contribute") {
            PublicationsService.sharedInstance.requestToContributeById(publication.getId());
        } else {
            PublicationsService.sharedInstance.retractRequestToContributeById(publication.getId());
        }
        view.onContributeRequestSuccess(row, column);
    }
}
