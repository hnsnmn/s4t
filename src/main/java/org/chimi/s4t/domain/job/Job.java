package org.chimi.s4t.domain.job;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING, COMPLETED
	}

	private State state;
	private Exception occurredException;

	public void changeState(State newState) {
		this.state = newState;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public State getLastState() {
		return state;
	}

	public Exception getOccurredException() {
		return occurredException;
	}

	public void exceptionOccurred(RuntimeException ex) {
		occurredException = ex;
	}

}
