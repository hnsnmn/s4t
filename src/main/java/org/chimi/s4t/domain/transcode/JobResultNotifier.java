package org.chimi.s4t.domain.transcode;

public interface JobResultNotifier {

	void notifyToRequester(Long jobId);

}
