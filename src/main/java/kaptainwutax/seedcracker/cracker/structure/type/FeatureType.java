package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.ChunkPos;

public abstract class FeatureType<T extends StructureData> {

	public final int salt;
	public final int distance;

	public FeatureType(int salt, int distance) {
		this.salt = salt;
		this.distance = distance;
	}

	public void build(T data, ChunkPos chunkPos) {
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;

		data.chunkX = chunkX;
		data.chunkZ = chunkZ;

		chunkX = chunkX < 0 ? chunkX - this.distance + 1 : chunkX;
		chunkZ = chunkZ < 0 ? chunkZ - this.distance + 1 : chunkZ;

		//Pick out in which region the chunk is.
		int regionX = (chunkX / this.distance);
		int regionZ = (chunkZ / this.distance);

		data.regionX = regionX;
		data.regionZ = regionZ;

		regionX *= this.distance;
		regionZ *= this.distance;

		data.offsetX = chunkPos.x - regionX;
		data.offsetZ = chunkPos.z - regionZ;
	}

	public abstract boolean test(Rand rand, T data, long structureSeed);

}
