package org.chimi.s4t.domain.job;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DestinationStorageFactoryDefaultTest {

	private DestinationStorageFactory factory = DestinationStorageFactory.DEFAULT;

	@Test
	public void createFileDestinationStorage() {
		DestinationStorage destinationStorage = factory
				.create("file://usr/local");
		assertTrue(destinationStorage instanceof FileDestinationStorage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedDestination() {
		factory.create("xxx://www.daum.net/");
		fail("must throw exception");
	}

}
