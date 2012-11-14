package org.chimi.s4t.application.transcode.jobqueue;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryJobQueueTest {

	@Test
	public void shouldBeFifo() {
		MemoryJobQueue queue = new MemoryJobQueue();
		Long jobId1 = 1L;
		Long jobId2 = 2L;
		queue.add(jobId1);
		queue.add(jobId2);
		assertEquals(jobId1, queue.nextJobId());
		assertEquals(jobId2, queue.nextJobId());
	}
}
