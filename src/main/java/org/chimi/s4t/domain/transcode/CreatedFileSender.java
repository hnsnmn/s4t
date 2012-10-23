package org.chimi.s4t.domain.transcode;

import java.io.File;
import java.util.List;

public interface CreatedFileSender {

	void send(List<File> multimediaFiles, List<File> thumbnails, Long jobId);

}
