package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AshenCircletItem extends SpectrumTrinketItem {
	
	public static final int FIRE_RESISTANCE_EFFECT_DURATION = 600;
	public static final long COOLDOWN_TICKS = 3000;
	
	public static final double LAVA_MOVEMENT_SPEED_MOD = 0.4; // vanilla uses 0.5 to slow the player down to half its speed
	public static final double LAVA_VIEW_DISTANCE_MOD = 24.0;
	
	public AshenCircletItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/ashen_circlet"));
	}
	
	public static long getCooldownTicks(@NotNull ItemStack ashenCircletStack, @NotNull Level world) {
		var last = ashenCircletStack.getOrDefault(SpectrumDataComponentTypes.LAST_COOLDOWN_START, 0L);
		return Math.max(0, last + COOLDOWN_TICKS - world.getGameTime());
	}
	
	private static void setCooldown(@NotNull ItemStack ashenCircletStack, @NotNull Level world) {
		ashenCircletStack.set(SpectrumDataComponentTypes.LAST_COOLDOWN_START, world.getGameTime());
	}
	
	public static void grantFireResistance(@NotNull ItemStack ashenCircletStack, @NotNull LivingEntity livingEntity) {
		if (!livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FIRE_RESISTANCE_EFFECT_DURATION, 0, true, true));
			livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
			setCooldown(ashenCircletStack, livingEntity.level());
		}
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		if (entity.isOnFire()) {
			entity.setRemainingFireTicks(0);
		}
		if (getCooldownTicks(stack, entity.level()) == 0 && OnPrimordialFireComponent.putOut(entity)) {
			entity.level().playSound(null, entity.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
			setCooldown(stack, entity.level());
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.ashen_circlet.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.ashen_circlet.tooltip2").withStyle(ChatFormatting.GRAY));
		
		var world = Minecraft.getInstance().level;
		if (world != null) {
			long cooldownTicks = getCooldownTicks(stack, world);
			if (cooldownTicks == 0) {
				tooltip.add(Component.translatable("item.spectrum.ashen_circlet.tooltip.cooldown_full"));
			} else {
				tooltip.add(Component.translatable("item.spectrum.ashen_circlet.tooltip.cooldown_seconds", cooldownTicks / 20));
			}
		}
	}
	
	public static ResourceLocation LAVA_SPEED_ATTRIBUTE_ID = SpectrumCommon.locate("ashen_circlet_lava_speed");
	public static ResourceLocation LAVA_VISIBILITY_ATTRIBUTE_ID = SpectrumCommon.locate("ashen_circlet_lava_visibility");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		
		modifiers.put(AdditionalEntityAttributes.LAVA_SPEED, new AttributeModifier(LAVA_SPEED_ATTRIBUTE_ID, LAVA_MOVEMENT_SPEED_MOD, AttributeModifier.Operation.ADD_VALUE));
		modifiers.put(AdditionalEntityAttributes.LAVA_VISIBILITY, new AttributeModifier(LAVA_VISIBILITY_ATTRIBUTE_ID, LAVA_VIEW_DISTANCE_MOD, AttributeModifier.Operation.ADD_VALUE));
		
		return modifiers;
	}
	
}
