package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

import org.chimi.s4t.application.transcode.CreatedFileSaver;
import org.chimi.s4t.application.transcode.JobResultNotifier;
import org.chimi.s4t.application.transcode.MediaSourceCopier;
import org.chimi.s4t.application.transcode.ThumbnailExtractor;
import org.chimi.s4t.application.transcode.Transcoder;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING, COMPLETED
	}

	private Long id;
	private State state;
	private Exception occurredException;
	
	public Job(Long id) {
		this.id = id;
	}

	public void changeState(State newState) {
		this.state = newState;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public State getLastState() {
		return state;
	}

	public Exception getOccurredException() {
		return occurredException;
	}

	public void exceptionOccurred(RuntimeException ex) {
		occurredException = ex;
	}

	public void transcode(MediaSourceCopier mediaSourceCopier,
			Transcoder transcoder, ThumbnailExtractor thumbnailExtractor,
			CreatedFileSaver createdFileSaver,
			JobResultNotifier jobResultNotifier) {
		try {
			changeState(Job.State.MEDIASOURCECOPYING);
			File multimediaFile = copyMultimediaSourceToLocal(mediaSourceCopier);
			changeState(Job.State.TRANSCODING);
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);
			changeState(Job.State.EXTRACTINGTHUMBNAIL);
			List<File> thumbnails = extractThumbnail(multimediaFile,
					thumbnailExtractor);
			changeState(Job.State.STORING);
			storeCreatedFilesToStorage(multimediaFiles, thumbnails,
					createdFileSaver);
			changeState(Job.State.NOTIFYING);
			notifyJobResultToRequester(jobResultNotifier);
			changeState(Job.State.COMPLETED);
		} catch (RuntimeException ex) {
			exceptionOccurred(ex);
			throw ex;
		}
	}

	private File copyMultimediaSourceToLocal(MediaSourceCopier mediaSourceCopier) {
		return mediaSourceCopier.copy(id);
	}

	private List<File> transcode(File multimediaFile, Transcoder transcoder) {
		return transcoder.transcode(multimediaFile, id);
	}

	private List<File> extractThumbnail(File multimediaFile,
			ThumbnailExtractor thumbnailExtractor) {
		return thumbnailExtractor.extract(multimediaFile, id);
	}

	private void storeCreatedFilesToStorage(List<File> multimediaFiles,
			List<File> thumbnails, CreatedFileSaver createdFileSaver) {
		createdFileSaver.store(multimediaFiles, thumbnails, id);
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		jobResultNotifier.notifyToRequester(id);
	}

}
