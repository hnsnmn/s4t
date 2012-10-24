package org.chimi.s4t.application.transcode;

public interface JobResultNotifier {

	void notifyToRequester(Long jobId);

}
