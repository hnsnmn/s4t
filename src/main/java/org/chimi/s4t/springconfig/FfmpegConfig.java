package org.chimi.s4t.springconfig;

import org.chimi.s4t.domain.job.Transcoder;
import org.chimi.s4t.infra.ffmpeg.FfmpegTranscoder;
import org.chimi.s4t.infra.ffmpeg.NamingRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FfmpegConfig {

	@Bean
	public Transcoder transcoder() {
		return new FfmpegTranscoder(NamingRule.DEFAULT);
	}

}
