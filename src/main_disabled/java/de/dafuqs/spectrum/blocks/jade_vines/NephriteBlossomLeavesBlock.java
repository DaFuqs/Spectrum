package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;

public class NephriteBlossomLeavesBlock extends LeavesBlock implements BonemealableBlock {
	
	public static final MapCodec<NephriteBlossomLeavesBlock> CODEC = simpleCodec(NephriteBlossomLeavesBlock::new);
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	public static final int MAX_AGE = BlockStateProperties.MAX_AGE_2;
	
	public NephriteBlossomLeavesBlock(Properties settings) {
        super(settings);
		registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
	public MapCodec<? extends NephriteBlossomLeavesBlock> codec() {
        return CODEC;
    }
    
    @Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getValue(AGE) == MAX_AGE) {
			int fortuneLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.FORTUNE, handStack) / 2;
			int count = 1 + world.getRandom().nextInt(fortuneLevel + 1);
			player.getInventory().placeItemBackInInventory(new ItemStack(SpectrumItems.GLASS_PEACH, count));
			
			world.setBlockAndUpdate(pos, state.setValue(AGE, 0));
			player.playNotifySound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 1 + player.getRandom().nextFloat() * 0.25F);
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		
		return super.useItemOn(handStack, state, world, pos, player, hand, hit);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int age = state.getValue(AGE);
		int leafSum = 0;
		
		if (state.getValue(PERSISTENT) || random.nextFloat() > 0.1F) {
			super.randomTick(state, world, pos, random);
			return;
		}
		
		for (BlockPos iPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            var leafState = world.getBlockState(iPos);
			if (leafState.is(this)) {
				leafSum += (leafState.getValue(AGE).byteValue() + 1) * 3;
            }
        }

        leafSum = Math.max(leafSum, 0) + 1;
		
		if (random.nextInt(leafSum) != 0) {
			super.randomTick(state, world, pos, random);
		} else {
			world.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
		}
    }

    @Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) != MAX_AGE;
    }

    @Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return state.getValue(AGE) != 2;
	}

    @Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		var age = state.getValue(AGE);
        if (age == MAX_AGE)
            return;
		
		world.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
    }
}
