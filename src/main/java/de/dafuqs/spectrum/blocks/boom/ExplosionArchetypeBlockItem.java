package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionArchetypeBlockItem extends BlockItem implements ExplosionArchetypeProvider {
	
	private final ExplosionArchetype archetype;
	private final int maxModifierCount;
	
	public ExplosionArchetypeBlockItem(Block block, ExplosionArchetype archetype, int maxModifierCount, Settings settings) {
		super(block, settings);
		this.archetype = archetype;
		this.maxModifierCount = maxModifierCount;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		ExplosionModifierSet.fromNbt(stack.getNbt()).appendTooltip(tooltip, this);
	}
	
	@Override
	public ExplosionArchetype getArchetype() {
		return archetype;
	}
	
	@Override
	public int getMaxModifierCount() {
		return maxModifierCount;
	}
	
}
