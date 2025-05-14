package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class TakeOffBeltItem extends SpectrumTrinketItem {
	
	public static final int CHARGE_TIME_TICKS = 20;
	public static final int MAX_CHARGES = 8;
	
	private static final HashMap<LivingEntity, Long> sneakingTimes = new HashMap<>();
	
	public TakeOffBeltItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/take_off_belt"));
	}
	
	public static int getJumpBoostAmplifier(int sneakTime, int powerEnchantmentLevel) {
		return (int) Math.floor(sneakTime * (2.0 + powerEnchantmentLevel * 0.5));
	}
	
	public static int getCurrentCharge(Player playerEntity) {
		if (sneakingTimes.containsKey(playerEntity)) {
			return (int) (playerEntity.level().getGameTime() - sneakingTimes.get(playerEntity)) / CHARGE_TIME_TICKS;
		}
		return 0;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.take_off_belt.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		Level world = entity.level();
		super.tick(stack, slot, entity);
		
		if (!world.isClientSide) {
			if (entity.isShiftKeyDown() && entity.onGround()) {
				if (sneakingTimes.containsKey(entity)) {
					long sneakTicks = world.getGameTime() - sneakingTimes.get(entity);
					if (sneakTicks % CHARGE_TIME_TICKS == 0) {
						if (sneakTicks > CHARGE_TIME_TICKS * MAX_CHARGES) {
							world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundSource.NEUTRAL, 4.0F, 1.05F);
							PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, entity.position(), ColoredCraftingParticleEffect.BLACK, 20, new Vec3(0, 0, 0), new Vec3(0.1, 0.05, 0.1));
							entity.removeEffect(MobEffects.JUMP);
						} else {
							int sneakTimeMod = (int) sneakTicks / CHARGE_TIME_TICKS;
							
							world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
							for (Vec3 vec : VectorPattern.SIXTEEN.getVectors()) {
								PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity((ServerLevel) world, entity.position(), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 1, vec.scale(0.5));
							}
							
							int powerEnchantmentLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.POWER, stack);
							int featherFallingEnchantmentLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.FEATHER_FALLING, stack);
							entity.addEffect(new MobEffectInstance(MobEffects.JUMP, CHARGE_TIME_TICKS, getJumpBoostAmplifier(sneakTimeMod, powerEnchantmentLevel), true, true));
							if (featherFallingEnchantmentLevel > 0) {
								entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, CHARGE_TIME_TICKS + featherFallingEnchantmentLevel * 20, 0, true, true));
							}
						}
					}
				} else {
					sneakingTimes.put(entity, world.getGameTime());
					if (entity instanceof ServerPlayer serverPlayerEntity) {
						PlayTakeOffBeltSoundInstancePayload.sendPlayTakeOffBeltSoundInstance(serverPlayerEntity);
					}
				}
			} else if (world.getGameTime() % CHARGE_TIME_TICKS == 0 && sneakingTimes.containsKey(entity)) {
				long lastSneakingTime = sneakingTimes.get(entity);
				if (lastSneakingTime < world.getGameTime() + CHARGE_TIME_TICKS) {
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
	public int getEnchantmentValue() {
		return 8;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.POWER) || enchantment.is(Enchantments.FEATHER_FALLING);
	}
	
}
