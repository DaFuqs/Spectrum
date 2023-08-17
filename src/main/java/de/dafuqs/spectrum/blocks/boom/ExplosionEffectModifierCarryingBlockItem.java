package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionEffectModifierCarryingBlockItem extends BlockItemWithTooltip implements ExplosionArchetypeProvider {
	
	private final ExplosionArchetype archetype;
	
	public ExplosionEffectModifierCarryingBlockItem(Block block, ExplosionArchetype archetype, Settings settings, String[] tooltips) {
		super(block, settings, tooltips);
		this.archetype = archetype;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		var nbt = stack.getOrCreateNbt();
		if (nbt.contains("outputDisplay")) {
			tooltip.add(SpectrumRegistries.EXPLOSION_EFFECT_MODIFIERS.get(Identifier.tryParse(nbt.getString("outputDisplay"))).getName());
		}
		
		ExplosionModifierSet set = ExplosionModifierSet.fromNbt(stack.getNbt());
		set.appendTooltip(tooltip);
	}
	
	@Override
	public ExplosionArchetype getArchetype() {
		return archetype;
	}
    
}
