package de.dafuqs.spectrum.blocks.dd_deco;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MudBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class GodawfulMudBlock extends MudBlock {

    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 11, 16);

    public GodawfulMudBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.003F) {
            world.playSound(null, pos, SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat() * 0.2F);
        }

        if (random.nextFloat() < 0.003F) {
            world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.AMBIENT, random.nextFloat() * 0.4F + 0.25F, random.nextFloat() * 0.5F + 0.1F);
        }
        if (random.nextFloat() < 0.001F) {
            world.playSound(null, pos, SoundEvents.ENTITY_FROG_AMBIENT, SoundCategory.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.3F + 0.01F);
        }
        if (random.nextFloat() < 0.002F) {
            world.playSound(null, pos, SoundEvents.BLOCK_SCULK_PLACE, SoundCategory.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.4F + 0.2F);
        }

        if (random.nextFloat() < 0.001F) {
            world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.AMBIENT, random.nextFloat() * 2F, 0.1F);
        }

        if (random.nextFloat() < 0.0001F) {
            world.playSound(null, pos, SoundEvents.ENTITY_VILLAGER_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F);
        }
        if (random.nextFloat() < 0.0001F) {
            world.playSound(null, pos, SoundEvents.ENTITY_PARROT_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F);
        }
        if (random.nextFloat() < 0.0001F) {
            world.playSound(null, pos, SoundEvents.ENTITY_CAT_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F);
        }
        if (random.nextFloat() < 0.0001F) {
            world.playSound(null, pos, SoundEvents.ENTITY_WOLF_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F);
        }
        if (random.nextFloat() < 0.000025F) {
            world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.AMBIENT, 2F, 0.1F);
        }
    }
}
