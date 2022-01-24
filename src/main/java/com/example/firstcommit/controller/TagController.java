package com.example.firstcommit.controller;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TagController {

    TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> create(@RequestBody Tag tag) {
        if (tag.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tagService.save(tag));
    }

    @PutMapping("/tags")
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        if (tag.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tagService.save(tag));
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Tag> tagOpt = tagService.findById(id);
        if (tagOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Candidate> candidates = tagOpt.get().getCandidates();
        for (Candidate c : candidates) {
            c.getTags().remove(tagOpt.get());
        }
        boolean result = tagService.deleteById(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
