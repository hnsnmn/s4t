package org.chimi.s4t.domain.job;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobTest {

	@Mock
	private MediaSourceFile mediaSource;
	@Mock
	private DestinationStorage destination;
	@Mock
	private List<OutputFormat> outputFormats;
	@Mock
	private ResultCallback callback;

	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;

	@Mock
	private List<File> multimediaFiles;
	@Mock
	private List<File> thumbnails;
	@Mock
	private File sourceFile;

	@Test
	public void successfully() {
		long jobId = 1L;

		when(mediaSource.getSourceFile()).thenReturn(sourceFile);
		when(transcoder.transcode(sourceFile, outputFormats)).thenReturn(
				multimediaFiles);
		when(thumbnailExtractor.extract(sourceFile, jobId)).thenReturn(
				thumbnails);

		Job job = new Job(jobId, mediaSource, destination, outputFormats,
				callback);
		job.transcode(transcoder, thumbnailExtractor);

		verify(mediaSource, only()).getSourceFile();
		verify(destination, only()).save(multimediaFiles, thumbnails);
		verify(callback, only()).nofiySuccessResult(jobId);
	}

	@Test
	public void failGetSourceFile() {
		long jobId = 1L;

		RuntimeException exception = new RuntimeException("exception");
		when(mediaSource.getSourceFile()).thenThrow(exception);

		Job job = new Job(jobId, mediaSource, destination, outputFormats,
				callback);
		try {
			job.transcode(transcoder, thumbnailExtractor);
			fail("발생해야 함");
		} catch (Exception ex) {
		}

		verify(mediaSource, only()).getSourceFile();
		verify(destination, never()).save(multimediaFiles, thumbnails);
		verify(callback, only()).nofiyFailedResult(jobId,
				Job.State.MEDIASOURCECOPYING, "exception");
	}
}
