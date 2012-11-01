package org.chimi.s4t.infra.ffmpeg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;
import org.junit.Before;
import org.junit.Test;

import com.xuggle.xuggler.Converter;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

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
		outputFormats.add(new OutputFormat(160, 120, 150, "h264", "aac"));
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		verifyTranscodedFile(outputFormats.get(0), transcodedFiles.get(0));
	}

	private void verifyTranscodedFile(OutputFormat outputFormat, File file) {
		IContainer container = IContainer.make();
		int openResult = container.open(file.getAbsolutePath(),
				IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed: "
					+ openResult);
		}
		int numStreams = container.getNumStreams();

		int width = 0;
		int height = 0;
		ICodec.ID videoCodec = null;
		ICodec.ID audioCodec = null;

		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				height = coder.getHeight();
			}
		}
		container.close();

		assertEquals(outputFormat.getWidth(), width);
		assertEquals(outputFormat.getHeight(), height);
		assertEquals(outputFormat.getVideoCodec(), videoCodec.toString());
		assertEquals(outputFormat.getAudioCodec(), audioCodec.toString());
	}
}
