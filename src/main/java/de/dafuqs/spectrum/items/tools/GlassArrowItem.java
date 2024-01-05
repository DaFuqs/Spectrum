package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassArrowItem extends ArrowItem {
	
	public final GlassArrowVariant variant;
	
	public GlassArrowItem(Settings settings, GlassArrowVariant variant, ParticleEffect particleEffect) {
		super(settings);
		this.variant = variant;
		variant.setData(this, particleEffect);
	}
	
	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		GlassArrowEntity entity = new GlassArrowEntity(world, shooter);
		entity.setVariant(variant);
		return entity;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		
		tooltip.add(Text.translatable("item.spectrum.glass_arrow.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.glass_arrow.tooltip2").formatted(Formatting.GRAY));
		if (variant != GlassArrowVariant.MALACHITE) {
			tooltip.add(Text.translatable(getTranslationKey() + ".tooltip").formatted(Formatting.GRAY));
		}
	}
	
}
