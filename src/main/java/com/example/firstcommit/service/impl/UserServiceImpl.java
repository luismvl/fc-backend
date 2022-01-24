package com.example.firstcommit.service.impl;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.repository.UserRepository;
import com.example.firstcommit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Candidate> findAllCandidatesById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get().getCandidates();
        }
        return new ArrayList<>();
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User incorrecto");
        }
        return userRepository.save(user);
    }


}
