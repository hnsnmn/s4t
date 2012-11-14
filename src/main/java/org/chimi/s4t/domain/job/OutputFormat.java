package org.chimi.s4t.domain.job;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class OutputFormat {

	@Column(name = "WIDTH")
	private int width;

	@Column(name = "HEIGHT")
	private int height;

	@Column(name = "BITRATE")
	private int bitrate;

	@Column(name = "CONTAINER")
	@Enumerated(EnumType.STRING)
	private Container container;

	@Column(name = "VIDEO_CODEC")
	@Enumerated(EnumType.STRING)
	private VideoCodec videoCodec;

	@Column(name = "AUDIO_CODEC")
	@Enumerated(EnumType.STRING)
	private AudioCodec audioCodec;

	@SuppressWarnings("unused")
	private OutputFormat() {
	}

	public OutputFormat(int width, int height, int bitrate,
			Container container, VideoCodec videoCodec, AudioCodec audioCodec) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.container = container;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
	}

	public OutputFormat(int width, int height, int bitrate, Container container) {
		this(width, height, bitrate, container, container
				.getDefaultVideoCodec(), container.getDefaultAudioCodec());
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
