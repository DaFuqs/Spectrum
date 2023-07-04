package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GleamingPinItem extends SpectrumTrinketItem implements ExtendedEnchantable {
	
	public static final int BASE_RANGE = 12;
	public static final int RANGE_BONUS_PER_LEVEL_OF_SNIPING = 4;
	public static final int EFFECT_DURATION = 240;
	public static final long COOLDOWN_TICKS = 160;
	
	public GleamingPinItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/gleaming_pin"));
	}
	
	public static void doGleamingPinEffect(@NotNull PlayerEntity player, @NotNull ServerWorld world, ItemStack gleamingPinStack) {
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SpectrumSoundEvents.RADIANCE_PIN_TRIGGER, SoundCategory.PLAYERS, 0.4F, 0.9F + world.getRandom().nextFloat() * 0.2F);
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, player.getPos().add(0, 0.75, 0), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 100, new Vec3d(0, 0.5, 0), new Vec3d(2.5, 0.1, 2.5));
		
		world.getOtherEntities(player, player.getBoundingBox().expand(getEffectRange(gleamingPinStack)), EntityPredicates.VALID_LIVING_ENTITY).forEach((entity) -> {
			if (entity instanceof LivingEntity livingEntity) {
				livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, EFFECT_DURATION, 0, true, true));
			}
		});
	}
	
	public static int getEffectRange(ItemStack stack) {
		return BASE_RANGE + RANGE_BONUS_PER_LEVEL_OF_SNIPING * EnchantmentHelper.getLevel(SpectrumEnchantments.SNIPER, stack);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.gleaming_pin.tooltip"));
	}
	
	@Override
	public int getEnchantability() {
		return 16;
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == SpectrumEnchantments.SNIPER;
	}
}