package org.chimi.s4t.domain.job;

import java.io.File;
import java.util.List;

public interface Transcoder {

	List<File> transcode(File multimediaFile, List<OutputFormat> outputFormat);

}
