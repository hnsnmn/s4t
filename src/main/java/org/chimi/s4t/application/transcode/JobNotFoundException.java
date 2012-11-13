package org.chimi.s4t.application.transcode;

@SuppressWarnings("serial")
public class JobNotFoundException extends RuntimeException {

	public JobNotFoundException(Long jobId) {
		super("Not found Job[" + jobId + "]");
	}

}
