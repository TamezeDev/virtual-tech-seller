package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
