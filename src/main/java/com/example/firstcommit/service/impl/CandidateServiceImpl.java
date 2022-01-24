package com.example.firstcommit.service.impl;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.repository.CandidateRepository;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    CandidateRepository candidateRepository;
    UserService userService;
    public CandidateServiceImpl(CandidateRepository candidateRepository, UserService userService) {
        this.candidateRepository = candidateRepository;
        this.userService = userService;
    }

    @Override
    public Optional<Candidate> findById(Long id) {
        if (id == null || id < 1) {
            return Optional.empty();
        }
        return candidateRepository.findById(id);
    }

    @Override
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    @Override
    public List<Candidate> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || !candidateRepository.existsById(id)) {
            return false;
        }

        Candidate candidate = candidateRepository.findById(id).get();
        List<User> users = candidate.getUsers();

        for (User u : users) {
            u.getCandidates().remove(candidate);
            userService.save(u);
        }

        candidateRepository.deleteById(id);
        return true;
    }
}
