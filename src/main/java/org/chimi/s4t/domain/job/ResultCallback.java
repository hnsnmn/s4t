package org.chimi.s4t.domain.job;

import org.chimi.s4t.domain.job.Job.State;

public interface ResultCallback {

	void nofiySuccessResult(Long jobId);

	void nofiyFailedResult(Long jobId, State lastState, String errorCause);

}
