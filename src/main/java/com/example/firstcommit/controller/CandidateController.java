package com.example.firstcommit.controller;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.service.CandidateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CandidateController {

    CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @ApiOperation("Obtener todos los candidatos creados")
    @GetMapping("/candidates")
    public List<Candidate> findAll() {
        return candidateService.findAll();
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> findById(@PathVariable Long id) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isPresent()) {
            return ResponseEntity.ok(candidateOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/candidates")
    public ResponseEntity<Candidate> create(@RequestBody Candidate candidate) {
        if (candidate.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(candidateService.save(candidate));
    }

    @PutMapping("/candidates/{id}")
    public ResponseEntity<Candidate> update(@RequestBody Candidate candidate) {
        if (candidate.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(candidateService.save(candidate));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean result = candidateService.deleteById(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
