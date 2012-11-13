package org.chimi.s4t.domain.job.destination;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.destination.FileDestinationStorage;
import org.junit.Test;

public class DefaultDestinationStorageFactoryTest {

	private DestinationStorageFactory factory = new DefaultDestinationStorageFactory();

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
