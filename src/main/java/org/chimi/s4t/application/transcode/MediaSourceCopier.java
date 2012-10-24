package org.chimi.s4t.application.transcode;

import java.io.File;

public interface MediaSourceCopier {

	File copy(Long jobId);

}
