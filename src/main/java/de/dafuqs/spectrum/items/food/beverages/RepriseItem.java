package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class RepriseItem extends BeverageItem {
	
	public RepriseItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return new RepriseProperties(itemStack.getNbt());
	}
	
	public static class RepriseProperties extends BeverageProperties {
		
		public RepriseProperties(NbtCompound nbtCompound) {
			super(nbtCompound);
		}
		
		@Override
		public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
			super.addTooltip(itemStack, tooltip);
			long teleportRange = getTeleportRange(itemStack);
			tooltip.add(Text.translatable("item.spectrum.reprise.tooltip.teleport", teleportRange).formatted(Formatting.GRAY));
		}
		
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		randomTeleport(world, user, getTeleportRange(stack));
		return super.finishUsing(stack, world, user);
	}

	public static long getTeleportRange(ItemStack itemStack) {
		BeverageProperties properties = BeverageProperties.getFromStack(itemStack);
		return (long) Math.ceil(Math.pow(2, properties.alcPercent));
	}

	public void randomTeleport(World world, LivingEntity user, long maxRange) {
		if (!world.isClient) {
			double d = user.getX();
			double e = user.getY();
			double f = user.getZ();

			for (int i = 0; i < 16; ++i) {
				double newX = user.getX() + (user.getRandom().nextDouble() - 0.5D) * maxRange;
				double newY = MathHelper.clamp(user.getY() + (double) (user.getRandom().nextFloat() * 2 * maxRange) - maxRange, world.getBottomY(), (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1));
				double newZ = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * maxRange;
				if (user.hasVehicle()) {
					user.stopRiding();
				}

				if (user.teleport(newX, newY, newZ, true)) {
					SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
					world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
					user.playSound(soundEvent, 1.0F, 1.0F);
					break;
				}
			}
			
			if (user instanceof PlayerEntity) {
				((PlayerEntity) user).getItemCooldownManager().set(this, 20);
			}
		}
	}
	
}
