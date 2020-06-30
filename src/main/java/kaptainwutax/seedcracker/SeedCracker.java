package kaptainwutax.seedcracker;

import kaptainwutax.featureutils.structure.*;
import kaptainwutax.seedcracker.command.ClientCommand;
import kaptainwutax.seedcracker.cracker.decorator.DesertWell;
import kaptainwutax.seedcracker.cracker.decorator.Dungeon;
import kaptainwutax.seedcracker.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcracker.cracker.decorator.EndGateway;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedutils.mc.MCVersion;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Formatting;

public class SeedCracker implements ModInitializer {

	public static final MCVersion MC_VERSION = MCVersion.v1_16;

    private static final SeedCracker INSTANCE = new SeedCracker();
    private DataStorage dataStorage = new DataStorage();
	private boolean active = true;

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public boolean isActive() {
		return this.active;
	}

    public void setActive(boolean active) {
		this.active = active;

	    if(this.active) {
		    ClientCommand.sendFeedback("SeedCracker is active.", Formatting.GREEN, true);
	    } else {
		    ClientCommand.sendFeedback("SeedCracker is not active.", Formatting.RED, true);
	    }
    }

	public void reset() {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().clear();
	}

	public static final BastionRemnant BASTION_REMNANT = new BastionRemnant(SeedCracker.MC_VERSION);
	public static final BuriedTreasure BURIED_TREASURE = new BuriedTreasure(SeedCracker.MC_VERSION);
	public static final DesertPyramid DESERT_PYRAMID = new DesertPyramid(SeedCracker.MC_VERSION);
	public static final EndCity END_CITY = new EndCity(SeedCracker.MC_VERSION);
	public static final Fortress FORTRESS = new Fortress(SeedCracker.MC_VERSION);
	public static final Igloo IGLOO = new Igloo(SeedCracker.MC_VERSION);
	public static final JunglePyramid JUNGLE_PYRAMID = new JunglePyramid(SeedCracker.MC_VERSION);
	public static final Mansion MANSION = new Mansion(SeedCracker.MC_VERSION);
	public static final Mineshaft MINESHAFT = new Mineshaft(SeedCracker.MC_VERSION, Mineshaft.Type.EITHER);
	public static final Monument MONUMENT = new Monument(SeedCracker.MC_VERSION);
	public static final NetherFossil NETHER_FOSSIL = new NetherFossil(SeedCracker.MC_VERSION);
	public static final OceanRuin OCEAN_RUIN = new OceanRuin(SeedCracker.MC_VERSION);
	public static final PillagerOutpost PILLAGER_OUTPOST = new PillagerOutpost(SeedCracker.MC_VERSION);
	public static final RuinedPortal RUINED_PORTAL = new RuinedPortal(SeedCracker.MC_VERSION);
	public static final Shipwreck SHIPWRECK = new Shipwreck(SeedCracker.MC_VERSION);
	public static final Stronghold STRONGHOLD = new Stronghold(SeedCracker.MC_VERSION);
	public static final SwampHut SWAMP_HUT = new SwampHut(SeedCracker.MC_VERSION);
	public static final Village VILLAGE = new Village(SeedCracker.MC_VERSION);

	public static final EndGateway END_GATEWAY = new EndGateway(SeedCracker.MC_VERSION);
	public static final DesertWell DESERT_WELL = new DesertWell(SeedCracker.MC_VERSION);
	public static final EmeraldOre EMERALD_ORE = new EmeraldOre(SeedCracker.MC_VERSION);
	public static final Dungeon DUNGEON = new Dungeon(SeedCracker.MC_VERSION);

}
