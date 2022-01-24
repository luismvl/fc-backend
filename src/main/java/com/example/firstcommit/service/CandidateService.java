package com.example.firstcommit.service;

import com.example.firstcommit.entities.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    Optional<Candidate> findById(Long id);

    List<Candidate> findAll();

    List<Candidate> findAllByUserId(Long id);

    Candidate save(Candidate candidate);

    boolean deleteById(Long id);

}
