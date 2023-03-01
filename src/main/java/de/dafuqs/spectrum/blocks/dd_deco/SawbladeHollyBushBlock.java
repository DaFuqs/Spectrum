package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.id.incubus_core.block.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class SawbladeHollyBushBlock extends TallCropBlock {

    protected static final Identifier SAWBLADE_HOLLY_HARVESTING_IDENTIFIER = SpectrumCommon.locate("gameplay/sawblade_holly_harvesting");
    protected static final Identifier SAWBLADE_HOLLY_SHEARING_IDENTIFIER = SpectrumCommon.locate("gameplay/sawblade_holly_shearing");

    protected static final int LAST_SINGLE_BLOCK_AGE = 2;
    protected static final int MAX_AGE = 7;
    protected static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
    };

    public SawbladeHollyBushBlock(Settings settings) {
        super(settings, LAST_SINGLE_BLOCK_AGE);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return SpectrumItems.SAWBLADE_HOLLY_BERRY;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(SpectrumBlocks.SAWBLADE_GRASS) || floor.isOf(Blocks.PODZOL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int age = state.get(AGE);

        ItemStack handStack = player.getStackInHand(hand);
        if (age > LAST_SINGLE_BLOCK_AGE + 1 && handStack.isIn(ConventionalItemTags.SHEARS)) {
            if (!world.isClient) {
                for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), SAWBLADE_HOLLY_SHEARING_IDENTIFIER)) {
                    dropStack(world, pos, stack);
                }
                handStack.damage(1, player, (p) -> {
                    p.sendToolBreakStatus(hand);
                });
            }
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            BlockState newState = setAge(state, world, pos, age - 1);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, newState));
            return ActionResult.success(world.isClient);
        } else if (age == MAX_AGE) {
            if (!world.isClient) {
                for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), SAWBLADE_HOLLY_HARVESTING_IDENTIFIER)) {
                    dropStack(world, pos, stack);
                }
            }
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            BlockState newState = setAge(state, world, pos, LAST_SINGLE_BLOCK_AGE + 1);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, newState));
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    private BlockState setAge(BlockState state, World world, BlockPos pos, int newAge) {
        BlockState newState = state.with(AGE, newAge);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            world.setBlockState(pos, newState, 2);
            BlockState upState = world.getBlockState(pos.up());
            if (upState.isOf(this)) {
                world.setBlockState(pos.up(), upState.with(AGE, newAge), 2);
            }
        } else {
            world.setBlockState(pos, newState, 2);
            BlockState downState = world.getBlockState(pos.down());
            if (downState.isOf(this)) {
                world.setBlockState(pos.down(), downState.with(AGE, newAge), 2);
            }
        }
        return newState;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (state.get(AGE) <= this.lastSingleBlockAge) {
                return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
            } else {
                // Fill in the bottom block if the plant is two-tall
                return VoxelShapes.fullCube();
            }
        } else {
            return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
        }
    }

}
