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

	private Job mockJob = new Job();

	private TranscodingService transcodingService;

	@Before
	public void setup() {
		transcodingService = new TranscodingServiceImpl(mediaSourceCopier,
				transcoder, thumbnailExtractor, createdFileSender,
				jobResultNotifier, jobStateChanger, transcodingExceptionHandler);

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
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(
				mockMultimediaFiles);

		List<File> mockThumbnails = new ArrayList<File>();
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(
				mockThumbnails);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		transcodingService.transcode(jobId);

		job = jobRepository.findById(jobId);
		assertTrue(job.isFinished());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).store(mockMultimediaFiles,
				mockThumbnails, jobId);
		verify(jobResultNotifier, only()).notifyToRequester(jobId);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		RuntimeException mockException = new RuntimeException();
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);

		try {
			transcodingService.transcode(jobId);
			fail("발생해야 함");
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);

		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, never()).transcode(any(File.class), anyLong());
		verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).store(anyListOf(File.class),
				anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(jobId);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtTranscoder() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		RuntimeException mockException = new RuntimeException();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenThrow(
				mockException);

		try {
			transcodingService.transcode(jobId);
			fail("발생해야 함");
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);

		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.TRANSCODING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).store(anyListOf(File.class),
				anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(jobId);
	}
}
