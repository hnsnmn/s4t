package org.chimi.s4t.application.transcode;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddJobServiceImplTest {

	@Mock
	private JobRepository jobRepository;
	@Mock
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Mock
	private MediaSourceFile mockMediaSourceFile;

	@Mock
	private DestinationStorageFactory destinationStorageFactory;
	@Mock
	private DestinationStorage mockDestinationStorage;

	@Mock
	private ResultCallbackFactory resultCallbackFactory;
	@Mock
	private ResultCallback mockResultCallback;

	@Mock
	private JobQueue jobQueue;

	@Test
	public void addJob() {
		AddJobRequest request = new AddJobRequest();
		when(mediaSourceFileFactory.create(request.getMediaSource()))
				.thenReturn(mockMediaSourceFile);
		when(destinationStorageFactory.create(request.getDestinationStorage()))
				.thenReturn(mockDestinationStorage);
		when(resultCallbackFactory.create(request.getResultCallback()))
				.thenReturn(mockResultCallback);

		final Long mockJobId = new Long(1);
		Job mockSavedJob = mock(Job.class);
		when(mockSavedJob.getId()).thenReturn(mockJobId);
		when(jobRepository.save(any(Job.class))).thenReturn(mockSavedJob);

		AddJobService addJobService = new AddJobServiceImpl(
				mediaSourceFileFactory, destinationStorageFactory,
				resultCallbackFactory, jobRepository, jobQueue);
		Long jobId = addJobService.addJob(request);

		assertNotNull(jobId);
		verify(jobRepository, only()).save(any(Job.class));
		verify(mediaSourceFileFactory, only()).create(request.getMediaSource());
		verify(destinationStorageFactory, only()).create(
				request.getDestinationStorage());
		verify(resultCallbackFactory, only()).create(
				request.getResultCallback());
		verify(jobQueue, only()).add(mockJobId);
	}
}
