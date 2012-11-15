package org.chimi.s4t.infra.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.Container;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.callback.HttpResultCallback;
import org.chimi.s4t.domain.job.destination.FileDestinationStorage;
import org.chimi.s4t.domain.job.mediasource.LocalStorageMediaSourceFile;
import org.chimi.s4t.springconfig.ApplicationContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
public class DbJobRepositoryIntTest {

	@Autowired
	private JobRepository jobRepository;

	@Test
	public void findById() {
		Job job = jobRepository.findById(1L);
		assertNotNull(job);
		assertTrue(job.isWaiting());
		assertEquals(2, job.getOutputFormats().size());
	}

	@Test
	public void save() {
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(60, 40, 150, Container.MP4));

		Job job = new Job(
				new LocalStorageMediaSourceFile("file://./video.avi"),
				new FileDestinationStorage("file://./target"), outputFormats,
				new HttpResultCallback("http://"));
		Job savedJob = jobRepository.save(job);
		assertNotNull(savedJob);
		assertNotNull(savedJob.getId());
		assertJobsEquals(job, savedJob);
	}

	private void assertJobsEquals(Job job, Job savedJob) {
		assertEquals(job.getOutputFormats().size(), savedJob.getOutputFormats()
				.size());
	}
}
