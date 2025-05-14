package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GleamingPinItem extends SpectrumTrinketItem {
	
	public static final int BASE_RANGE = 12;
	public static final int RANGE_BONUS_PER_LEVEL_OF_SNIPING = 4;
	public static final int EFFECT_DURATION = 240;
	public static final long COOLDOWN_TICKS = 160;
	
	public GleamingPinItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/gleaming_pin"));
	}
	
	public static void doGleamingPinEffect(@NotNull Player player, @NotNull ServerLevel world, ItemStack gleamingPinStack) {
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SpectrumSoundEvents.RADIANCE_PIN_TRIGGER, SoundSource.PLAYERS, 0.4F, 0.9F + world.getRandom().nextFloat() * 0.2F);
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, player.position().add(0, 0.75, 0), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 100, new Vec3(0, 0.5, 0), new Vec3(2.5, 0.1, 2.5));
		
		world.getEntities(player, player.getBoundingBox().inflate(getEffectRange(world, gleamingPinStack)), EntitySelector.LIVING_ENTITY_STILL_ALIVE).forEach((entity) -> {
			if (entity instanceof LivingEntity livingEntity) {
				livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, EFFECT_DURATION, 0, true, true));
			}
		});
	}
	
	public static int getEffectRange(ServerLevel world, ItemStack stack) {
		return BASE_RANGE + RANGE_BONUS_PER_LEVEL_OF_SNIPING * SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantments.SNIPING, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.gleaming_pin.tooltip"));
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public int getEnchantmentValue() {
		return 16;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(SpectrumEnchantments.SNIPING);
	}
	
}
