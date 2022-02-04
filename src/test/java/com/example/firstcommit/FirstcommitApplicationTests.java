package com.example.firstcommit;

import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FirstcommitApplicationTests {

    @Autowired
    TagService tagService;

    @Test
    void contextLoads() {
        Tag tag1 = new Tag();
        tag1.setId(1L);


        tagService.save(new Tag(null, "React"));

        Tag tagDB = tagService.findById(1L).get();

    }

}
