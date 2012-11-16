package org.chimi.s4t.infra.persistence;

import java.util.List;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.OutputFormat;
import org.chimi.s4t.domain.job.ResultCallback;
import org.chimi.s4t.domain.job.ThumbnailPolicy;

public class JobImpl extends Job {

	private JobDataDao jobDataDao;

	public JobImpl(JobDataDao jobDataDao, Long id, State state,
			MediaSourceFile mediaSourceFile,
			DestinationStorage destinationStorage,
			List<OutputFormat> outputFormats, ResultCallback callback,
			ThumbnailPolicy thumbnailPolicy, String errorMessage) {
		super(id, state, mediaSourceFile, destinationStorage, outputFormats,
				callback, thumbnailPolicy, errorMessage);
		this.jobDataDao = jobDataDao;
	}

	@Override
	protected void changeState(State newState) {
		super.changeState(newState);
		jobDataDao.updateState(getId(), newState);
	}

	@Override
	protected void exceptionOccurred(RuntimeException ex) {
		String exceptionMessage = ExceptionMessageUtil.getMessage(ex);
		jobDataDao.updateExceptionMessage(getId(), exceptionMessage);
		super.exceptionOccurred(ex);
	}

}
