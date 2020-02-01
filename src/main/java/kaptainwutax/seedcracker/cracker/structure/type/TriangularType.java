package kaptainwutax.seedcracker.cracker.structure.type;


import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;

public class TriangularType extends FeatureType<StructureData> {

	protected final int peak;
	protected final double bits;

	public TriangularType(int salt, int distance, int peak) {
		super(salt, distance);
		this.peak = peak;
		this.bits = Math.log(this.peak * this.peak) / Math.log(2);
	}

	@Override
	public boolean test(Rand rand, StructureData data, long structureSeed) {
		return (rand.nextInt(this.peak) + rand.nextInt(this.peak)) / 2 == data.offsetX
				&& (rand.nextInt(this.peak) + rand.nextInt(this.peak)) / 2 == data.offsetZ;
	}

	/*
	 * For this type specifically, the bits approximation is bad since the distribution is special.
	 */
	@Override
	public double getBits() {
		return this.bits;
	}

}
