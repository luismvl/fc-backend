package com.example.firstcommit;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.repository.CandidateRepository;
import com.example.firstcommit.service.TagService;
import com.example.firstcommit.specification.CandidateSpecification;
import com.example.firstcommit.utils.Modality;
import com.example.firstcommit.utils.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class JPASpecificationsTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TagService tagService;

    private Candidate candidateLuis;
    private Candidate candidateDaniel;

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag(null, "React");
        tagService.save(tag1);
        Tag tag2 = new Tag(null, "HTML&CSS");
        tagService.save(tag2);
        Tag tag3 = new Tag(null, "Java");
        tagService.save(tag3);

        candidateLuis = new Candidate(null, "Luis Manuel Vela Linares", "luis@mail.com",
                "+58 424 123 4567", "Venezuela", "Barinas", true, Modality.MIXED);
        candidateLuis.getTags().add(tag2);
        candidateLuis.getTags().add(tag1);
        candidateRepository.save(candidateLuis);

        candidateDaniel = new Candidate(null, "Daniel Gonzalez", "daniel@mail.com",
                "+58 424 558 1142", "Colombia", "Bogotá", true, Modality.REMOTE);
        candidateDaniel.getTags().add(tag1);
        candidateDaniel.getTags().add(tag3);
        candidateRepository.save(candidateDaniel);
    }

    @Test
    void searchByTags() {
        CandidateSpecification spec =
                new CandidateSpecification(new SearchCriteria("tags", ":", "[1,2]"));
        System.out.println("Luis tags - " + candidateLuis.getName());
        candidateLuis.getTags().forEach(tag -> System.out.println("\t" + tag));

        List<Candidate> result = candidateRepository.findAll(spec);
        System.out.println("Result");
        for (Candidate c : result) {
            System.out.println(c.getName());
            c.getTags().forEach(tag -> System.out.println("\t" + tag));
        }
        assertEquals(1L, (long) result.get(0).getId());

    }
}
