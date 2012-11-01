package org.chimi.s4t.infra.ffmpeg;

import org.chimi.s4t.domain.job.AudioCodec;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.VideoCodec;
import org.junit.Test;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class VideoConverterTest {

	@Test
	public void transcode() {
		IMediaReader reader = ToolFactory
				.makeReader("src/test/resources/sample.avi");

		OutputFormat outputFormat = new OutputFormat(160, 120, 150,
				VideoCodec.H264, AudioCodec.AAC);
		VideoConverter writer = new VideoConverter("target/sample.mp4", reader,
				outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null)
			do {
			} while (false);
	}
}
