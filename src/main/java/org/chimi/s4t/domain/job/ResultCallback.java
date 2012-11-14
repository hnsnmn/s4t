package org.chimi.s4t.domain.job;

import org.chimi.s4t.domain.job.Job.State;

public abstract class ResultCallback {

	private String url;

	public ResultCallback(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void nofiySuccessResult(Long jobId);

	public abstract void nofiyFailedResult(Long jobId, State lastState,
			String errorCause);

}
