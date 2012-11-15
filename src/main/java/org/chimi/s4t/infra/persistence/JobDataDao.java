package org.chimi.s4t.infra.persistence;

import org.chimi.s4t.domain.job.Job;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface JobDataDao extends Repository<JobData, Long> {

	public JobData save(JobData jobData);

	public JobData findById(Long id);

	@Transactional
	@Modifying
	@Query("update JobData j set j.state = ?2 where j.id = ?1")
	public int updateState(Long id, Job.State newState);

	@Transactional
	@Modifying
	@Query("update JobData j set j.exceptionMessage = ?2 where j.id = ?1")
	public void updateExceptionMessage(Long id, String exceptionMessage);
}
