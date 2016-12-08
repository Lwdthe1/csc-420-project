package viewControllers;

import models.*;
import org.json.JSONObject;
import utils.PublicationsService;
import utils.WebService.socketio.SocketEvent;
import utils.WebService.socketio.SocketListener;
import utils.WebService.socketio.SocketManager;
import viewControllers.interfaces.AppView;
import viewControllers.interfaces.AppViewController;
import views.appViews.UserRequestsFeedView;
import views.subviews.NavBarView;
import views.subviews.RequestStatusButtonCellRenderer;

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
public class UserRequestsFeedViewController implements SocketListener, AppViewController {
    private final MainApplication application;
    private NavigationController navigationController;
    private final UserRequestsFeedView view;

    private SocketManager socketManger;
    private PublicationsService publicationsService;
    private ArrayList<RequestToContribute> publicationRequests;

    public UserRequestsFeedViewController(MainApplication application) {
        this.application = application;
        this.navigationController = new NavigationController(application);

        publicationsService = PublicationsService.sharedInstance;

        this.view = new UserRequestsFeedView(this, application.getMainFrame().getWidth(), application.getMainFrame().getHeight());
        setupView();
        showPublicationRequests();
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

    }

    private void showPublicationRequests() {
        //acquire the lock to assure the view is ready
        publicationRequests = CurrentUser.sharedInstance.getRequestsToContribute();
        publicationRequests.sort(new Comparator<RequestToContribute>() {
            @Override
            public int compare(RequestToContribute o1, RequestToContribute o2) {
                return o1.getPublication().getName().compareToIgnoreCase(o1.getPublication().getName());
            }
        });

        setupPublicationsTable(publicationRequests);
    }

    private void setupPublicationsTable(ArrayList<RequestToContribute> publicationRequests) {
        DefaultTableModel model = new DefaultTableModel();
        view.getTable().setModel(model);

        model.addColumn("", publicationRequests.toArray());
        model.addColumn(format("Your %d Requests to Contribute", publicationRequests.size()), publicationRequests.toArray());
        model.addColumn("", publicationRequests.toArray());

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
        socketManger.listen(SocketEvent.CHAT_MESSAGE, this);
        socketManger.listen(SocketEvent.NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION, this);
    }

    public void publicationContributeCellClicked(Publication index) {

    }

    public void publicationImageButtonClicked(Publication publication) {
        System.out.printf("%s image button clicked.", publication.getName());
        //TODO(keith) move to publication page.
    }

    public void publicationContributeCellClicked(RequestToContribute requestToContribute, int row, int column) {
        RequestStatusButtonCellRenderer feedTableCell = requestToContribute.getFeedTableCell();
        JButton contributeButton = feedTableCell.contributeButton;
        if (contributeButton.getText() == "Contribute") {
            PublicationsService.sharedInstance.requestToContributeById(requestToContribute.getPublication().getId(), "LincolnWDaniel");
        } else {
            PublicationsService.sharedInstance.retractRequestToContributeById(requestToContribute.getPublication().getId(), "LincolnWDaniel");
        }
        view.onContributeRequestSuccess(row, column);
    }
}
