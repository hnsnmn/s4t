package org.chimi.s4t.domain.job;

public class OutputFormat {

	private int width;
	private int height;
	private int bitrate;
	private String videoFormat;
	private String audioFormat;

	public OutputFormat(int width, int height, int bitrate, String videoFormat,
			String audioFormat) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.videoFormat = videoFormat;
		this.audioFormat = audioFormat;
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

	public String getVideoFormat() {
		return videoFormat;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

}
