package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public interface DestinationStorage {

	void save(List<File> multimediaFiles, List<File> thumbnails);

}
