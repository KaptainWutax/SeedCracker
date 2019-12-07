package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.finder.population.DungeonFinder;
import kaptainwutax.seedcracker.finder.population.EndGatewayFinder;
import kaptainwutax.seedcracker.finder.population.EndPillarsFinder;
import kaptainwutax.seedcracker.finder.population.ore.DiamondOreFinder;
import kaptainwutax.seedcracker.finder.population.ore.EmeraldOreFinder;
import kaptainwutax.seedcracker.finder.population.ore.InfestedStoneOreFinder;
import kaptainwutax.seedcracker.finder.structure.*;
import kaptainwutax.seedcracker.util.FinderBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class FinderConfig {

    protected Map<Type, Boolean> typeStates = new HashMap<>();
    protected Map<Type, ConcurrentLinkedQueue<Finder>> activeFinders = new ConcurrentHashMap<>();

    public FinderConfig() {
        for(Type type: Type.values()) {
            this.typeStates.put(type, true);
        }
    }

    public List<Type> getActiveFinderTypes() {
        return this.typeStates.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Finder> getActiveFinders() {
        this.activeFinders.values().forEach(finders -> {
            finders.removeIf(Finder::isUseless);
        });

        return this.activeFinders.values().stream()
                .flatMap(Queue::stream).collect(Collectors.toList());
    }

    public void addFinder(Type type, Finder finder) {
        if(finder.isUseless())return;

        if(!this.activeFinders.containsKey(type)) {
            this.activeFinders.put(type, new ConcurrentLinkedQueue<>());
        }

        this.activeFinders.get(type).add(finder);
    }

    public boolean getTypeState(Type type) {
        return this.typeStates.get(type);
    }

    public void setTypeState(Type type, boolean flag) {
        this.typeStates.put(type, flag);
    }

    public enum Category {
        STRUCTURES,
        BIOMES,
        ORES,
        OTHERS
    }

    public enum Type {
        BURIED_TREASURE(BuriedTreasureFinder::create, Category.STRUCTURES),
        DESERT_TEMPLE(DesertTempleFinder::create, Category.STRUCTURES),
        END_CITY(EndCityFinder::create, Category.STRUCTURES),
        IGLOO(IglooFinder::create, Category.STRUCTURES),
        JUNGLE_TEMPLE(JungleTempleFinder::create, Category.STRUCTURES),
        MONUMENT(OceanMonumentFinder::create, Category.STRUCTURES),
        SWAMP_HUT(SwampHutFinder::create, Category.STRUCTURES),
        MANSION(MansionFinder::create, Category.STRUCTURES),

        END_PILLARS(EndPillarsFinder::create, Category.OTHERS),
        END_GATEWAY(EndGatewayFinder::create, Category.OTHERS),
        DUNGEON(DungeonFinder::create, Category.OTHERS),
        DIAMOND_ORE(DiamondOreFinder::create, Category.ORES),
        INFESTED_STONE_ORE(InfestedStoneOreFinder::create, Category.ORES),
        EMERALD_ORE(EmeraldOreFinder::create, Category.ORES),

        BIOME(BiomeFinder::create, Category.BIOMES);

        public final FinderBuilder finderBuilder;
        private final Category category;

        Type(FinderBuilder finderBuilder, Category category) {
            this.finderBuilder = finderBuilder;
            this.category = category;
        }

        public static List<Type> getForCategory(Category category) {
            return Arrays.stream(values()).filter(type -> type.category == category).collect(Collectors.toList());
        }
    }

}
