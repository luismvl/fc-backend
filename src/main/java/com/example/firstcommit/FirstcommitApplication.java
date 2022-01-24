package com.example.firstcommit;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.repository.UserRepository;
import com.example.firstcommit.service.CandidateService;
import com.example.firstcommit.utils.Modality;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.repository.CandidateRepository;
import com.example.firstcommit.repository.TagRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class FirstcommitApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(FirstcommitApplication.class, args);

		CandidateRepository candidateRepository = context.getBean(CandidateRepository.class);
		TagRepository tagRepository = context.getBean(TagRepository.class);
		UserRepository userRepository = context.getBean(UserRepository.class);

		Candidate candidate1 = new Candidate(null, "Luis Manuel Vela Linares", "luis@mail.com",
				"+58 424 123 4567", "Venezuela", "Barinas", true, Modality.MIXED, "url");
		candidateRepository.save(candidate1);

		Tag tag1 = new Tag(null, "React");
		tagRepository.save(tag1);
		Tag tag2 = new Tag(null, "HTML&CSS");
		tagRepository.save(tag2);
		Tag tag3 = new Tag(null, "Java");
		tagRepository.save(tag3);

		candidate1.getTags().add(tag1);
		candidate1.getTags().add(tag2);
		candidateRepository.save(candidate1);

		User user1 = new User(null, "user1", "user name","user1@mail.com", "password1");
		userRepository.save(user1);
		user1.getCandidates().add(candidate1);
		userRepository.save(user1);



	}

}
