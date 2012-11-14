package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.Transcoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranscodingServiceImpl implements TranscodingService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(Transcoder transcoder,
			ThumbnailExtractor thumbnailExtractor, JobRepository jobRepository) {
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		checkJobExists(jobId, job);
		transcode(job);
	}

	private void transcode(Job job) {
		try {
			job.transcode(transcoder, thumbnailExtractor);
		} catch (RuntimeException ex) {
			logger.error("fail to do transcoding job {}", job.getId(), ex);
			throw ex;
		}
	}

	private void checkJobExists(Long jobId, Job job) {
		if (job == null) {
			throw new JobNotFoundException(jobId);
		}
	}

}