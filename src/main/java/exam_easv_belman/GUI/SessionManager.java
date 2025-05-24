package exam_easv_belman.GUI;
import exam_easv_belman.BE.User;

//Thread-safe singleton user session manager class
public class SessionManager {
    //volatile to stop partially created objects from being used.
    private static volatile SessionManager instance;
    private User currentUser;
    private String currentOrderNumber;

    private SessionManager() {}

    //Double-checked locking to ensure thread safety.
    public static SessionManager getInstance() {
        //if the instance is null at the time of accessing enter if statement
        if (instance == null) {
            //if multiple threads wants to access this at the same time - race conditions - the synchronized keyword
            //locks this to only allow one thread at a time.
            synchronized (SessionManager.class) {
                //if the instance is null, create a new object.
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentOrderNumber(String orderNumber) {
        this.currentOrderNumber = orderNumber;
    }
    public String getCurrentOrderNumber() {
        return currentOrderNumber;
    }


    //TODO remove souts when this works
    public void logout() {
        System.out.println(currentUser + " logged out");
        currentUser = null;
        System.out.println("Logged out successfully");
        currentOrderNumber = null;

    }
}