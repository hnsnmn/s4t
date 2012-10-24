package org.chimi.s4t.application.transcode;

public interface TranscodingExceptionHandler {

	void notifyToJob(Long jobId, RuntimeException ex)
			throws RuntimeException;

}
