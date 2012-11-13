package org.chimi.s4t.application.transcode;

import java.util.List;

import org.chimi.s4t.domain.job.OutputFormat;

public class AddJobRequest {

	private String mediaSource;
	private String destinationStorage;
	private List<OutputFormat> outputFormats;
	private String resultCallback;

	public String getMediaSource() {
		return mediaSource;
	}

	public void setMediaSource(String mediaSource) {
		this.mediaSource = mediaSource;
	}

	public String getDestinationStorage() {
		return destinationStorage;
	}

	public void setDestinationStorage(String destinationStorage) {
		this.destinationStorage = destinationStorage;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

	public void setOutputFormats(List<OutputFormat> outputFormats) {
		this.outputFormats = outputFormats;
	}

	public String getResultCallback() {
		return resultCallback;
	}

	public void setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
	}

	@Override
	public String toString() {
		return "AddJobRequest [mediaSource=" + mediaSource
				+ ", destinationStorage=" + destinationStorage
				+ ", outputFormats=" + outputFormats + ", resultCallback="
				+ resultCallback + "]";
	}

}
