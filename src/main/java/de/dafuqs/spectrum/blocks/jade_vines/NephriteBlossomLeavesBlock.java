package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
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

public class NephriteBlossomLeavesBlock extends LeavesBlock implements Fertilizable {

    public static final MapCodec<NephriteBlossomLeavesBlock> CODEC = createCodec(NephriteBlossomLeavesBlock::new);

    public static final IntProperty AGE = Properties.AGE_2;
    public static final int MAX_AGE = Properties.AGE_2_MAX;

    public NephriteBlossomLeavesBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0));
    }

    @Override
    public MapCodec<? extends NephriteBlossomLeavesBlock> getCodec() {
        return CODEC;
    }
    
    @Override
    public ItemActionResult onUseWithItem(ItemStack handStack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == MAX_AGE) {
			int fortuneLevel = SpectrumEnchantmentHelper.getLevel(world.getRegistryManager(), Enchantments.FORTUNE, handStack) / 2;
			int count = 1 + world.getRandom().nextInt(fortuneLevel + 1);
			player.getInventory().offerOrDrop(new ItemStack(SpectrumItems.GLASS_PEACH, count));
	
			world.setBlockState(pos, state.with(AGE, 0));
			player.playSoundToPlayer(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1, 1 + player.getRandom().nextFloat() * 0.25F);
			return ItemActionResult.success(world.isClient());
		}
	
		return super.onUseWithItem(handStack, state, world, pos, player, hand, hit);
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
		} else {
			world.setBlockState(pos, state.with(AGE, age + 1));
		}
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
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
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
