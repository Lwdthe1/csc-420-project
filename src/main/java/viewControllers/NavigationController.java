package viewControllers;

import utils.WebService.socketio.SocketManager;
import viewControllers.interfaces.AppView;
import viewControllers.interfaces.AppViewController;
import viewControllers.interfaces.View;
import viewControllers.interfaces.ViewController;
import views.subviews.NavBarView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * Created by lwdthe1 on 9/5/16.
 */
public class NavigationController implements ViewController {
    private final NavBarView view;
    private final MainApplication application;
    private HashMap<String, AppViewController> viewControllersMap = new HashMap<>();

    private SocketManager socketManger;

    public NavigationController(MainApplication application) {
        this.application = application;
        this.view = new NavBarView(application.getMainFrame().getWidth());
        setupView();
        setActionListeners();
    }

    @Override
    public View getView() {
        return view;
    }

    public void setupView() {
        this.view.createAndShow();
    }

    private void setActionListeners() {
        view.getPublicationsTabButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeFeedViewController viewController = (HomeFeedViewController) getViewController(ViewControllerKey.PUBLICATIONS);
                    moveTo(viewController);
            }
        });

        view.getProfileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserRequestsFeedViewController viewController = (UserRequestsFeedViewController) getViewController(ViewControllerKey.PROFILE);
                moveTo(viewController);
            }
        });
    }

    public void moveTo(AppViewController viewController) {
        this.application.setVisibleView(viewController.getView());
    }

    private enum ViewControllerKey {
        PUBLICATIONS("Publications"),
        PROFILE("Profile");

        private String value;
        ViewControllerKey(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    private AppViewController getViewController(ViewControllerKey key) {
        if (!viewControllersMap.containsKey(key.getValue())) {
            switch (key) {
                case PUBLICATIONS:
                    viewControllersMap.put(key.getValue(), new HomeFeedViewController(application));
                    break;
                case PROFILE:
                    viewControllersMap.put(key.getValue(), new UserRequestsFeedViewController(application));
                    break;
            }
        }
        return viewControllersMap.get(key.getValue());
    }
}
