package org.chimi.s4t.application.transcode;

import static org.mockito.Mockito.*;

import org.chimi.s4t.domain.job.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {

	@Mock
	private Job job;
	
	@Test
	public void runTranscodingWhenJobIsExists() {
		TranscodingRunner runner = new TranscodingRunner();
		runner.run();
		
		//verify(job, only()).transcode(transcoder, thumbnailExtractor);
	}
}
