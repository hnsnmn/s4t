package org.chimi.s4t.domain.job;

import static org.junit.Assert.*;

import org.junit.Test;

public class MediaSourceFileFactoryDefaultTest {

	@Test
	public void createLocalStorageMediaSourceFile() {
		MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;
		MediaSourceFile sourceFile = factory
				.create("file://./src/test/resources/sample.avi");
		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(sourceFile.getSourceFile().exists());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedSource() {
		MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;
		factory.create("xxx://www.daum.net/");
		fail("must throw exception");
	}
}
