package org.chimi.s4t.domain.job.mediasource;

import static org.junit.Assert.*;

import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.mediasource.LocalStorageMediaSourceFile;
import org.junit.Test;

public class MediaSourceFileFactoryDefaultTest {

	private MediaSourceFileFactory factory = new DefaultMediaSourceFileFactory();

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
