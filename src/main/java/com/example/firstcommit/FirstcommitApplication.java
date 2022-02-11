package com.example.firstcommit;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.service.TagService;
import com.example.firstcommit.service.UserService;
import com.example.firstcommit.utils.Modality;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class FirstcommitApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(FirstcommitApplication.class, args);

        CandidateService candidateService = context.getBean(CandidateService.class);
        TagService tagService = context.getBean(TagService.class);
        UserService userService = context.getBean(UserService.class);

        Candidate candidate1 = new Candidate(null, "Luis Manuel Vela Linares", "luis@mail.com",
                "+58 424 123 4567", "Venezuela", "Barinas", true, Modality.REMOTE);
        candidateService.save(candidate1);
        Candidate candidate2 = new Candidate(null, "Daniel Gonzalez", "daniel@mail.com",
                "+58 424 558 1142", "Colombia", "Bogotá", true, Modality.REMOTE);
        candidateService.save(candidate2);

        Tag tag1 = new Tag(null, "React");
        tagService.save(tag1);
        Tag tag2 = new Tag(null, "HTML&CSS");
        tagService.save(tag2);
        Tag tag3 = new Tag(null, "Java");
        tagService.save(tag3);
        Tag tag4 = new Tag(null, "Spring");
        tagService.save(tag4);


        candidate1.getTags().add(tag1);
        candidate1.getTags().add(tag2);
        candidateService.save(candidate1);

        candidate2.getTags().add(tag3);
        candidate2.getTags().add(tag1);
        candidateService.save(candidate2);

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        User user1 = new User(null, "demo", "demo@mail.com", passwordEncoder.encode("demo"));
        userService.save(user1);
        Candidate candidate1DB = candidateService.findById(1L).get();
        Candidate candidate2DB = candidateService.findById(2L).get();
        candidate1DB.setUser(new User(1L));
        candidate2DB.setUser(new User(1L));
        candidateService.save(candidate1DB);
        candidateService.save(candidate2DB);

        User user2 = new User(null, "username2", "user2@mail.com", passwordEncoder.encode("password2"));
        userService.save(user2);

        String[] tags = {"Angular", "Hibernate", "PHP", "TypeScript", "Symfony", ".NET", "Docker", "C#", "SQL", "React Native", "Lavarel"};
        Arrays.stream(tags).map(s -> new Tag(null, s)).forEach(tagService::save);
    }

}
