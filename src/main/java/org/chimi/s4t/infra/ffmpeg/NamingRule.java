package org.chimi.s4t.infra.ffmpeg;

import org.chimi.s4t.domain.job.OutputFormat;

public interface NamingRule {

	String createName(OutputFormat format);

}
