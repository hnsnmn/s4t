package org.chimi.s4t.domain.job;

import java.io.File;

public abstract class MediaSourceFile {

	private String url;

	public MediaSourceFile(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract File getSourceFile();
}
