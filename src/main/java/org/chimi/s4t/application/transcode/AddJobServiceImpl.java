package org.chimi.s4t.application.transcode;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.ResultCallbackFactory;

public class AddJobServiceImpl implements AddJobService {

	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;
	private ResultCallbackFactory resultCallbackFactory;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory,
			ResultCallbackFactory resultCallbackFactory,
			JobRepository jobRepository) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
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
		ResultCallback resultCallback = resultCallbackFactory.create(request
				.getResultCallback());
		return new Job(mediaSourceFile, destinationStorage,
				request.getOutputFormats(), resultCallback);
	}

	private Job saveJob(Job job) {
		return jobRepository.save(job);
	}

}
