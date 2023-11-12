package com.kyanite.deeperdarker.world.features;

import com.kyanite.deeperdarker.content.DDBlocks;
import com.kyanite.deeperdarker.content.blocks.vegetation.GlowingVinesPlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlowingVinesFeature extends Feature<NoneFeatureConfiguration> {
    public GlowingVinesFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos origin = pContext.origin();
        if(level.isEmptyBlock(origin)) {
            BlockState state = level.getBlockState(origin.above());
            if(state.is(Blocks.SCULK) || state.is(DDBlocks.SCULK_STONE.get())) {
                this.placeVines(level, pContext.random(), origin);
                return true;
            }
        }

        return false;
    }

    private void placeVines(LevelAccessor level, RandomSource random, BlockPos pos) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 100; i++) {
            blockPos.setWithOffset(pos, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if(level.isEmptyBlock(blockPos)) {
                BlockState state = level.getBlockState(blockPos.above());
                if(state.is(Blocks.SCULK) || state.is(DDBlocks.SCULK_STONE.get())) {
                    int length = Mth.nextInt(random, 6, 24);
                    if(random.nextFloat() < 0.12f) length *= 2;
                    if(random.nextFloat() < 0.125f) length = 1;

                    placeVinesColumn(level, random, blockPos, length);
                }
            }
        }
    }

    private void placeVinesColumn(LevelAccessor level, RandomSource random, BlockPos.MutableBlockPos pos, int length) {
        for(int i = 0; i <= length; i++) {
            if(level.isEmptyBlock(pos)) {
                if(i == length || !level.isEmptyBlock(pos.below())) {
                    level.setBlock(pos, DDBlocks.GLOWING_VINES.get().defaultBlockState().setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(random, 17, 25)), 3);
                    break;
                }

                BlockState vines = DDBlocks.GLOWING_VINES_PLANT.get().defaultBlockState();
                if(random.nextFloat() < 0.25f) vines = vines.setValue(GlowingVinesPlantBlock.BERRIES, true);
                level.setBlock(pos, vines, 3);
            }

            pos.move(Direction.DOWN);
        }
    }
}