package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public abstract class DestinationStorage {

	private String url;

	public DestinationStorage(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void save(List<File> multimediaFiles, List<File> thumbnails);

}
