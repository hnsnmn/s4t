package org.chimi.s4t.domain.transcode;

import java.io.File;
import java.util.List;

public interface ThumbnailExtractor {

	List<File> extract(File multimediaFile, Long jobId);

}
