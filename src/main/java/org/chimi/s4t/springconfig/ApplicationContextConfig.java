package org.chimi.s4t.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import({ DomainConfig.class, RepositoryConfig.class, JpaConfig.class,
		FfmpegConfig.class, TranscodeApplicationConfig.class })
@ImportResource("classpath:spring/datasource.xml")
@EnableTransactionManagement
public class ApplicationContextConfig {

}
