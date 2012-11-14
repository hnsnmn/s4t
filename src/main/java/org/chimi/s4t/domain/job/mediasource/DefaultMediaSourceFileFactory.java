package org.chimi.s4t.domain.job.mediasource;

import org.chimi.s4t.domain.job.MediaSourceFile;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;

public class DefaultMediaSourceFileFactory implements MediaSourceFileFactory {

	@Override
	public MediaSourceFile create(String mediaSource) {
		if (mediaSource.startsWith("file://")) {
			return new LocalStorageMediaSourceFile(mediaSource);
		}
		throw new IllegalArgumentException("not supported media source: "
				+ mediaSource);
	}

}
