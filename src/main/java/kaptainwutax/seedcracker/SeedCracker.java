package kaptainwutax.seedcracker;

import com.google.common.hash.Hashing;
import kaptainwutax.seedcracker.command.ClientCommand;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedutils.lcg.rand.JRand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.nio.charset.StandardCharsets;

public class SeedCracker implements ModInitializer {

    private static final SeedCracker INSTANCE = new SeedCracker();
    private DataStorage dataStorage = new DataStorage();
	private boolean active = true;

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
		//SeedCracker.main(null);
	}

	public static SeedCracker get() {
	    return INSTANCE;
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

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public void reset() {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().clear();
	}

	public static void main(String[] args) {
		int s = Hashing.sha256().hashString("joe" + ":why_so_salty#LazyCrypto", StandardCharsets.UTF_8).asInt() & Integer.MAX_VALUE;

		for(int seed = 0; seed < Integer.MAX_VALUE; seed++) {
			JRand rand = new JRand(seed, true);
			rand.nextInt(3); //0 = Normal Voronoi, 1 = Horizontal Voronoi, 2 = New Voronoi I don't comprehend
			rand.nextBoolean(); //Does the dimension have skylight?
			int propertiesSeed = rand.nextInt(); //This seed is used for creating dimension properties such as if vater vaporizes, if it's nether-like, etc. Will be documenting this soon.

			rand.setSeed((long)propertiesSeed, true);
			rand.nextFloat(); //Defines the light level to brightness conversion.
			boolean vaporize = rand.nextInt(5) == 0; //Does water vaporize?
			rand.nextBoolean(); //Is this dimension nether-like?
			rand.nextBoolean(); //Is the sky immobile?
			rand.nextFloat(); //If the sky is immobile, this is the angle it's stuck at.
			rand.nextBoolean();
			boolean endSky = rand.nextInt(8) == 0; //Does it have an end sky?
			Math.max(100.0D, rand.nextGaussian() * 3.0D * 24000.0D); //Number of ticks per day.
			Vec3d thing = new Vec3d(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()); //Fog color multiplier.
			thing = new Vec3d(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()); //Fog color addend.
			rand.nextBoolean(); //Is the dimension foggy?
			float d = (float) Math.max(5.0D, 30.0D * (1.0D + 4.0D * rand.nextGaussian())); //Sun size scalar.
			d = (float)Math.max(5.0D, 30.0D * (1.0D + 4.0D * rand.nextGaussian())); //Moon size scalar.
			Vector3f thing2 = rand.nextBoolean() ? new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()) : new Vector3f(1.0F, 1.0F, 1.0F); //Sun Color.
			thing2 = rand.nextBoolean() ? new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()) : new Vector3f(1.0F, 1.0F, 1.0F); //Moon Color.
			rand.nextInt(255); //Cloud height.

			rand.nextInt();
			int n = rand.nextInt(15);

			for(int i = 2; i < n; i++) {
				rand.nextInt();

				do {
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
				} while(rand.nextBoolean());
			}

			while(rand.nextBoolean()) {
				rand.nextInt(81); //81 biomes.

				do {
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
				} while(rand.nextBoolean());
			}

			if(rand.nextInt(7) == 0) {
				for(int var9 = 0; var9 < Direction.values().length; ++var9) {
					rand.nextGaussian();
				}
			}

			if(rand.nextInt(4) == 0) {
				//this.field_23500 = this.method_26520(chunkJRandom); //SKIP THIS SEED!
				continue;
			}

			if(rand.nextInt(3) == 0) {
				int i = rand.nextInt(6) + 2;

				for(int j = 0; j < i; ++j) {
					rand.nextFloat();
					rand.nextFloat();
					rand.nextFloat();
				}
			}

			int i = rand.nextInt();

			if(rand.nextInt(3) == 0) {
				rand.setSeed(i, true);
				rand.nextInt(128);
				int v = rand.nextInt(8) + 1;
				v = rand.nextInt(8) + 1;

				if(rand.nextInt(252) == 37 && rand.nextInt(5) == 2 && rand.nextInt(252) == 69) {
					System.out.println(seed);
				}
			}
		}
	}

}
