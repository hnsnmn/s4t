package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING, COMPLETED
	}

	private Long id;
	private MediaSourceFile mediaSourceFile;
	private DestinationStorage destinationStorage;
	private List<OutputFormat> outputFormats;
	private State state;
	private Exception occurredException;

	public Job(Long id, MediaSourceFile mediaSourceFile,
			DestinationStorage destinationStorage,
			List<OutputFormat> outputFormats) {
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
		this.outputFormats = outputFormats;
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

	public void transcode(Transcoder transcoder,
			ThumbnailExtractor thumbnailExtractor,
			JobResultNotifier jobResultNotifier) {
		try {
			File multimediaFile = copyMultimediaSourceToLocal();
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);
			List<File> thumbnails = extractThumbnail(multimediaFile,
					thumbnailExtractor);
			storeCreatedFilesToStorage(multimediaFiles, thumbnails);
			notifyJobResultToRequester(jobResultNotifier);
			completed();
		} catch (RuntimeException ex) {
			exceptionOccurred(ex);
			throw ex;
		}
	}

	private void changeState(State newState) {
		this.state = newState;
	}

	private void exceptionOccurred(RuntimeException ex) {
		occurredException = ex;
	}

	private File copyMultimediaSourceToLocal() {
		changeState(Job.State.MEDIASOURCECOPYING);
		return mediaSourceFile.getSourceFile();
	}

	private List<File> transcode(File multimediaFile, Transcoder transcoder) {
		changeState(Job.State.TRANSCODING);
		return transcoder.transcode(multimediaFile, outputFormats);
	}

	private List<File> extractThumbnail(File multimediaFile,
			ThumbnailExtractor thumbnailExtractor) {
		changeState(Job.State.EXTRACTINGTHUMBNAIL);
		return thumbnailExtractor.extract(multimediaFile, id);
	}

	private void storeCreatedFilesToStorage(List<File> multimediaFiles,
			List<File> thumbnails) {
		changeState(Job.State.STORING);
		destinationStorage.save(multimediaFiles, thumbnails);
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		changeState(Job.State.NOTIFYING);
		jobResultNotifier.notifyToRequester(id);
	}

	private void completed() {
		changeState(Job.State.COMPLETED);
	}

}
