package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.Job.State;

public interface JobStateChanger {

	void chageJobState(Long jobId, State newJobState);

}
