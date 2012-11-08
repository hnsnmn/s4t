package org.chimi.s4t.domain.job;

import java.io.File;

public class LocalStorageMediaSourceFile implements MediaSourceFile {

	private String filePath;

	public LocalStorageMediaSourceFile(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public File getSourceFile() {
		return new File(filePath);
	}

}
