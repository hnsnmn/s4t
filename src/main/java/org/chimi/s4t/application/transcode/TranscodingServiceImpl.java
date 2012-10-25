package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;

public class TranscodingServiceImpl implements TranscodingService {
	private MediaSourceCopier mediaSourceCopier;
	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;
	private CreatedFileSaver createdFileSaver;
	private JobResultNotifier jobResultNotifier;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier,
			Transcoder transcoder, ThumbnailExtractor thumbnailExtractor,
			CreatedFileSaver createdFileSaver,
			JobResultNotifier jobResultNotifier,
			JobRepository jobRepository) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSaver = createdFileSaver;
		this.jobResultNotifier = jobResultNotifier;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(mediaSourceCopier, transcoder, thumbnailExtractor,
				createdFileSaver, jobResultNotifier);
	}

}