package org.chimi.s4t.application.transcode.jobqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.chimi.s4t.application.transcode.JobQueue;

public class MemoryJobQueue implements JobQueue {

	private BlockingQueue<Long> queue = new LinkedBlockingQueue<Long>();

	@Override
	public void add(Long jobId) {
		queue.add(jobId);
	}

	@Override
	public Long nextJobId() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Blocking Queue interrupted");
		}
	}

}
