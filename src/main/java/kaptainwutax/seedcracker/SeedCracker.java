package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.cracker.PillarData;
import kaptainwutax.seedcracker.cracker.StructureData;
import kaptainwutax.seedcracker.cracker.TimeMachine;
import kaptainwutax.seedcracker.render.RenderQueue;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SeedCracker implements ModInitializer {

	public static final Logger LOG = LogManager.getLogger("Seed Cracker");

	public List<Long> worldSeeds = null;
	public List<Long> structureSeeds = null;
	public List<Integer> pillarSeeds = null;

	private TimeMachine timeMachine = new TimeMachine();
	private List<StructureData> structureCache = new ArrayList<>();
	private List<BiomeData> biomeCache = new ArrayList<>();

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public void reset() {
		this.worldSeeds = null;
		this.structureSeeds = null;
		this.pillarSeeds = null;
		this.structureCache.clear();
		this.biomeCache.clear();
	}

	public void onPillarData(PillarData pillarData) {
		if(this.pillarSeeds == null) {
			this.pillarSeeds = pillarData.getPillarSeeds();
			this.onStructureData(null);
		}
	}

	public void onStructureData(StructureData structureData) {
		if(structureData != null) {
			this.structureCache.add(structureData);
		}

		if(this.structureSeeds == null && this.pillarSeeds != null && this.structureCache.size() >= 3) {
			this.structureSeeds = new ArrayList<>();

			this.pillarSeeds.forEach(pillarSeed -> {
				timeMachine.buildStructureSeeds(pillarSeed, this.structureCache, this.structureSeeds);
			});

			this.structureCache.clear();
			this.onBiomeData(null);
		} else if(this.structureSeeds != null && structureData != null) {
			this.structureSeeds.removeIf(structureSeed -> {
				ChunkRandom chunkRandom = new ChunkRandom();
				chunkRandom.setStructureSeed(structureSeed, structureData.getRegionX(), structureData.getRegionZ(), structureData.getSalt());
				return !structureData.test(chunkRandom);
			});

			this.structureCache.clear();
			this.onBiomeData(null);
		}
	}

	public void onBiomeData(BiomeData biomeData) {
		if(biomeData != null) {
			this.biomeCache.add(biomeData);
		}

		if(this.worldSeeds == null && this.structureSeeds != null && this.biomeCache.size() >= 3) {
			this.worldSeeds = new ArrayList<>();

			this.structureSeeds.forEach(structureSeed -> {
				for(long i = 0; i < (1L << 16); i++) {
					long worldSeed = (i << 48) | structureSeed;
					boolean goodSeed = true;

					for(BiomeData data: this.biomeCache) {
						if(!data.test(worldSeed)) {
							goodSeed = false;
							break;
						}
					}

					if(goodSeed) {
						this.worldSeeds.add(worldSeed);
					}
				}
			});

			this.biomeCache.clear();
		} else if(this.worldSeeds != null && biomeData != null) {
			this.worldSeeds.removeIf(worldSeed -> !biomeData.test(worldSeed));
		}
	}



	public static void main(String[] args) {
		//91,94,82,85,88,79,97,76,100,103
		SeedCracker cracker = new SeedCracker();
		List<Integer> heights = new ArrayList<>();
		heights.add(91);
		heights.add(94);
		heights.add(82);
		heights.add(85);
		heights.add(88);
		heights.add(79);
		heights.add(97);
		heights.add(76);
		heights.add(100);
		heights.add(103);

		List<StructureData> structureDataList = new ArrayList<>();
		structureDataList.add(new StructureData(new ChunkPos(-37, -95), StructureData.END_CITY));
		structureDataList.add(new StructureData(new ChunkPos(-57,-116), StructureData.END_CITY));
		structureDataList.add(new StructureData(new ChunkPos(-18, 204), StructureData.OCEAN_MONUMENT));
		structureDataList.add(new StructureData(new ChunkPos(-44, 109), StructureData.OCEAN_MONUMENT));
		structureDataList.add(new StructureData(new ChunkPos(14, -158), StructureData.DESERT_PYRAMID));

		cracker.getSpikeSeeds(heights).forEach(seed -> {
			System.out.println("Found pillar seed: [" + seed + "].");

			structureDataList.forEach(s -> {
				System.out.println("Structure data: [" + s.getSalt() + ", " + s.getOffsetX() + ", " + s.getOffsetZ() + "].");
			});

			cracker.getStructureSeeds(seed, structureDataList).forEach(seed1 -> {
				System.out.println("Found structure seed: [" + seed1 + "].");

				for(long i = 0; i < (1L << 16); i++) {
					long worldSeed = (i << 48) | seed1;
					BiomeLayerSampler[] biomeLayerSamplers = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT, BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings());
					//System.out.println(worldSeed + ", " + biomeLayerSamplers[0].sample(0, 0));
				}
			});
		});
	}



	/*
	public List<Integer> getSpikeHeights(long worldSeed) {
		Random rand = new Random(worldSeed);
		long pillarSeed = rand.nextLong() & 65535L;

		List<Integer> indices = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			indices.add(i);
		}

		Collections.shuffle(indices, new Random(pillarSeed));

		List<Integer> heights = new ArrayList<>();

		for (Integer index: indices) {
			heights.add(76 + index * 3);
		}

		return heights;
	}


	protected ChunkPos getStart(ChunkGenerator<?> chunkGenerator_1, Random rand, int chunkX, int chunkZ) {
		int templeDistance = 32;
		int templeSeparation = 8;

		chunkX = chunkX < 0 ? chunkX - templeDistance + 1 : chunkX;
		chunkZ = chunkZ < 0 ? chunkZ - templeDistance + 1 : chunkZ;

		//Pick out in which region the chunk is.
		int regionX = chunkX / templeDistance;
		int regionZ = chunkZ / templeDistance;

		((ChunkRandom)rand).setStructureSeed(chunkGenerator_1.getSeed(), regionX, regionZ, this.getSeedModifier());

		//Convert it back to a chunk pos.
		regionX *= templeDistance;
		regionZ *= templeDistance;

		//Add a random chunk offset.
		regionX += rand.nextInt(templeDistance - templeSeparation);
		regionZ += rand.nextInt(templeDistance - templeSeparation);

		return new ChunkPos(regionX, regionZ);
	}

	public void getTempleCalls(ChunkPos chunkPos) {
		int templeDistance = 32;

		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;

		chunkX = chunkX < 0 ? chunkX - templeDistance + 1 : chunkX;
		chunkZ = chunkZ < 0 ? chunkZ - templeDistance + 1 : chunkZ;

		//Pick out in which region the chunk is.
		int regionX = (chunkX / templeDistance) * templeDistance;
		int regionZ = (chunkZ / templeDistance) * templeDistance;

		int offsetX = chunkPos.x - regionX;
		int offsetZ = chunkPos.z - regionZ;
	}

	public boolean shouldStartAt(ChunkGenerator<?> chunkGen, Random random_1, int chunkX, int chunkZ) {
		ChunkPos startPosition = this.getStart(chunkGen, random_1, chunkX, chunkZ);

		if(chunkX == startPosition.x && chunkZ == startPosition.z) {
			Biome biome = chunkGen.getBiomeSource().getBiome(new BlockPos(chunkX * 16 + 9, 0, chunkZ * 16 + 9));

			if(chunkGen.hasStructure(biome, this)) {
				return true;
			}
		}

		return false;
	}

	public long setStructureSeed(long worldSeed, int regionX, int regionZ, int salt) {
		long seed = (long)regionX * 341873128712L + (long)regionZ * 132897987541L + worldSeed + (long)salt;
		seed = (seed ^ Rand.JAVA_LCG.multiplier) & (Rand.JAVA_LCG.modulo - 1);
		return seed;
	}

	public void getStart(long worldSeed, int regionX, int regionZ, int salt) {
		long seed = this.setStructureSeed(worldSeed, regionX, regionZ, salt);
		int nextIntSeed = (int)(Rand.JAVA_LCG.nextSeed(seed) >>> (48 - 31));
		int chunkX = nextIntSeed % 24;
		nextIntSeed = (int)(Rand.JAVA_LCG.nextSeed(Rand.JAVA_LCG.nextSeed(seed)) >>> (48 - 31));
		int chunkZ = nextIntSeed % 24;
	}
	*/

}
