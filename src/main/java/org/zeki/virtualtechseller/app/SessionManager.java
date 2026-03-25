package org.zeki.virtualtechseller.app;

import org.zeki.virtualtechseller.model.user.User;

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
    }

    public boolean isLogged() {
        return currentUser != null;
    }
}
