package org.chimi.s4t.domain.transcode;

import org.chimi.s4t.domain.job.Job.State;

public interface JobStateChanger {

	void chageJobState(Long jobId, State newJobState);

}
