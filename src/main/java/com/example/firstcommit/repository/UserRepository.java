package com.example.firstcommit.repository;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
