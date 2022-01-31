package com.example.firstcommit.controller;

import com.cloudinary.utils.ObjectUtils;
import com.example.firstcommit.dto.MessageDTO;
import com.example.firstcommit.dto.TagsListDTO;
import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.TagService;
import com.example.firstcommit.service.UserService;
import com.example.firstcommit.service.impl.CloudinaryService;
import com.example.firstcommit.utils.CandidateSpecificationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @Autowired
    CloudinaryService cloudinaryService;

    @GetMapping("/candidates")
    public List<Candidate> findAll(Authentication authentication) {
        return candidateService.findAllByUserUsername(authentication.getName());
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> findById(@PathVariable Long id, Authentication authentication) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty() || !candidateOptional.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateOptional.get());
    }

    @GetMapping("/candidates/search")
    public List<Candidate> search(@RequestParam(value = "search") String search, Authentication authentication) {
        CandidateSpecificationsBuilder builder = new CandidateSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?&*\\w*),");
        Matcher matcher = pattern.matcher(search + ",user:" + authentication.getName() + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Candidate> spec = builder.build();
        return candidateService.findAll(spec);
    }

    @PostMapping("/candidates")
    public ResponseEntity<?> create(@RequestBody Candidate candidate, Authentication authentication) {
        if (candidate.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        if (candidate.getName() == null || candidate.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageDTO("Both 'name' and 'email' must be set"));
        }
        if (candidateService.existsByEmail(candidate.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO("Candidate with email '" + candidate.getEmail() + "' already exists"));
        }

        User currentUser = userService.findByUsername(authentication.getName()).get();
        candidate.setUser(currentUser);
        return ResponseEntity.ok(candidateService.save(candidate));
    }

    @PutMapping("/candidates")
    public ResponseEntity<Candidate> update(@RequestBody Candidate candidateRequest, Authentication authentication) {
        if (candidateRequest.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Verifica si existe y si pertenece al usuario actual
        Optional<Candidate> candidateOptional = candidateService.findById(candidateRequest.getId());
        if (candidateOptional.isEmpty() || !candidateOptional.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateService.save(candidateRequest));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication authentication) {
        if (!candidateService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Candidate candidate = candidateService.findById(id).get();
        if (!candidate.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        boolean result = candidateService.deleteById(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/candidates/{id}/tags")
    public ResponseEntity<Set<Tag>> findAllTags(@PathVariable Long id, Authentication authentication) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty() || !candidateOptional.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateOptional.get().getTags());
    }

    @PostMapping("/candidates/{candidateId}/tags/{tagId}")
    public ResponseEntity<Candidate> addTag(@PathVariable Long candidateId, @PathVariable Long tagId) {
        if (candidateId == null || !candidateService.existsById(candidateId) || !tagService.existsById(tagId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateService.addTag(candidateId, tagId));
    }

    @PostMapping("/candidates/{id}/tags")
    public ResponseEntity<Candidate> addTags(@PathVariable Long id, @RequestBody TagsListDTO tagsList) {
        if (id == null || !candidateService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateService.addTags(id, tagsList.getTags()));
    }

    @DeleteMapping("/candidates/{candidateId}/tags/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable Long candidateId, @PathVariable Long tagId) {
        if (candidateId == null || !candidateService.existsById(candidateId) || !tagService.existsById(tagId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateService.removeTag(candidateId, tagId));
    }

    @PostMapping("/candidates/{candidateId}/image")
    public ResponseEntity<?> uploadImage(@PathVariable Long candidateId, @RequestBody MultipartFile image, Authentication authentication) {
        Optional<Candidate> candidateOpt = candidateService.findById(candidateId);
        if (candidateOpt.isEmpty() || !candidateOpt.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        System.out.println(image.getContentType());
        if (image == null || image.isEmpty() || image.getContentType() == null || !image.getContentType().startsWith("image")) {
            return ResponseEntity.badRequest().build();
        }
        Map<?, ?> result = null;
        Candidate candidate = candidateOpt.get();
        try {
            String filename = candidateId + "-" + candidate.getName().toLowerCase().replaceAll("\\s", "") + "-image";
            result = cloudinaryService.uploadImage(image, ObjectUtils.asMap(
                    "folder", "fc-images",
                    "public_id", filename,
                    "filename_override", filename,
                    "use_filename",  false,
                    "overwrite", true
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        candidate.setImage_url((String) result.get("url"));
        return ResponseEntity.ok().body(candidateService.save(candidate));
    }

    @PostMapping("/candidates/{candidateId}/cv")
    public ResponseEntity<?> uploadPdf(@PathVariable Long candidateId, @RequestBody MultipartFile pdf, Authentication authentication) {
        Optional<Candidate> candidateOpt = candidateService.findById(candidateId);
        if (candidateOpt.isEmpty() || !candidateOpt.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        if (pdf == null || pdf.isEmpty() || pdf.getContentType() == null || !pdf.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().build();
        }
        Map<?, ?> result = null;
        Candidate candidate = candidateOpt.get();
        try {
            String filename = candidateId + "-" + candidate.getName().toLowerCase().replaceAll("\\s", "") + "-cv";
            result = cloudinaryService.uploadPdf(pdf, ObjectUtils.asMap(
                    "folder", "fc-cvs",
                    "resource_type", "pdf",
                    "pages", true,
                    "public_id", filename,
                    "filename_override", filename,
                    "use_filename",  false,
                    "overwrite", true
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        candidate.setCv_url((String) result.get("url"));
        return ResponseEntity.ok().body(candidateService.save(candidate));
    }
}
