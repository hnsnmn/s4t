package org.chimi.s4t.domain.job.mediasource;

import java.io.File;

import org.chimi.s4t.domain.job.MediaSourceFile;

public class LocalStorageMediaSourceFile extends MediaSourceFile {

	public LocalStorageMediaSourceFile(String url) {
		super(url);
	}

	@Override
	public File getSourceFile() {
		return new File(getUrl());
	}

}
