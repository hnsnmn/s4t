package org.chimi.s4t.application.transcode;

import java.io.File;
import java.util.List;

public interface CreatedFileSaver {

	void store(List<File> multimediaFiles, List<File> thumbnails, Long jobId);

}
