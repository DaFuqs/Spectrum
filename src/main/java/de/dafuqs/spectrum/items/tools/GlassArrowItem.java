package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassArrowItem extends ArrowItem {
	
	public final GlassArrowVariant variant;
	
	public GlassArrowItem(Item.Properties settings, GlassArrowVariant variant, ParticleOptions particleEffect) {
		super(settings);
		this.variant = variant;
		variant.setData(this, particleEffect);
	}
	
	@Override
	public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
		GlassArrowEntity entity = new GlassArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
		entity.setVariant(variant);
		return entity;
	}
	
	@Override
	public AbstractArrow asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
		GlassArrowEntity arrowEntity = new GlassArrowEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
		arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
		arrowEntity.setVariant(variant);
		return arrowEntity;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.glass_arrow.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.glass_arrow.tooltip2").withStyle(ChatFormatting.GRAY));
		if (variant != GlassArrowVariant.MALACHITE) {
			tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
		}
	}
	
}
