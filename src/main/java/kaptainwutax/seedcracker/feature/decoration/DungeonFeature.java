package kaptainwutax.seedcracker.feature.decoration;

import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DungeonFeature extends DecorationFeature {

    private static int MOSSY_COBBLESTONE_CALL = 0;
    private static int COBBLESTONE_CALL = 1;

    public DungeonFeature(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public void reverseSeed() {
        BlockPos decoratorPos = this.getLocalPos(this.pos);
        Vec3i dungeonSize = this.getDungeonSize(this.pos);

        List<Integer> floorCalls = new ArrayList<>();

        for(int xo = -dungeonSize.getX(); xo <= dungeonSize.getX(); xo++) {
            for(int zo = -dungeonSize.getZ(); zo <= dungeonSize.getZ(); zo++) {
                Block block = this.world.getBlockState(this.pos.add(xo, -1, zo)).getBlock();

                if(block == Blocks.MOSSY_COBBLESTONE) {
                    floorCalls.add(MOSSY_COBBLESTONE_CALL);
                } else if(block == Blocks.COBBLESTONE) {
                    floorCalls.add(COBBLESTONE_CALL);
                }
            }
        }

        List<Long> dungeonSeeds = this.getDungeonSeeds(dungeonSize, floorCalls);
        List<Long> decoratorSeeds = new ArrayList<>();

        for(long dungeonSeed: dungeonSeeds) {
            long decoratorSeed = Rand.JAVA_LCG.combine(-3).nextSeed(dungeonSeed);
            Rand rand = new Rand(decoratorSeed, false);
            if(rand.nextInt(16) != decoratorPos.getX())continue;
            if(rand.nextInt(256) != decoratorPos.getY())continue;
            if(rand.nextInt(16) != decoratorPos.getZ())continue;
            decoratorSeeds.add(decoratorSeed);
        }

        decoratorSeeds.forEach(System.out::println);
    }

    public BlockPos getLocalPos(BlockPos pos) {
        return new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
    }

    public Vec3i getDungeonSize(BlockPos spawnerPos) {
        for(int xo = 4; xo >= 3; xo--) {
            for(int zo = 4; zo >= 3; zo--) {
                Block block = this.getBlockAt(spawnerPos.add(xo, -1, zo));
                if(block != Blocks.MOSSY_COBBLESTONE)continue;
                if(block != Blocks.COBBLESTONE)continue;
                return new Vec3i(xo, 0, zo);
            }
        }
        
        return Vec3i.ZERO;
    }

    public List<Long> getDungeonSeeds(Vec3i dungeonSize, List<Integer> floorCalls) {
        List<Long> floorSeeds = new ArrayList<>();

        //TODO: Lattice magic to find floorSeeds from floorCalls.

        List<Long> dungeonSeeds = new ArrayList<>();

        for(long floorSeed: floorSeeds) {
            long dungeonSeed = Rand.JAVA_LCG.combine(-2).nextSeed(floorSeed);
            Rand rand = new Rand(dungeonSeed, false);
            if(rand.nextInt(2) + 3 != dungeonSize.getX())continue;
            if(rand.nextInt(2) + 3 != dungeonSize.getZ())continue;
            dungeonSeeds.add(dungeonSeed);
        }

        return dungeonSeeds;
    }

}
