package org.chimi.s4t.infra.ffmpeg;

import org.chimi.s4t.domain.job.OutputFormat;
import org.junit.Test;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoConverterTest {

	@Test
	public void transcode() {
		IMediaReader reader = ToolFactory
				.makeReader("src/test/resources/sample.avi");

		OutputFormat outputFormat = new OutputFormat(160, 120, 150, "h264",
				"aac");
		VideoConverter writer = new VideoConverter("target/sample.mp4", reader,
				outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null)
			do {
			} while (false);
	}
}
