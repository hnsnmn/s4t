package org.chimi.s4t.domain.job;

public class OutputFormat {

	private int width;
	private int height;
	private int bitrate;
	private Container container;
	private VideoCodec videoCodec;
	private AudioCodec audioCodec;

	public OutputFormat(int width, int height, int bitrate,
			Container container, VideoCodec videoCodec, AudioCodec audioCodec) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.container = container;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getFileExtension() {
		return container.getFileExtension();
	}

	public VideoCodec getVideoCodec() {
		return videoCodec;
	}

	public AudioCodec getAudioCodec() {
		return audioCodec;
	}

}
