package org.chimi.s4t.domain.job;

public interface JobRepository {

	Job findById(Long jobId);

	Job save(Job job);

	Job findEldestJobOfWaitingState();

}
