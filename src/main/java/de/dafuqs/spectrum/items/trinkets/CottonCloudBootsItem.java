package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class CottonCloudBootsItem extends SpectrumTrinketItem {
	
	public CottonCloudBootsItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/cotton_cloud_boots"));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		Level level = entity.level();
		if (entity.isSprinting() && !entity.onGround() && !entity.isShiftKeyDown()) {
			Vec3 deltaMovement = entity.getDeltaMovement();
			if (deltaMovement.y < 0) {
				entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0.1, 1));
				if (level.isClientSide()) {
					RandomSource random = level.getRandom();
					level.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(),
							0.125 - random.nextFloat() * 0.25, 0.04 - random.nextFloat() * 0.08, 0.125 - random.nextFloat() * 0.25);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.cotton_cloud_boots.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.cotton_cloud_boots.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
}
