package org.chimi.s4t.domain.transcode;

import java.io.File;
import java.util.List;

public class TranscodingServiceImpl implements TranscodingService {
	private MediaSourceCopier mediaSourceCopier;
	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;
	private CreatedFileSaver createdFileSaver;
	private JobResultNotifier jobResultNotifier;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier,
			Transcoder transcoder, ThumbnailExtractor thumbnailExtractor,
			CreatedFileSaver createdFileSaver,
			JobResultNotifier jobResultNotifier) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSaver = createdFileSaver;
		this.jobResultNotifier = jobResultNotifier;
	}

	@Override
	public void transcode(Long jobId) {
		File multimediaFile = copyMultimediaSourceToLocal(jobId);
		List<File> multimediaFiles = transcode(multimediaFile, jobId);
		List<File> thumbnails = extractThubmail(multimediaFile, jobId);
		storeCreatedFilesToStorage(multimediaFiles, thumbnails, jobId);
		notifyJobResultToRequester(jobId);
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		return mediaSourceCopier.copy(jobId);
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		return transcoder.transcode(multimediaFile, jobId);
	}

	private List<File> extractThubmail(File multimediaFile, Long jobId) {
		return thumbnailExtractor.extract(multimediaFile, jobId);
	}

	private void storeCreatedFilesToStorage(List<File> multimediaFiles,
			List<File> thumbnails, Long jobId) {
		createdFileSaver.store(multimediaFiles, thumbnails, jobId);
	}

	private void notifyJobResultToRequester(Long jobId) {
		jobResultNotifier.notifyToRequester(jobId);
	}

}