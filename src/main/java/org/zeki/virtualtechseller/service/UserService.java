package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login (String email, String pass){
        System.out.println(userRepository.matchCredentials(email, pass));
        return false;
    }
}
