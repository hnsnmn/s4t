package org.chimi.s4t.infra.persistence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.Transcoder;
import org.chimi.s4t.springconfig.ApplicationContextConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
public class JobIntTest {

	@Autowired
	private JobRepository jobRepository;

	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;

	@Before
	public void setup() {
		transcoder = mock(Transcoder.class);
		thumbnailExtractor = mock(ThumbnailExtractor.class);
	}

	@Test
	public void jobShouldChangeStateInDB() {
		RuntimeException trancoderException = new RuntimeException("강제발생");
		when(
				transcoder.transcode(any(File.class),
						anyListOf(OutputFormat.class))).thenThrow(
				trancoderException);

		Long jobId = new Long(1);
		Job job = jobRepository.findById(jobId);
		try {
			job.transcode(transcoder, thumbnailExtractor);
		} catch (RuntimeException ex) {
		}

		Job updatedJob = jobRepository.findById(jobId);

		assertEquals(Job.State.TRANSCODING, job.getLastState());
		assertEquals(Job.State.TRANSCODING, updatedJob.getLastState());
		assertEquals("강제발생", job.getExceptionMessage());
		assertEquals(job.getExceptionMessage(), updatedJob.getExceptionMessage());
	}
}
