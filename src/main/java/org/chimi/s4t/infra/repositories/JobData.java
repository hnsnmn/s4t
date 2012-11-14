package org.chimi.s4t.infra.repositories;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.Job.State;
import org.chimi.s4t.domain.job.OutputFormat;

@Entity
@Table(name = "JOB")
public class JobData {

	@Id
	@Column(name = "JOB_ID")
	private Long id;

	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private Job.State state;

	@Column(name = "SOURCE_URL")
	private String sourceUrl;

	@Column(name = "DESTINATION_URL")
	private String destinationUrl;

	@Column(name = "CALLBACK_URL")
	private String callbackUrl;

	@Column(name = "EXCEPTION_MESSAGE")
	private String exceptionMessage;

	@Transient
	private List<OutputFormat> outputFormats;

	public Long getId() {
		return id;
	}

	public Job.State getState() {
		return state;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

	public static class ExporterToJobData implements Job.Exporter {

		private JobData jobData = new JobData();

		@Override
		public void addId(Long id) {
			jobData.id = id;
		}

		@Override
		public void addState(State state) {
			jobData.state = state;
		}

		@Override
		public void addMediaSource(String url) {
			jobData.sourceUrl = url;
		}

		@Override
		public void addDestinationStorage(String url) {
			jobData.destinationUrl = url;
		}

		@Override
		public void addResultCallback(String url) {
			jobData.callbackUrl = url;
		}

		@Override
		public void addExceptionMessage(String exceptionMessage) {
			jobData.exceptionMessage = exceptionMessage;
		}

		@Override
		public void addOutputFormat(List<OutputFormat> outputFormat) {
			jobData.outputFormats = outputFormat;
		}

		public JobData getJobData() {
			return jobData;
		}
	}
}
