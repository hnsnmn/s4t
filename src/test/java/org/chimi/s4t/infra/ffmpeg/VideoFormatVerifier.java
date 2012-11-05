package org.chimi.s4t.infra.ffmpeg;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.chimi.s4t.domain.job.OutputFormat;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoFormatVerifier {

	public static void verifyVideoFormat(OutputFormat expectedFormat,
			File videoFile) {
		new VideoFormatVerifier(expectedFormat, videoFile).verify();
	}

	private IContainer container;
	private int width;
	private int height;
	private ICodec.ID videoCodec;
	private ICodec.ID audioCodec;

	private OutputFormat expectedFormat;
	private File videoFile;

	public VideoFormatVerifier(OutputFormat expectedFormat, File videoFile) {
		this.expectedFormat = expectedFormat;
		this.videoFile = videoFile;
	}

	public void verify() {
		try {
			assertExtension();
			makeContainer();
			extractMetaInfoOfVideo();
			assertVideoFile();
		} finally {
			closeContainer();
		}
	}

	private void assertExtension() {
		assertEquals(expectedFormat.getFileExtension(), fileExtenstion());
	}

	private String fileExtenstion() {
		String filePath = videoFile.getAbsolutePath();
		int lastDotIdx = filePath.lastIndexOf(".");
		String extension = filePath.substring(lastDotIdx + 1);
		return extension;
	}

	private void makeContainer() {
		container = IContainer.make();
		int openResult = container.open(videoFile.getAbsolutePath(),
				IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed: "
					+ openResult);
		}
	}

	private void extractMetaInfoOfVideo() {
		int numStreams = container.getNumStreams();
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
	}

	private void closeContainer() {
		if (container != null)
			container.close();
	}

	private void assertVideoFile() {
		assertEquals(expectedFormat.getWidth(), width);
		assertEquals(expectedFormat.getHeight(), height);
		assertEquals(expectedFormat.getVideoCodec(),
				CodecValueConverter.toDomainVideoCodec(videoCodec));
		assertEquals(expectedFormat.getAudioCodec(),
				CodecValueConverter.toDomainAudioCodec(audioCodec));
	}

}