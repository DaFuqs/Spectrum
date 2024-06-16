package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TakeOffBeltItem extends SpectrumTrinketItem implements ExtendedEnchantable {
	
	public static final int CHARGE_TIME_TICKS = 20;
	public static final int MAX_CHARGES = 8;
	
	private static final HashMap<LivingEntity, Long> sneakingTimes = new HashMap<>();
	
	public TakeOffBeltItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/take_off_belt"));
	}
	
	public static int getJumpBoostAmplifier(int sneakTime, int powerEnchantmentLevel) {
		return (int) Math.floor(sneakTime * (2.0 + powerEnchantmentLevel * 0.5));
	}
	
	public static int getCurrentCharge(PlayerEntity playerEntity) {
		if (sneakingTimes.containsKey(playerEntity)) {
			return (int) (playerEntity.getWorld().getTime() - sneakingTimes.get(playerEntity)) / CHARGE_TIME_TICKS;
		}
		return 0;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.take_off_belt.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		World world = entity.getWorld();
		super.tick(stack, slot, entity);
		
		if (!world.isClient) {
			if (entity.isSneaking() && entity.isOnGround()) {
				if (sneakingTimes.containsKey(entity)) {
					long sneakTicks = entity.getWorld().getTime() - sneakingTimes.get(entity);
					if (sneakTicks % CHARGE_TIME_TICKS == 0) {
						if (sneakTicks > CHARGE_TIME_TICKS * MAX_CHARGES) {
							entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 4.0F, 1.05F);
							SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), SpectrumParticleTypes.BLACK_CRAFTING, 20, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.05, 0.1));
							entity.removeStatusEffect(StatusEffects.JUMP_BOOST);
						} else {
							int sneakTimeMod = (int) sneakTicks / CHARGE_TIME_TICKS;
							
							entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
							for (Vec3d vec : VectorPattern.SIXTEEN.getVectors()) {
								SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) entity.getWorld(), entity.getPos(), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 1, vec.multiply(0.5));
							}
							
							int powerEnchantmentLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
							int featherFallingEnchantmentLevel = EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, stack);
							entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, CHARGE_TIME_TICKS, getJumpBoostAmplifier(sneakTimeMod, powerEnchantmentLevel), true, true));
							if (featherFallingEnchantmentLevel > 0) {
								entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, CHARGE_TIME_TICKS + featherFallingEnchantmentLevel * 20, 0, true, true));
							}
						}
					}
				} else {
					sneakingTimes.put(entity, entity.getWorld().getTime());
					if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
						SpectrumS2CPacketSender.sendPlayTakeOffBeltSoundInstance(serverPlayerEntity);
					}
				}
			} else if (entity.getWorld().getTime() % CHARGE_TIME_TICKS == 0 && sneakingTimes.containsKey(entity)) {
				long lastSneakingTime = sneakingTimes.get(entity);
				if (lastSneakingTime < entity.getWorld().getTime() + CHARGE_TIME_TICKS) {
					sneakingTimes.remove(entity);
				}
			}
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	
	@Override
	public int getEnchantability() {
		return 8;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.POWER || enchantment == Enchantments.FEATHER_FALLING;
	}
	
}
