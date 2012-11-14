package org.chimi.s4t.springconfig;

import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.infra.repositories.JpaJobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.chimi.s4t.infra.persistence")
public class RepositoryConfig {

	@Bean
	public JobRepository jobRepository() {
		return new JpaJobRepository();
	}
}
