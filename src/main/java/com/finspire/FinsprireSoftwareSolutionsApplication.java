package com.finspire;

import com.finspire.entity.Role;
import com.finspire.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class FinsprireSoftwareSolutionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinsprireSoftwareSolutionsApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository repository) {
		return args -> {
			List<String> requiredRoles = List.of("ADMIN");
			requiredRoles.forEach(roleName -> {
				if (repository.findByName(roleName).isEmpty()) {
					repository.save(Role.builder().name(roleName).build());
				}
			});
		};
	}
}
