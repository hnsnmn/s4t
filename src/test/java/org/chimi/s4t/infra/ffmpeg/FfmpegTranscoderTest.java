package org.chimi.s4t.infra.ffmpeg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class FfmpegTranscoderTest {

	private Transcoder transcoder;
	private File multimediaFile;
	private List<OutputFormat> outputFormats;

	private OutputFormat mp4Format;
	private OutputFormat mp4Format2;
	private OutputFormat aviFormat;

	@Before
	public void setup() {
		outputFormats = new ArrayList<OutputFormat>();
		mp4Format = new OutputFormat(160, 120, 150, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC);
		mp4Format2 = new OutputFormat(80, 60, 80, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(160, 120, 150, Container.AVI,
				VideoCodec.MPEG4, AudioCodec.MP3);
		multimediaFile = new File("src/test/resources/sample.avi");

		transcoder = new FfmpegTranscoder(NamingRule.DEFAULT);
	}

	@Test
	public void transcodeWithOneMp4OutputFormat() {
		outputFormats.add(mp4Format);
		executeTranscoderAndAssert();
	}

	private void executeTranscoderAndAssert() {
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(outputFormats.size(), transcodedFiles.size());
		for (int i = 0; i < outputFormats.size(); i++) {
			assertTrue(transcodedFiles.get(i).exists());
			VideoFormatVerifier.verifyVideoFormat(outputFormats.get(i),
					transcodedFiles.get(i));
		}
	}

	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		executeTranscoderAndAssert();
	}

	@Test
	public void transcodeWithTwoMp4OutputFormats() {
		outputFormats.add(mp4Format);
		outputFormats.add(mp4Format2);
		executeTranscoderAndAssert();
	}
}
