package org.chimi.s4t.domain.transcode;

public interface TranscodingExceptionHandler {

	void notifyToJob(Long jobId, RuntimeException ex)
			throws RuntimeException;

}
