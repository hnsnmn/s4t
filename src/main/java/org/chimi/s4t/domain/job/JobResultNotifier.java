package org.chimi.s4t.domain.job;

public interface JobResultNotifier {

	void notifyToRequester(Long jobId);

}
