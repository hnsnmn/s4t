package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;

public class TranscodingRunner {

	private TranscodingService transcodingService;
	private JobRepository jobRepository;

	public TranscodingRunner(TranscodingService transcodingService,
			JobRepository jobRepository) {
		this.transcodingService = transcodingService;
		this.jobRepository = jobRepository;
	}

	public void run() {
		Job job = getNextJob();
		runTranscoding(job);
	}

	private Job getNextJob() {
		return jobRepository.findEldestJobOfCreatedState();
	}

	private void runTranscoding(Job job) {
		if (job == null)
			return;
		transcodingService.transcode(job.getId());
	}

}
