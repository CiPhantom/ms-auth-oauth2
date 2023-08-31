package com.actia.msauthenticationoauth2.service;

import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByMatricule(String matricule) {
        return userRepository.findUserByMatricule(matricule);
    }
}
