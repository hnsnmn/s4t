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

import org.chimi.s4t.domain.job.CreatedFileSaver;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.Job.State;
import org.chimi.s4t.domain.job.JobRepository;
import org.chimi.s4t.domain.job.JobResultNotifier;
import org.chimi.s4t.domain.job.MediaSourceFile;
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

	private Job mockJob;

	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private CreatedFileSaver createdFileSender;
	@Mock
	private JobResultNotifier jobResultNotifier;
	@Mock
	private JobRepository jobRepository;

	private TranscodingService transcodingService;

	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumbnails = new ArrayList<File>();
	private RuntimeException mockException = new RuntimeException();

	@Before
	public void setup() {
		mockJob = new Job(jobId, mediaSourceFile);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);

		transcodingService = new TranscodingServiceImpl(transcoder,
				thumbnailExtractor, createdFileSender, jobResultNotifier,
				jobRepository);

		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(
				mockMultimediaFiles);
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
		assertNull(job.getOccurredException());

		VerifyOption verifyOption = new VerifyOption();
		verifyCollaboration(verifyOption);
	}

	private void verifyCollaboration(VerifyOption verifyOption) {
		if (verifyOption.transcoderNever)
			verify(transcoder, never()).transcode(any(File.class), anyLong());
		else
			verify(transcoder, only()).transcode(mockMultimediaFile, jobId);

		if (verifyOption.thumbnailExtractorNever)
			verify(thumbnailExtractor, never()).extract(any(File.class),
					anyLong());
		else
			verify(thumbnailExtractor, only()).extract(mockMultimediaFile,
					jobId);

		if (verifyOption.createdFileSenderNever)
			verify(createdFileSender, never()).store(anyListOf(File.class),
					anyListOf(File.class), anyLong());
		else
			verify(createdFileSender, only()).store(mockMultimediaFiles,
					mockThumbnails, jobId);

		if (verifyOption.jobResultNotifierNever)
			verify(jobResultNotifier, never()).notifyToRequester(jobId);
		else
			verify(jobResultNotifier, only()).notifyToRequester(jobId);
	}

	private void assertJobIsWaitingState() {
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceFile() {
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		executeFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = true;
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createdFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;

		verifyCollaboration(verifyOption);
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
		assertNotNull(job.getOccurredException());
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenThrow(
				mockException);

		executeFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createdFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;

		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumbnailExtractor() {
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenThrow(
				mockException);

		executeFailingTranscodeAndAssertFail(Job.State.EXTRACTINGTHUMBNAIL);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.createdFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;

		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtCreatedFileSender() {
		doThrow(mockException).when(createdFileSender).store(
				mockMultimediaFiles, mockThumbnails, jobId);

		executeFailingTranscodeAndAssertFail(Job.State.STORING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.jobResultNotifierNever = true;

		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
		doThrow(mockException).when(jobResultNotifier).notifyToRequester(jobId);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.NOTIFYING);

		VerifyOption verifyOption = new VerifyOption();

		verifyCollaboration(verifyOption);
	}

	public class VerifyOption {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean createdFileSenderNever;
		public boolean jobResultNotifierNever;
	}
}
