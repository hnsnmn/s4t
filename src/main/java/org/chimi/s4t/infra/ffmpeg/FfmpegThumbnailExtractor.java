package org.chimi.s4t.infra.ffmpeg;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.chimi.s4t.domain.job.ThumbnailExtractor;
import org.chimi.s4t.domain.job.ThumbnailPolicy;

public class FfmpegThumbnailExtractor implements ThumbnailExtractor {

	@Override
	public List<File> extract(File multimediaFile,
			ThumbnailPolicy thumbnailPolicy) {
		// 썸네일 크기, 시작시간, 끝시간, 간격
		return Collections.emptyList();
	}

}
