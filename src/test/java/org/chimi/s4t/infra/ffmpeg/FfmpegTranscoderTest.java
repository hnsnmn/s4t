package org.chimi.s4t.infra.ffmpeg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.AudioCodec;
import org.chimi.s4t.domain.job.Container;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;
import org.chimi.s4t.domain.job.VideoCodec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FfmpegTranscoderTest {

	private Transcoder transcoder;
	private File multimediaFile;
	private List<OutputFormat> outputFormats;

	@Mock
	private NamingRule namingRule;

	private OutputFormat mp4Format;
	private OutputFormat aviFormat;

	@Before
	public void setup() {
		outputFormats = new ArrayList<OutputFormat>();
		mp4Format = new OutputFormat(160, 120, 150, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(160, 120, 150, Container.AVI,
				VideoCodec.MPEG4, AudioCodec.MP3);
		when(namingRule.createName(mp4Format)).thenReturn("target/result.mp4");
		when(namingRule.createName(aviFormat)).thenReturn("target/result.avi");
		multimediaFile = new File("src/test/resources/sample.avi");

		transcoder = new FfmpegTranscoder(namingRule);
	}

	@Test
	public void transcodeWithOneMp4OutputFormat() {
		outputFormats.add(mp4Format);
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0),
				transcodedFiles.get(0));
	}

	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0),
				transcodedFiles.get(0));
	}
}
