package com.example.firstcommit.service.impl;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.repository.TagRepository;
import com.example.firstcommit.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    public Optional<Tag> findById(Long id) {
        if (id == null || id < 1) {
            return Optional.empty();
        }
        return tagRepository.findById(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag save(Tag tag) {
        tag.setName(tag.getName().toUpperCase());
        return tagRepository.save(tag);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || !tagRepository.existsById(id)) {
            return false;
        }
        Tag tag = tagRepository.findById(id).get();
        Set<Candidate> candidates = tag.getCandidates();
        for (Candidate c : candidates) {
            c.getTags().remove(tag);
        }
        tagRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return tagRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name.toUpperCase());
    }


}
