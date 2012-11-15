package org.chimi.s4t.infra.persistence;

import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.springframework.transaction.annotation.Transactional;

public class DbJobRepository implements JobRepository {

	private JobDataDao jobDataDao;
	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private ResultCallbackFactory resultCallbackFactory;

	public DbJobRepository(JobDataDao jobDataDao,
			MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory,
			ResultCallbackFactory resultCallbackFactory) {
		this.jobDataDao = jobDataDao;
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Transactional
	@Override
	public Job findById(Long jobId) {
		JobData jobData = jobDataDao.findById(jobId);
		if (jobData == null) {
			return null;
		}
		return createJobFromJobData(jobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return new JobImpl(jobDataDao, jobData.getId(), jobData.getState(),
				mediaSourceFileFactory.create(jobData.getSourceUrl()),
				destinationStorageFactory.create(jobData.getDestinationUrl()),
				jobData.getOutputFormats(),
				resultCallbackFactory.create(jobData.getCallbackUrl()),
				jobData.getExceptionMessage());
	}

	@Transactional
	@Override
	public Job save(Job job) {
		JobData.ExporterToJobData exporter = new JobData.ExporterToJobData();
		JobData jobData = job.export(exporter);
		JobData savedJobData = jobDataDao.save(jobData);
		return createJobFromJobData(savedJobData);
	}

}
