package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class SawbladeHollyBushBlock extends PlantBlock implements Fertilizable {
    
    public static final int MAX_TINY_AGE = 0;
    public static final int MAX_SMALL_AGE = 2;
    public static final int MAX_AGE = Properties.AGE_7_MAX;
    public static final IntProperty AGE = Properties.AGE_7;
    private static final VoxelShape SMALL_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    
    public static final Identifier HARVESTING_LOOT_TABLE_ID = SpectrumCommon.locate("gameplay/sawblade_holly_harvesting");
    public static final Identifier SHEARING_LOOT_TABLE_ID = SpectrumCommon.locate("gameplay/sawblade_holly_shearing");
    
    public SawbladeHollyBushBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
    
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < MAX_AGE;
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            BlockState blockState = state.with(AGE, i + 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY);
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(SpectrumBlocks.SAWBLADE_GRASS) || floor.isOf(Blocks.PODZOL);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(AGE) <= MAX_TINY_AGE) {
            return SMALL_SHAPE;
        } else {
            return state.get(AGE) <= MAX_SMALL_AGE ? LARGE_SHAPE : super.getOutlineShape(state, world, pos, context);
        }
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int age = state.get(AGE);
        
        ItemStack handStack = player.getStackInHand(hand);
        if (canBeSheared(age) && handStack.isIn(ConventionalItemTags.SHEARS)) {
            if (!world.isClient) {
                for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), SHEARING_LOOT_TABLE_ID)) {
                    dropStack(world, pos, stack);
                }
                handStack.damage(1, player, (p) -> {
                    p.sendToolBreakStatus(hand);
                });
            }
            
            BlockState newState = state.with(AGE, state.get(AGE) - 1);
            world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.SHEAR, pos, GameEvent.Emitter.of(player, newState));
            world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            
            return ActionResult.success(world.isClient);
        } else if (age == MAX_AGE) {
            if (!world.isClient) {
                for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(state, (ServerWorld) world, pos, world.getBlockEntity(pos), player, player.getMainHandStack(), HARVESTING_LOOT_TABLE_ID)) {
                    dropStack(world, pos, stack);
                }
            }
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            
            BlockState newState = state.with(AGE, 4);
            world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.SHEAR, pos, GameEvent.Emitter.of(player, newState));
            
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }
    
    public static boolean canBeSheared(int age) {
        return age > MAX_SMALL_AGE;
    }
    
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < MAX_AGE;
    }
    
    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }
    
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(MAX_AGE, state.get(AGE) + (random.nextBoolean() ? 1 : 2));
        world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_LISTENERS);
    }
    
}
