package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Job {

	public static enum State {
		WAITING, MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING, COMPLETED
	}

	private Long id;
	private State state;

	private MediaSourceFile mediaSourceFile;
	private DestinationStorage destinationStorage;
	private List<OutputFormat> outputFormats;
	private ResultCallback callback;

	private String exceptionMessage;

	public Job(MediaSourceFile mediaSourceFile,
			DestinationStorage destinationStorage,
			List<OutputFormat> outputFormats, ResultCallback callback) {
		this(null, mediaSourceFile, destinationStorage, outputFormats, callback);
	}

	public Job(Long id, MediaSourceFile mediaSourceFile,
			DestinationStorage destinationStorage,
			List<OutputFormat> outputFormats, ResultCallback callback) {
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
		this.outputFormats = outputFormats;
		this.callback = callback;
		this.state = State.WAITING;
	}

	public Long getId() {
		return id;
	}

	public List<OutputFormat> getOutputFormats() {
		return Collections.unmodifiableList(outputFormats);
	}

	public boolean isWaiting() {
		return state == State.WAITING;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public boolean isExceptionOccurred() {
		return exceptionMessage != null;
	}

	public State getLastState() {
		return state;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void transcode(Transcoder transcoder,
			ThumbnailExtractor thumbnailExtractor) {
		try {
			File multimediaFile = copyMultimediaSourceToLocal();
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);
			List<File> thumbnails = extractThumbnail(multimediaFile,
					thumbnailExtractor);
			storeCreatedFilesToStorage(multimediaFiles, thumbnails);
			notifyJobResultToRequester();
			completed();
		} catch (RuntimeException ex) {
			exceptionOccurred(ex);
			throw ex;
		}
	}

	protected void changeState(State newState) {
		this.state = newState;
	}

	private void exceptionOccurred(RuntimeException ex) {
		exceptionMessage = ex.getMessage();
		callback.nofiyFailedResult(id, state, exceptionMessage);
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

	private void notifyJobResultToRequester() {
		changeState(Job.State.NOTIFYING);
		callback.nofiySuccessResult(id);
	}

	private void completed() {
		changeState(Job.State.COMPLETED);
	}

	public Exporter export(Exporter exporter) {
		exporter.addId(id);
		exporter.addState(state);
		exporter.addMediaSource(mediaSourceFile.getUrl());
		exporter.addDestinationStorage(destinationStorage.getUrl());
		exporter.addResultCallback(callback.getUrl());
		exporter.addOutputFormat(getOutputFormats());
		exporter.addExceptionMessage(exceptionMessage);
		return exporter;
	}

	public static interface Exporter {
		public void addId(Long id);

		public void addState(Job.State state);

		public void addMediaSource(String url);

		public void addDestinationStorage(String url);

		public void addResultCallback(String url);

		public void addExceptionMessage(String exceptionMessage);

		public void addOutputFormat(List<OutputFormat> outputFormat);
	}
}
