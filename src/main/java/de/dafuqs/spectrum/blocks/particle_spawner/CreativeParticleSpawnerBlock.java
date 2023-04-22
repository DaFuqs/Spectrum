package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.items.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CreativeParticleSpawnerBlock extends AbstractParticleSpawnerBlock implements CreativeOnlyItem {
	
	public CreativeParticleSpawnerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.creative_particle_spawner.tooltip").formatted(Formatting.GRAY));
		CreativeOnlyItem.appendTooltip(tooltip);
	}
	
	@Override
	public boolean shouldSpawnParticles(World world, BlockPos pos) {
		return true;
	}
	
}
