package com.example.firstcommit.repository;

import com.example.firstcommit.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    List<Candidate> findAllByUser_Username(String username);

    boolean existsByEmail(String email);
}
