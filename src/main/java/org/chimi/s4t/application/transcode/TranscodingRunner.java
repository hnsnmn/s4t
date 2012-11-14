package org.chimi.s4t.application.transcode;

public class TranscodingRunner {

	private TranscodingService transcodingService;
	private JobQueue jobQueue;

	public TranscodingRunner(TranscodingService transcodingService,
			JobQueue jobQueue) {
		this.transcodingService = transcodingService;
		this.jobQueue = jobQueue;
	}

	public void run() {
		while (true) {
			Long jobId = null;
			try {
				jobId = getNextWaitingJob();
			} catch (JobQueue.ClosedException ex) {
				break;
			}
			runTranscoding(jobId);
		}
	}

	private Long getNextWaitingJob() {
		return jobQueue.nextJobId();
	}

	private void runTranscoding(Long jobId) {
		try {
			transcodingService.transcode(jobId);
		} catch (RuntimeException ex) {
		}
	}

	public void stop() {

	}
}
