package org.chimi.s4t.infra.persistence;

public abstract class ExceptionMessageUtil {

	public static String getMessage(Exception ex) {
		return ex.getMessage() == null ? ex.getClass().getName() : ex
				.getMessage();
	}
}
