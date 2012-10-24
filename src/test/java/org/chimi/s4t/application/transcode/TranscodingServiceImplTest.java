package org.chimi.s4t.application.transcode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.application.transcode.CreatedFileSaver;
import org.chimi.s4t.application.transcode.JobResultNotifier;
import org.chimi.s4t.application.transcode.JobStateChanger;
import org.chimi.s4t.application.transcode.MediaSourceCopier;
import org.chimi.s4t.application.transcode.ThumbnailExtractor;
import org.chimi.s4t.application.transcode.Transcoder;
import org.chimi.s4t.application.transcode.TranscodingExceptionHandler;
import org.chimi.s4t.application.transcode.TranscodingService;
import org.chimi.s4t.application.transcode.TranscodingServiceImpl;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.Job.State;
import org.chimi.s4t.domain.job.JobRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingServiceImplTest {

	private Long jobId = new Long(1);
	private Job mockJob = new Job();

	@Mock
	private MediaSourceCopier mediaSourceCopier;
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
	@Mock
	private JobStateChanger jobStateChanger;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;

	private TranscodingService transcodingService;

	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumbnails = new ArrayList<File>();
	private RuntimeException mockException = new RuntimeException();

	@Before
	public void setup() {
		transcodingService = new TranscodingServiceImpl(mediaSourceCopier,
				transcoder, thumbnailExtractor, createdFileSender,
				jobResultNotifier, jobStateChanger, transcodingExceptionHandler);

		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(
				mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(
				mockThumbnails);

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Job.State newState = (State) invocation.getArguments()[1];
				mockJob.changeState(newState);
				return null;
			}
		}).when(jobStateChanger).chageJobState(anyLong(), any(Job.State.class));

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				RuntimeException ex = (RuntimeException) invocation
						.getArguments()[1];
				mockJob.exceptionOccurred(ex);
				return null;
			}
		}).when(transcodingExceptionHandler).notifyToJob(anyLong(),
				any(RuntimeException.class));
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
		verify(mediaSourceCopier, only()).copy(jobId);

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
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);

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
