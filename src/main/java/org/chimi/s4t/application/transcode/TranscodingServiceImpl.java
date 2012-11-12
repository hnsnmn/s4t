package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.Transcoder;

public class TranscodingServiceImpl implements TranscodingService {
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
		job.transcode(transcoder, thumbnailExtractor);
	}

}