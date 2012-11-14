package org.chimi.s4t.springconfig;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextConfigTest {

	@Test
	public void getBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationContextConfig.class);
		assertNotNull(context.getBean("dataSource", DataSource.class));
		assertNotNull(context.getBean("resultCallbackFactory",
				ResultCallbackFactory.class));
	}
}
