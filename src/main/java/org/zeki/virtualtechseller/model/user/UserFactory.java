package org.zeki.virtualtechseller.model.user;

public class UserFactory {

    public User createUser(Role role) {
        return switch (role) {
            case ADMIN -> new Admin();
            case CLIENT -> new Client();
            case MODERATOR -> new Moderator();
        };
    }
}