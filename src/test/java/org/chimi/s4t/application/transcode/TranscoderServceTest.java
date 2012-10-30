package org.chimi.s4t.application.transcode;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.Transcoder;
import org.junit.Test;

public class TranscoderServceTest {

	private Transcoder transcoder;

	@Test
	public void transcodeWithOnfOutputFormat() {
		File multimediaFile = new File(".");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(640, 480, 300, "h264", "aac"));
		List<File> transcodedFiles = transcoder.transcode(multimediaFile,
				outputFormats);
		assertEquals(1, transcodedFiles.size());
	}
}