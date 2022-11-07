package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.BeverageProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class RepriseItem extends BeverageItem {
	
	public RepriseItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return new RepriseProperties(itemStack.getNbt());
	}
	
	public static class RepriseProperties extends BeverageProperties {
		
		public RepriseProperties(NbtCompound nbtCompound) {
			super(nbtCompound);
		}
		
		public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
			super.addTooltip(itemStack, tooltip);
			int teleportRange = getTeleportRange(itemStack);
			tooltip.add(new TranslatableText("item.spectrum.reprise.tooltip.teleport", teleportRange).formatted(Formatting.GRAY));
		}
		
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		randomTeleport(world, user, getTeleportRange(stack));
		return super.finishUsing(stack, world, user);
	}
	
	public static int getTeleportRange(ItemStack itemStack) {
		BeverageProperties properties = BeverageProperties.getFromStack(itemStack);
		return (int) Math.ceil(Math.pow(1.4, properties.alcPercent));
	}
	
	public void randomTeleport(World world, LivingEntity user, int maxRange) {
		if (!world.isClient) {
			double d = user.getX();
			double e = user.getY();
			double f = user.getZ();
			
			for(int i = 0; i < 16; ++i) {
				double g = user.getX() + (user.getRandom().nextDouble() - 0.5D) * maxRange;
				double h = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(maxRange) - maxRange / 2), world.getBottomY(), (world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
				double j = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * maxRange;
				if (user.hasVehicle()) {
					user.stopRiding();
				}
				
				if (user.teleport(g, h, j, true)) {
					SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
					world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
					user.playSound(soundEvent, 1.0F, 1.0F);
					break;
				}
			}
			
			if (user instanceof PlayerEntity) {
				((PlayerEntity)user).getItemCooldownManager().set(this, 20);
			}
		}
	}
	
}
