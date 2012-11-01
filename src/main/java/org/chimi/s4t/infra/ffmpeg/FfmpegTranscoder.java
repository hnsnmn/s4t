package org.chimi.s4t.infra.ffmpeg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class FfmpegTranscoder implements Transcoder {

	@Override
	public List<File> transcode(File multimediaFile,
			List<OutputFormat> outputFormats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : outputFormats) {
			results.add(transcode(multimediaFile, format));
		}
		return results;
	}

	private File transcode(File sourceFile, OutputFormat format) {
		IMediaReader reader = ToolFactory.makeReader(sourceFile
				.getAbsolutePath());

		String outputFile = "outputFile.mp4";
		VideoConverter converter = new VideoConverter(outputFile, reader,
				format);
		reader.addListener(converter);
		while (reader.readPacket() == null)
			do {
			} while (false);
		return new File(outputFile);
	}

}
