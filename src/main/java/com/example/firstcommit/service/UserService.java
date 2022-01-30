package com.example.firstcommit.service;

import com.example.firstcommit.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    boolean existByUsername(String username);


}
