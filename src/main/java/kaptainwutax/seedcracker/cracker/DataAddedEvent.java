package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;

@FunctionalInterface
public interface DataAddedEvent {

	void onDataAdded(DataStorage dataStorage);

}
