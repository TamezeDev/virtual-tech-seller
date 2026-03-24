package org.zeki.virtualtechseller.model.user;

public interface UserManager {
    void activateUser(User user);
    void deactivateUser(User user);
    void updateUser(User user);
}
