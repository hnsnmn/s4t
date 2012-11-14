package org.chimi.s4t.infra.repositories;

import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.Job;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;

public class JobImpl extends Job {

	public JobImpl(JobData jobData,
			MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory,
			ResultCallbackFactory resultCallbackFactory) {
		super(jobData.getId(), mediaSourceFileFactory.create(jobData
				.getSourceUrl()), destinationStorageFactory.create(jobData
				.getSourceUrl()), jobData.getOutputFormats(),
				resultCallbackFactory.create(jobData.getCallbackUrl()));
	}
}
