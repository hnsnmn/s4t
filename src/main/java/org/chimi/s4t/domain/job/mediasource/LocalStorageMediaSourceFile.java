package org.chimi.s4t.domain.job.mediasource;

import java.io.File;

import org.chimi.s4t.domain.job.MediaSourceFile;

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
