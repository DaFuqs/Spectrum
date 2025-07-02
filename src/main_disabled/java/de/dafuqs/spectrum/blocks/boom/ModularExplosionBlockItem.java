package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.explosion.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ModularExplosionBlockItem extends BlockItem implements ModularExplosionProvider {
	
	private final int maxModifierCount;
	private final double baseBlastRadius;
	private final float baseDamage;
	
	public ModularExplosionBlockItem(Block block, double baseBlastRadius, float baseDamage, int maxModifierCount, Item.Properties properties) {
		super(block, properties);
		this.maxModifierCount = maxModifierCount;
		this.baseBlastRadius = baseBlastRadius;
		this.baseDamage = baseDamage;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		ModularExplosionDefinition.getFromStack(stack).appendTooltip(tooltip, this);
	}
	
	@Override
	public double getBaseExplosionBlastRadius() {
		return baseBlastRadius;
	}
	
	@Override
	public float getBaseExplosionDamage() {
		return baseDamage;
	}
	
	@Override
	public int getMaxExplosionModifiers() {
		return maxModifierCount;
	}
	
}
