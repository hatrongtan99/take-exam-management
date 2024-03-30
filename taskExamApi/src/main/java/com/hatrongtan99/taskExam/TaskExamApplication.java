package com.hatrongtan99.taskExam;

import com.hatrongtan99.taskExam.entity.AuthorityEntity;
import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.repository.AuthorityRepo;
import com.hatrongtan99.taskExam.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class TaskExamApplication {

	@Autowired
	public PasswordEncoder passwordEncoder;
	@Autowired
	private AuthorityRepo authorityRepo;
	@Autowired
	private UserRepo userRepo;

	private static final String DEFAULT_EMAIL_TEACHER = "teacher@example.com";
	private static final String DEFAULT_PASSWORD_TEACHER = "12345678";

	public static void main(String[] args) {
		SpringApplication.run(TaskExamApplication.class, args);
	}

	@Transactional
	@Bean
	public CommandLineRunner init() {
		return args -> {
			String[] roleInit = {"TEACHER", "STUDENT"};
			for(String role : roleInit) {
				AuthorityEntity exist = this.authorityRepo.findByName(role).orElse(null);
				if (exist == null) {
					this.authorityRepo.saveAndFlush(AuthorityEntity.builder()
							.name(role)
							.isActive(true)
							.build());
				}
			}

			UserEntity defaultTeacher = this.userRepo.findByEmailAndIsActiveIsTrue(DEFAULT_EMAIL_TEACHER).orElse(null);
			if (defaultTeacher == null) {
				AuthorityEntity roleTeacher = this.authorityRepo.findByName("TEACHER").orElseThrow(
						() -> new RuntimeException("invalid role")
				);
                defaultTeacher = UserEntity.builder()
						.fullname("default teacher")
						.email(DEFAULT_EMAIL_TEACHER)
						.password(this.passwordEncoder.encode(DEFAULT_PASSWORD_TEACHER))
						.authorities(roleTeacher)
						.isActive(true)
						.build();
				this.userRepo.save(defaultTeacher);
			}
		};
	}
}
