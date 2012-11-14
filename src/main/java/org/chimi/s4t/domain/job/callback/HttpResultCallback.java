package org.chimi.s4t.domain.job.callback;

import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.Job.State;

public class HttpResultCallback extends ResultCallback {

	public HttpResultCallback(String url) {
		super(url);
	}

	@Override
	public void nofiySuccessResult(Long jobId) {
	}

	@Override
	public void nofiyFailedResult(Long jobId, State lastState, String errorCause) {
	}

}
