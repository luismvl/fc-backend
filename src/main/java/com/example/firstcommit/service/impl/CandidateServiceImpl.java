package com.example.firstcommit.service.impl;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.repository.CandidateRepository;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.TagService;
import com.example.firstcommit.service.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CandidateServiceImpl implements CandidateService {

    CandidateRepository candidateRepository;
    UserService userService;
    TagService tagService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, UserService userService, TagService tagService) {
        this.candidateRepository = candidateRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public List<Candidate> findAll(Specification<Candidate> specification) {
        return candidateRepository.findAll(specification);
    }

    @Override
    public Optional<Candidate> findById(Long id) {
        if (id == null || id < 1) {
            return Optional.empty();
        }
        return candidateRepository.findById(id);
    }

    @Override
    public List<Candidate> findAllByUserUsername(String username) {
        return candidateRepository.findAllByUser_Username(username);
    }

    @Override
    public Candidate save(Candidate candidate) {
        // Si ya existe cambio solo las propiedades que me pasan (si es null, la ignoro y se deja la que tenía)
        if (candidate.getId() != null && candidateRepository.existsById(candidate.getId())) {
            candidateRepository.findById(candidate.getId()).ifPresent(candidateDB -> {
                candidate.setName(candidate.getName() == null ? candidateDB.getName() : candidate.getName());
                candidate.setEmail(candidate.getEmail() == null ? candidateDB.getEmail() : candidate.getEmail());
                candidate.setPhone(candidate.getPhone() == null ? candidateDB.getPhone() : candidate.getPhone());
                candidate.setCountry(candidate.getCountry() == null ? candidateDB.getCountry() : candidate.getCountry());
                candidate.setCity(candidate.getCity() == null ? candidateDB.getCity() : candidate.getCity());
                candidate.setRelocation(candidate.getRelocation() == null ? candidateDB.getRelocation() : candidate.getRelocation());
                candidate.setModality(candidate.getModality() == null ? candidateDB.getModality() : candidate.getModality());
                candidate.setCv_url(candidate.getCv_url() == null ? candidateDB.getCv_url() : candidate.getCv_url());
                candidate.setUser(candidate.getUser() == null ? candidateDB.getUser() : candidate.getUser());
                candidate.setTags(candidate.getTags().size() == 0 ? candidateDB.getTags() : candidate.getTags());
            });
            return candidateRepository.save(candidate);
        }

        // Verifico que las tags asociadas existan en la DB
        // Si una tag no existe, la ignoro
        Set<Tag> tagsFromDb = new HashSet<>();
        for (Tag t : candidate.getTags()) {
            Long id = t.getId();
            tagService.findById(id).ifPresent(tagsFromDb::add);
        }
        candidate.setTags(tagsFromDb);
        return candidateRepository.save(candidate);
    }

    @Override
    public boolean existsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return candidateRepository.existsById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || !candidateRepository.existsById(id)) {
            return false;
        }
        candidateRepository.deleteById(id);
        return true;
    }

    @Override
    public Candidate addTag(Long candidateId, Long tagId) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
        Optional<Tag> tagOptional = tagService.findById(tagId);
        if (candidateOptional.isEmpty() || tagOptional.isEmpty()) {
            throw new IllegalArgumentException("Candidato o tag incorrectos");
        }
        Candidate candidate = candidateOptional.get();
        candidate.getTags().add(tagOptional.get());
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate addTags(Long candidateId, List<Tag> tags) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
        if (candidateOptional.isEmpty()) {
            throw new IllegalArgumentException("Candidato no existe");
        }
        Candidate candidate = candidateOptional.get();
        Set<Tag> tagsFromDb = new HashSet<>();
        for (Tag t : tags) {
            Long id = t.getId();
            tagService.findById(id).ifPresent(tagsFromDb::add);
        }
        candidate.getTags().addAll(tagsFromDb);
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate removeTag(Long candidateId, Long tagId) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
        Optional<Tag> tagOptional = tagService.findById(tagId);
        if (candidateOptional.isEmpty() || tagOptional.isEmpty()) {
            throw new IllegalArgumentException("Candidato o tag incorrectos");
        }
        Candidate candidate = candidateOptional.get();
        candidate.getTags().remove(tagOptional.get());
        candidateRepository.save(candidate);
        return candidateRepository.save(candidate);
    }
}
