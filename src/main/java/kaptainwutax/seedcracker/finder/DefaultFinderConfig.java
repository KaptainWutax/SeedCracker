package kaptainwutax.seedcracker.finder;

public class DefaultFinderConfig extends FinderConfig {

    public DefaultFinderConfig() {
        this.typeStates.put(Type.IGLOO, false);
        this.typeStates.put(Type.MANSION, false);
    }

}
