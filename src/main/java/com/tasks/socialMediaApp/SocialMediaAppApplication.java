package com.tasks.socialMediaApp;

import com.tasks.socialMediaApp.model.Profile;
import com.tasks.socialMediaApp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class SocialMediaAppApplication {

	public static void main(String[] args) {

		ApplicationContext ac = SpringApplication.run(SocialMediaAppApplication.class, args);

		Queries q = ac.getBean(Queries.class);

		q.createUser();

	}

}
