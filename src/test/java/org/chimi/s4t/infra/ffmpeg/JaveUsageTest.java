package org.chimi.s4t.infra.ffmpeg;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;

import java.io.File;

import org.junit.Test;

public class JaveUsageTest {

	@Test
	public void getInfo() throws InputFormatException, EncoderException {
		Encoder encoder = new Encoder();
		MultimediaInfo info = encoder.getInfo(new File(
				"src/test/resources/sample.mp4"));
		System.out.println(info.getFormat());
		System.out.println(info.getAudio().getDecoder());
		System.out.println(info.getVideo().getDecoder());
	}

	@Test
	public void encodeAviToMp4() throws IllegalArgumentException,
			InputFormatException, EncoderException {
		File source = new File("src/test/resources/sample.avi");
		File target = new File("target/sample.mp4");

		AudioAttributes audio = new AudioAttributes();
		// audio.setCodec("libfaac");
		// audio.setBitRate(new Integer(64000));
		// audio.setChannels(new Integer(1));
		// audio.setSamplingRate(new Integer(22050));

		VideoAttributes video = new VideoAttributes();
		// video.setCodec("libx264");
		// video.setBitRate(new Integer(160000));
		// video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(400, 300));

		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);

		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);
	}
}
