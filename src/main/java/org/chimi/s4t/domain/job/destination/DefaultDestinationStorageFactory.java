package org.chimi.s4t.domain.job.destination;

import org.chimi.s4t.domain.job.DestinationStorage;
import org.chimi.s4t.domain.job.DestinationStorageFactory;

public class DefaultDestinationStorageFactory implements
		DestinationStorageFactory {

	@Override
	public DestinationStorage create(String destinationStorage) {
		if (destinationStorage.startsWith("file://")) {
			return new FileDestinationStorage(
					destinationStorage.substring("file://".length()));
		}
		throw new IllegalArgumentException(
				"not supported destination storage: " + destinationStorage);

	}
}
