package org.chimi.s4t.springconfig;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EmbeddedDatabaseTest {

	@Test
	public void dataSourceShouldExsts() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring-datasource.xml");
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertNotNull(dataSource);
	}
}
