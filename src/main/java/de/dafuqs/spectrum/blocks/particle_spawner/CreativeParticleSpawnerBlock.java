package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CreativeParticleSpawnerBlock extends ParticleSpawnerBlock implements CreativeOnlyItem {
	
	public CreativeParticleSpawnerBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(POWERED, true));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		CreativeOnlyItem.appendTooltip(tooltip);
		tooltip.add(Text.translatable("block.spectrum.creative_particle_spawner.tooltip"));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CreativeParticleSpawnerBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntities.CREATIVE_PARTICLE_SPAWNER, ParticleSpawnerBlockEntity::clientTick) : null;
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
	
	}
	
}
