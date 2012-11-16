package org.chimi.s4t.domain.job;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class ThumbnailPolicy {

	public static enum Option {
		NONE, FIRST, MULTIPLE
	}

	@Column(name = "TP_OPTION")
	@Enumerated(EnumType.STRING)
	private Option option;
	@Column(name = "TP_WIDTH")
	private int width;
	@Column(name = "TP_HEIGHT")
	private int height;
	@Column(name = "TP_BEGINTIME")
	private int beginTimeInSeconds;
	@Column(name = "TP_ENDTIME")
	private int endTimeInSeconds;
	@Column(name = "TP_INTERVAL")
	private int interval;

	public Option getOption() {
		return option;
	}

	public void setOption(Option extract) {
		this.option = extract;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBeginTimeInSeconds() {
		return beginTimeInSeconds;
	}

	public void setBeginTimeInSeconds(int beginTimeInSeconds) {
		this.beginTimeInSeconds = beginTimeInSeconds;
	}

	public int getEndTimeInSeconds() {
		return endTimeInSeconds;
	}

	public void setEndTimeInSeconds(int endTimeInSeconds) {
		this.endTimeInSeconds = endTimeInSeconds;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
