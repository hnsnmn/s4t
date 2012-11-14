package org.chimi.s4t.springconfig;

import org.chimi.s4t.domain.job.DestinationStorageFactory;
import org.chimi.s4t.domain.job.MediaSourceFileFactory;
import org.chimi.s4t.domain.job.ResultCallbackFactory;
import org.chimi.s4t.domain.job.callback.DefaultResultCallbackFactory;
import org.chimi.s4t.domain.job.destination.DefaultDestinationStorageFactory;
import org.chimi.s4t.domain.job.mediasource.DefaultMediaSourceFileFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

	@Bean
	public ResultCallbackFactory resultCallbackFactory() {
		return new DefaultResultCallbackFactory();
	}

	@Bean
	public DestinationStorageFactory destinationStorageFactory() {
		return new DefaultDestinationStorageFactory();
	}

	@Bean
	public MediaSourceFileFactory mediaSourceFileFactory() {
		return new DefaultMediaSourceFileFactory();
	}
}
