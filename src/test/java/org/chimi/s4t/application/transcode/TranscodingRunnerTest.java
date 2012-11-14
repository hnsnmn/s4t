package org.chimi.s4t.application.transcode;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.chimi.s4t.domain.job.Job;
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
	private JobQueue jobQueue;

	private TranscodingRunner runner;

	@Before
	public void setup() {
		runner = new TranscodingRunner(transcodingService, jobQueue);
	}

	@Test
	public void runTranscodingWhenJobQueueIsNotEmpty() {
		when(job.getId()).thenReturn(1L);
		when(jobQueue.nextJobId()).thenReturn(1L).thenThrow(
				new JobQueue.ClosedException());

		runner.run();

		verify(transcodingService, only()).transcode(1L);
	}

}
