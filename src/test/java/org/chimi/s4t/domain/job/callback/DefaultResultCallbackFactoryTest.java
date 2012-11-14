package org.chimi.s4t.domain.job.callback;

import static org.junit.Assert.*;

import org.chimi.s4t.domain.job.ResultCallback;
import org.junit.Test;

public class DefaultResultCallbackFactoryTest {

	private DefaultResultCallbackFactory callbackFactory = new DefaultResultCallbackFactory();

	@Test
	public void shouldCreateHttpResultCallbackWhenUrlIsHttp() {
		ResultCallback callback = callbackFactory
				.create("http://localhost:9999/transcode/callback");
		assertTrue(callback instanceof HttpResultCallback);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenUrlIsNotSupported() {
		callbackFactory.create("xxxx://localhost:9999/transcode/callback");
	}

}
