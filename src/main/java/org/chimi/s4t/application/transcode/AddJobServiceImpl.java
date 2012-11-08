package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;

public class AddJobServiceImpl implements AddJobService {

	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory,
			JobRepository jobRepository) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.jobRepository = jobRepository;
	}

	@Override
	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job savedJob = saveJob(job);
		return savedJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request
				.getMediaSource());
		DestinationStorage destinationStorage = destinationStorageFactory
				.create(request.getDestinationStorage());
		return new Job(mediaSourceFile, destinationStorage,
				request.getOutputFormats());
	}

	private Job saveJob(Job job) {
		return jobRepository.save(job);
	}

}
