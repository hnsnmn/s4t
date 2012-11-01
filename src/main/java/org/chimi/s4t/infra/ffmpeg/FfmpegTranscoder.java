package org.chimi.s4t.infra.ffmpeg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;

public class FfmpegTranscoder implements Transcoder {

	@Override
	public List<File> transcode(File multimediaFile,
			List<OutputFormat> outputFormats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : outputFormats) {
			results.add(new File("."));
		}
		return results;
	}

}
