package org.chimi.s4t.application.transcode;

import static org.mockito.Mockito.*;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {

	@Mock
	private Job job;
	@Mock
	private TranscodingService transcodingService;
	@Mock
	private JobRepository jobRepository;

	private TranscodingRunner runner;

	@Before
	public void setup() {
		runner = new TranscodingRunner(transcodingService, jobRepository);
	}

	@Test
	public void runTranscodingSuccessfullyWhenJobIsExists() {
		when(job.getId()).thenReturn(1L);
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(job);

		runner.run();

		verify(transcodingService, only()).transcode(1L);
	}

	@Test
	public void dontRunTranscodingWhenJobIsNotExists() {
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(null);

		runner.run();

		verify(transcodingService, never()).transcode(anyLong());
	}

}
