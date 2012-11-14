package org.chimi.s4t.infra.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaJobRepository implements JobRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private ResultCallbackFactory resultCallbackFactory;

	public JpaJobRepository(MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory,
			ResultCallbackFactory resultCallbackFactory) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Transactional
	@Override
	public Job findById(Long jobId) {
		JobData jobData = entityManager.find(JobData.class, jobId);
		if (jobData == null) {
			return null;
		}
		return createJobFromJobData(jobData);
	}

	@Transactional
	@Override
	public Job save(Job job) {
		JobData.ExporterToJobData exporter = new JobData.ExporterToJobData();
		job.export(exporter);
		JobData jobData = exporter.getJobData();
		entityManager.persist(jobData);
		return createJobFromJobData(jobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return new JobImpl(jobData, mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory);
	}

}
