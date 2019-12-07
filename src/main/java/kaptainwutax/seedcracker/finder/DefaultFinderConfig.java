package kaptainwutax.seedcracker.finder;

public class DefaultFinderConfig extends FinderConfig {

    public DefaultFinderConfig() {
        super();
        this.typeStates.put(Type.DIAMOND_ORE, false);
        this.typeStates.put(Type.INFESTED_STONE_ORE, false);
        this.typeStates.put(Type.IGLOO, false);
        this.typeStates.put(Type.MANSION, false);
    }

}
