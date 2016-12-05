package viewControllers;

/**
 * Created by lwdthe1 on 12/5/16.
 */
public interface AppViewController {
    AppView getView();
    void setupView();
    void setAsApplicationVisibleView();
}
