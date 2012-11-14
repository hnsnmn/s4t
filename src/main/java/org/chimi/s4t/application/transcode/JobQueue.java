package org.chimi.s4t.application.transcode;

public interface JobQueue {

	void add(Long jobId);

	Long nextJobId();

	@SuppressWarnings("serial")
	public class ClosedException extends RuntimeException {

	}

}
