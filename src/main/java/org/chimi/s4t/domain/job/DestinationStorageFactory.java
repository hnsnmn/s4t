package org.chimi.s4t.domain.job;

public interface DestinationStorageFactory {

	DestinationStorage create(String destinationStorage);

	DestinationStorageFactory DEFAULT = new DestinationStorageFactory() {
		@Override
		public DestinationStorage create(String destinationStorage) {
			if (destinationStorage.startsWith("file://")) {
				return new FileDestinationStorage(
						destinationStorage.substring("file://".length()));
			}
			throw new IllegalArgumentException(
					"not supported destination storage: " + destinationStorage);

		}
	};
}
