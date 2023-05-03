package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CottonCloudBootsItem extends SpectrumTrinketItem {
	
	public CottonCloudBootsItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/cotton_cloud_boots"));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		if (entity.isSprinting()) {
			entity.setNoGravity(true);
			if (entity.world.isClient) {
				Random random = entity.world.random;
				entity.world.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(),
						0.125 - random.nextFloat() * 0.25, 0.04 - random.nextFloat() * 0.08, 0.125 - random.nextFloat() * 0.25);
			}
		} else {
			entity.setNoGravity(false);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.cotton_cloud_boots.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.cotton_cloud_boots.tooltip2").formatted(Formatting.GRAY));
	}
	
}