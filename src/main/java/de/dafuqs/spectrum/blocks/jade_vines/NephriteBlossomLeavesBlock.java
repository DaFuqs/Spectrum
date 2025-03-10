package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
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
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class NephriteBlossomLeavesBlock extends LeavesBlock implements Fertilizable {
    
    public static final IntProperty AGE = Properties.AGE_2;
    public static final int MAX_AGE = Properties.AGE_2_MAX;
    
    public NephriteBlossomLeavesBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0));
    }
    
    @Override
	@SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == MAX_AGE) {
			ItemStack handStack = player.getStackInHand(hand);
			int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, handStack) / 2;
			int count = 1 + world.getRandom().nextInt(fortuneLevel + 1);
			player.getInventory().offerOrDrop(new ItemStack(SpectrumItems.GLASS_PEACH, count));
	
			world.setBlockState(pos, state.with(AGE, 0));
			player.playSound(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1, 1 + player.getRandom().nextFloat() * 0.25F);
			return ActionResult.success(world.isClient());
		}
	
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		int leafSum = 0;
		
		if (state.get(PERSISTENT) || random.nextFloat() > 0.1F) {
			super.randomTick(state, world, pos, random);
			return;
		}
    
        for (BlockPos iPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            var leafState = world.getBlockState(iPos);
            if (leafState.isOf(this)) {
                leafSum += (leafState.get(AGE).byteValue() + 1) * 3;
            }
        }

        leafSum = Math.max(leafSum, 0) + 1;

        if (random.nextInt(leafSum) != 0) {
            super.randomTick(state, world, pos, random);
            return;
        }

        if (age == 2) {
            BlockPos.Mutable dropPos = pos.mutableCopy();
            while (world.getBlockState(dropPos).isOf(this) && pos.getY() - dropPos.getY() < 32) {
                dropPos.move(0, -1, 0);
            }
            ItemStack drop = new ItemStack(SpectrumItems.GLASS_PEACH);
            world.spawnEntity(new ItemEntity(world, dropPos.getX() + 0.5, dropPos.getY() + 0.15, dropPos.getZ() + 0.5, drop));
            BlockState newState = state.with(AGE, 0);
            world.setBlockState(pos, newState);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
        }
        else {
            world.setBlockState(pos, state.with(AGE, age + 1));
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) != MAX_AGE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }

    @Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) != 2;
	}

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var age = state.get(AGE);
        if (age == MAX_AGE)
            return;
    
        world.setBlockState(pos, state.with(AGE, age + 1));
    }
}
