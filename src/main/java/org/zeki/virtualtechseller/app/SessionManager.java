package org.zeki.virtualtechseller.app;

import lombok.Getter;
import org.zeki.virtualtechseller.model.user.User;

@Getter
public class SessionManager {
    // Singleton pattern to get only one user for session
    private static SessionManager INSTANCE;

    private User currentUser;

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionManager();
        }
        return INSTANCE;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        AppContext.getInstance().getConnectionManager().getDatabaseConfig().useDefaultConnection();
    }

    public boolean isLogged() {
        return currentUser != null;
    }

}
