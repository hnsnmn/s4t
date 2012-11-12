package org.chimi.s4t.application.transcode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.Job.State;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.Transcoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingServiceImplTest {

	private Long jobId = new Long(1);
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private DestinationStorage destinationStorage;
	@Mock
	private ResultCallback callback;

	private List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
	private Job mockJob;

	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private JobRepository jobRepository;

	private TranscodingService transcodingService;

	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumbnails = new ArrayList<File>();
	private RuntimeException mockException = new RuntimeException();

	@Before
	public void setup() {
		mockJob = new Job(jobId, mediaSourceFile, destinationStorage,
				outputFormats, callback);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);

		transcodingService = new TranscodingServiceImpl(transcoder,
				thumbnailExtractor, jobRepository);

		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(transcoder.transcode(mockMultimediaFile, outputFormats))
				.thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(
				mockThumbnails);
	}

	@Test
	public void transcodeSuccessfully() {
		assertJobIsWaitingState();

		transcodingService.transcode(jobId);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinished());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertFalse(job.isExceptionOccurred());
		assertNull(job.getExceptionMessage());

		CollaborationVerifier colVerifier = new CollaborationVerifier();
		colVerifier.verifyCollaboration();
	}

	private void assertJobIsWaitingState() {
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceFile() {
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		executeFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		CollaborationVerifier colVerifier = new CollaborationVerifier();
		colVerifier.transcoderNever = true;
		colVerifier.thumbnailExtractorNever = true;
		colVerifier.destinationStorageNever = true;

		colVerifier.verifyCollaboration();
	}

	private void executeFailingTranscodeAndAssertFail(State expectedLastState) {
		try {
			transcodingService.transcode(jobId);
			fail("발생해야 함");
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);

		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		assertEquals(expectedLastState, job.getLastState());
		assertTrue(job.isExceptionOccurred());
		assertNotNull(job.getExceptionMessage());
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(mockMultimediaFile, outputFormats))
				.thenThrow(mockException);

		executeFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		CollaborationVerifier colVerifier = new CollaborationVerifier();
		colVerifier.thumbnailExtractorNever = true;
		colVerifier.destinationStorageNever = true;

		colVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumbnailExtractor() {
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenThrow(
				mockException);

		executeFailingTranscodeAndAssertFail(Job.State.EXTRACTINGTHUMBNAIL);

		CollaborationVerifier colVerifier = new CollaborationVerifier();
		colVerifier.destinationStorageNever = true;

		colVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtDestinationStorage() {
		doThrow(mockException).when(destinationStorage).save(
				mockMultimediaFiles, mockThumbnails);

		executeFailingTranscodeAndAssertFail(Job.State.STORING);

		CollaborationVerifier colVerifier = new CollaborationVerifier();

		colVerifier.verifyCollaboration();
	}

	private class CollaborationVerifier {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean destinationStorageNever;

		public void verifyCollaboration() {
			if (this.transcoderNever)
				verify(transcoder, never()).transcode(any(File.class),
						anyListOf(OutputFormat.class));
			else
				verify(transcoder, only()).transcode(mockMultimediaFile,
						outputFormats);

			if (this.thumbnailExtractorNever)
				verify(thumbnailExtractor, never()).extract(any(File.class),
						anyLong());
			else
				verify(thumbnailExtractor, only()).extract(mockMultimediaFile,
						jobId);

			if (this.destinationStorageNever)
				verify(destinationStorage, never()).save(anyListOf(File.class),
						anyListOf(File.class));
			else
				verify(destinationStorage, only()).save(mockMultimediaFiles,
						mockThumbnails);
		}

	}
}
