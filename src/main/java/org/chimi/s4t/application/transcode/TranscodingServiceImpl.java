package org.chimi.s4t.application.transcode;

import java.io.File;
import java.util.List;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.Job.State;

public class TranscodingServiceImpl implements TranscodingService {
	private MediaSourceCopier mediaSourceCopier;
	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;
	private CreatedFileSaver createdFileSaver;
	private JobResultNotifier jobResultNotifier;
	private JobStateChanger jobStateChanger;
	private TranscodingExceptionHandler transcodingExceptionHandler;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier,
			Transcoder transcoder, ThumbnailExtractor thumbnailExtractor,
			CreatedFileSaver createdFileSaver,
			JobResultNotifier jobResultNotifier,
			JobStateChanger jobStateChanger,
			TranscodingExceptionHandler transcodingExceptionHandler) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSaver = createdFileSaver;
		this.jobResultNotifier = jobResultNotifier;
		this.jobStateChanger = jobStateChanger;
		this.transcodingExceptionHandler = transcodingExceptionHandler;
	}

	@Override
	public void transcode(Long jobId) {
		changeJobState(jobId, Job.State.MEDIASOURCECOPYING);
		File multimediaFile = copyMultimediaSourceToLocal(jobId);
		changeJobState(jobId, Job.State.TRANSCODING);
		List<File> multimediaFiles = transcode(multimediaFile, jobId);
		changeJobState(jobId, Job.State.EXTRACTINGTHUMBNAIL);
		List<File> thumbnails = extractThumbnail(multimediaFile, jobId);
		changeJobState(jobId, Job.State.STORING);
		storeCreatedFilesToStorage(multimediaFiles, thumbnails, jobId);
		changeJobState(jobId, Job.State.NOTIFYING);
		notifyJobResultToRequester(jobId);
		changeJobState(jobId, Job.State.COMPLETED);
	}

	private void changeJobState(Long jobId, State newJobState) {
		jobStateChanger.chageJobState(jobId, newJobState);
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		try {
			return mediaSourceCopier.copy(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		try {
			return transcoder.transcode(multimediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
		try {
			return thumbnailExtractor.extract(multimediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void storeCreatedFilesToStorage(List<File> multimediaFiles,
			List<File> thumbnails, Long jobId) {
		try {
			createdFileSaver.store(multimediaFiles, thumbnails, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void notifyJobResultToRequester(Long jobId) {
		try {
			jobResultNotifier.notifyToRequester(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

}