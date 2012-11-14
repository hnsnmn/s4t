package org.chimi.s4t.domain.job.callback;

import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.ResultCallbackFactory;

public class DefaultResultCallbackFactory implements ResultCallbackFactory {

	@Override
	public ResultCallback create(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return new HttpResultCallback(url);
		}
		throw new IllegalArgumentException();
	}

}
