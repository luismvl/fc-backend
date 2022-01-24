package com.example.firstcommit.service;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.entities.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    List<Candidate> findAllCandidatesById(Long id);

    User save(User user);

}
