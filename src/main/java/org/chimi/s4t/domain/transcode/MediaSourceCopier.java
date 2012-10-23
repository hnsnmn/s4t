package org.chimi.s4t.domain.transcode;

import java.io.File;

public interface MediaSourceCopier {

	File copy(Long jobId);

}
