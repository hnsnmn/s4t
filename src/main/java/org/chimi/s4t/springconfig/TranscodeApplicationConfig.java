package org.chimi.s4t.springconfig;

import org.chimi.s4t.application.transcode.AddJobService;
import org.chimi.s4t.application.transcode.AddJobServiceImpl;
import org.chimi.s4t.application.transcode.JobQueue;
import org.chimi.s4t.application.transcode.TranscodingService;
import org.chimi.s4t.application.transcode.TranscodingServiceImpl;
import org.chimi.s4t.application.transcode.jobqueue.MemoryJobQueue;
import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.Transcoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranscodeApplicationConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private Transcoder transcoder;

	private ThumbnailExtractor thumbnailExtractor;

	@Bean
	public AddJobService addJobService() {
		return new AddJobServiceImpl(mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory,
				jobRepository, jobQueue());
	}

	@Bean
	public TranscodingService transcodingService() {
		return new TranscodingServiceImpl(transcoder, thumbnailExtractor,
				jobRepository);
	}

	public JobQueue jobQueue() {
		return new MemoryJobQueue();
	}
}
