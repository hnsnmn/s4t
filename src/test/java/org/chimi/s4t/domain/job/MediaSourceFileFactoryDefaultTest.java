package org.chimi.s4t.domain.job;

import static org.junit.Assert.*;

import org.junit.Test;

public class MediaSourceFileFactoryDefaultTest {

	private MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;

	@Test
	public void createLocalStorageMediaSourceFile() {
		MediaSourceFile sourceFile = factory
				.create("file://./src/test/resources/sample.avi");
		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedSource() {
		factory.create("xxx://www.daum.net/");
		fail("must throw exception");
	}
}
