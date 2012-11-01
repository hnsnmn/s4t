package org.chimi.s4t.infra.ffmpeg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.chimi.s4t.domain.job.AudioCodec;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;
import org.chimi.s4t.domain.job.VideoCodec;
import org.junit.Before;
import org.junit.Test;

import com.xuggle.xuggler.Converter;

public class FfmpegTranscoderTest {

	private Transcoder transcoder;

	@Before
	public void setup() {
		transcoder = new FfmpegTranscoder();
	}

	@Test
	public void transcodeWithOnfOutputFormat() {
		Converter converter = new Converter();
		CommandLine arg0 = null;
		converter.run(arg0);
		File multimediaFile = new File("src/test/resources/sample.avi");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(160, 120, 150, VideoCodec.H264,
				AudioCodec.AAC));
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0),
				transcodedFiles.get(0));
	}
}
