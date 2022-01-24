package com.example.firstcommit.service;

import com.example.firstcommit.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    Tag save(Tag tag);

    boolean deleteById(Long id);

}
