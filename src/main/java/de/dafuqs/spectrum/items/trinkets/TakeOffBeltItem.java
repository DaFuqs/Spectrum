package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class TakeOffBeltItem extends SpectrumTrinketItem implements EnchanterEnchantable {
	
	public static final int CHARGE_TIME_TICKS = 20;
	public static final int MAX_CHARGES = 8;
	
	private static final HashMap<LivingEntity, Long> sneakingTimes = new HashMap<>();
	
	public TakeOffBeltItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_take_off_belt"));
	}
	
	public static int getJumpBoostAmplifier(int sneakTime, int powerEnchantmentLevel) {
		return (int) Math.floor(sneakTime * (1.5 + powerEnchantmentLevel * 0.25));
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
		tooltip.add(new TranslatableText("item.spectrum.take_off_belt.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if (!entity.getWorld().isClient) {
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
							for (Vec3d vec : Support.VECTORS_16) {
								SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 1, new Vec3d(0, 0, 0), vec.multiply(0.5));
							}
							
							int powerEnchantmentLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
							int featherFallingEnchantmentLevel = EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, stack);
							entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, CHARGE_TIME_TICKS, getJumpBoostAmplifier(sneakTimeMod, powerEnchantmentLevel), true, false, true));
							if (featherFallingEnchantmentLevel > 0) {
								entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, CHARGE_TIME_TICKS + featherFallingEnchantmentLevel * 20, 0, true, false, true));
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
	public int getEnchantability() {
		return 8;
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.POWER || enchantment == Enchantments.FEATHER_FALLING;
	}
}