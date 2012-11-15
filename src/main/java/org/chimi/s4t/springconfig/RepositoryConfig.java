package org.chimi.s4t.springconfig;

import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.chimi.s4t.infra.persistence.DbJobRepository;
import org.chimi.s4t.infra.persistence.JobDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.chimi.s4t.infra.persistence")
public class RepositoryConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobDataDao jobDataDao;

	@Bean
	public JobRepository jobRepository() {
		return new DbJobRepository(jobDataDao, mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory);
	}
}
