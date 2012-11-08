package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public class FileDestinationStorage implements DestinationStorage {

	private String folder;

	public FileDestinationStorage(String folder) {
		this.folder = folder;
	}

	@Override
	public void save(List<File> multimediaFiles, List<File> thumbnails) {

	}

}
