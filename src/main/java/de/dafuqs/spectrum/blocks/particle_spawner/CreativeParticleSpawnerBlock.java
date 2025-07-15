package de.dafuqs.spectrum.blocks.particle_spawner;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class CreativeParticleSpawnerBlock extends AbstractParticleSpawnerBlock implements CreativeOnlyItem {
	
	public static final MapCodec<CreativeParticleSpawnerBlock> CODEC = simpleCodec(CreativeParticleSpawnerBlock::new);
	
	public CreativeParticleSpawnerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CreativeParticleSpawnerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.creative_particle_spawner.tooltip").withStyle(ChatFormatting.GRAY));
		CreativeOnlyItem.appendTooltip(tooltip);
	}
	
	@Override
	public boolean shouldSpawnParticles(Level world, BlockPos pos) {
		return true;
	}
	
}
