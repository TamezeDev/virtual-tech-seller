package org.zeki.virtualtechseller.model.user;

public class UserFactory {

    public User createUser(UserRole USerRole) {
        return switch (USerRole) {
            case ADMIN -> new Admin();
            case CLIENT -> new Client();
            case MODERATOR -> new Moderator();
        };
    }
}