package org.chimi.s4t.domain.job;

public interface MediaSourceFileFactory {

	MediaSourceFile create(String mediaSource);

	MediaSourceFileFactory DEFAULT = new MediaSourceFileFactory() {
		@Override
		public MediaSourceFile create(String mediaSource) {
			if (mediaSource.startsWith("file://")) {
				String filePath = mediaSource.substring("file://".length());
				return new LocalStorageMediaSourceFile(filePath);
			}
			throw new IllegalArgumentException("not supported media source: "
					+ mediaSource);
		}
	};

}
