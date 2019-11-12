package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.cracker.StructureData;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SeedCracker implements ModInitializer {

	public static final Logger LOG = LogManager.getLogger("Seed Cracker");

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
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

	public List<Long> getStructureSeeds(int pillarSeed, List<StructureData> structureDataList) {
		List<Long> structureSeeds = new ArrayList<>();
		ChunkRandom chunkRandom = new ChunkRandom();

		for(long i = 0; i < (1L << 32); i++) {
			long structureSeed = this.timeMachine(i, pillarSeed);
			boolean goodSeed = true;

			for(StructureData structureData: structureDataList) {
				chunkRandom.setStructureSeed(structureSeed, structureData.getRegionX(),
						structureData.getRegionZ(), structureData.getSalt());

				if(!structureData.test(chunkRandom)) {
					goodSeed = false;
					break;
				}
			}

			if(goodSeed) {
				structureSeeds.add(structureSeed);
			}
		}
		return structureSeeds;
	}

	private LCG inverseLCG = Rand.JAVA_LCG.combine(-2);

	public long timeMachine(long partialWorldSeed, int pillarSeed) {
		long currentSeed = 0L;
		currentSeed |= (partialWorldSeed & 0xFFFF0000L) << 16;
		currentSeed |= (long)pillarSeed << 16;
		currentSeed |= partialWorldSeed & 0xFFFFL;

		currentSeed = inverseLCG.nextSeed(currentSeed);
		currentSeed ^= Rand.JAVA_LCG.multiplier;
		return currentSeed;
	}


	public List<Integer> getSpikeSeeds(List<Integer> heights) {
		List<Integer> result = new ArrayList<>();

		for(int spikeSeed = 0; spikeSeed < (1 << 16); spikeSeed++) {
			List<Integer> h = this.getSpikeHeights(spikeSeed);
			if(h.equals(heights))result.add(spikeSeed);
		}

		return result;
	}

	public List<Integer> getSpikeHeights(int spikeSeed) {
		List<Integer> indices = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			indices.add(i);
		}

		Collections.shuffle(indices, new Random(spikeSeed));

		List<Integer> heights = new ArrayList<>();

		for (Integer index: indices) {
			heights.add(76 + index * 3);
		}

		return heights;
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
