package com.example.firstcommit.controller;

import com.example.firstcommit.dto.MessageDTO;
import com.example.firstcommit.entities.Tag;
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

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> findAll(@PathVariable Long id) {
        Optional<Tag> tagOptional = tagService.findById(id);
        if (tagOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tagOptional.get());
    }

    @PostMapping("/tags")
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        if (tag.getId() != null || tag.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (tagService.existsByName(tag.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO("Tag with name '" + tag.getName() + "' already exists"));
        }
        return ResponseEntity.ok(tagService.save(tag));
    }

    @PutMapping("/tags")
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        if (tag.getId() == null || tag.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tagService.save(tag));
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean result = tagService.deleteById(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
