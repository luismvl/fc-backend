package com.example.firstcommit.controller;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    Lo dejo así mientras implemento seguridad

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}/candidates")
    public List<Candidate> findAllCandidatesById(@PathVariable Long id){
        if( id == null || id <= 0){
            return new ArrayList<>();
        }
        return userService.findAllCandidatesById(id);
    }


}
