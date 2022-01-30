package com.example.firstcommit.service;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


public interface CandidateService {

    List<Candidate> findAll(Specification<Candidate> specification);

    Optional<Candidate> findById(Long id);

    List<Candidate> findAllByUserUsername(String username);

    Candidate save(Candidate candidate);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    boolean deleteById(Long id);

    Candidate addTag(Long candidateId, Long tagId);

    Candidate addTags(Long candidateId, List<Tag> tags);

    Candidate removeTag(Long candidateId, Long tagId);

}
