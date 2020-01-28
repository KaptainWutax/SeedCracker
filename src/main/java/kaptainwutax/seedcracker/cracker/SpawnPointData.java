package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class SpawnPointData {

	private BlockPos center;
	private int distanceSquared;

	public SpawnPointData(BlockPos center, int maxDistance) {
		this.center = center;
		this.distanceSquared = maxDistance * maxDistance;
	}

	public boolean test(FakeBiomeSource source) {
		BlockPos spawnPos = getSpawnPoint(source);

		int distanceX = spawnPos.getX() - this.center.getX();
		distanceX *= distanceX;
		int distanceZ = spawnPos.getZ() - this.center.getZ();
		distanceZ *= distanceZ;
		return distanceX + distanceZ <= this.distanceSquared;
	}

	public static BlockPos getSpawnPoint(FakeBiomeSource source) {
		List<Biome> spawnBiomes = source.getSpawnBiomes();
		Random random = new Random(source.getSeed());
		BlockPos spawnPos = source.locateBiome(0, 63, 0, 256, spawnBiomes, random);
		ChunkPos chunkPos = spawnPos == null ? new ChunkPos(0, 0) : new ChunkPos(spawnPos);
		return chunkPos.getCenterBlockPos().add(0, 64, 0);
	}

}
