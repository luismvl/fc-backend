package com.example.firstcommit.service.impl;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.repository.CandidateRepository;
import com.example.firstcommit.repository.TagRepository;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

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
        return tagRepository.save(tag);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || !tagRepository.existsById(id)) {
            return false;
        }
        tagRepository.deleteById(id);
        return true;
    }


}
