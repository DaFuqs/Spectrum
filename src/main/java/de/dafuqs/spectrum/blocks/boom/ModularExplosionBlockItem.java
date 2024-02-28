package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.explosion.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ModularExplosionBlockItem extends BlockItem implements ModularExplosionProvider {
	
	private final int maxModifierCount;
	private final double baseBlastRadius;
	private final float baseDamage;
	
	public ModularExplosionBlockItem(Block block, double baseBlastRadius, float baseDamage, int maxModifierCount, Settings settings) {
		super(block, settings);
		this.maxModifierCount = maxModifierCount;
		this.baseBlastRadius = baseBlastRadius;
		this.baseDamage = baseDamage;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
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
