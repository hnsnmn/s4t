package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public interface ThumbnailExtractor {

	List<File> extract(File multimediaFile, ThumbnailPolicy thumbnailPolicy);

}
