package org.chimi.s4t.domain.job;

public class OutputFormat {

	private int width;
	private int height;
	private int bitrate;
	private String videoCodec;
	private String audioCodec;

	public OutputFormat(int width, int height, int bitrate, String videoCodec,
			String audioCodec) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
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

	public String getVideoCodec() {
		return videoCodec;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

}
