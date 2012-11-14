package org.chimi.s4t.infra.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.Container;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.chimi.s4t.springconfig.ApplicationContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
public class JpaJobRepositoryIntTest {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;

	@Test
	public void save() {
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(60, 40, 150, Container.MP4));

		Job job = new Job(mediaSourceFileFactory.create("file://./video.avi"),
				destinationStorageFactory.create("file://./target"),
				outputFormats, resultCallbackFactory.create("http://"));
		Job savedJob = jobRepository.save(job);
		assertNotNull(savedJob);
		assertNotNull(savedJob.getId());
		assertValue(job, savedJob);
	}

	private void assertValue(Job job, Job savedJob) {
		assertEquals(job.getOutputFormats().size(), savedJob.getOutputFormats()
				.size());
	}

	@Test
	public void findById() {
		Job job = jobRepository.findById(1L);
		assertTrue(job.isWaiting());
		assertEquals(2, job.getOutputFormats().size());
	}
}
