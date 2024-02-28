package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GemstoneArmorItem extends ArmorItem implements ArmorWithHitEffect {
	
	public GemstoneArmorItem(ArmorMaterial material, ArmorItem.Type type, Settings settings) {
		super(material, type, settings);
	}
	
	@Override
	public void onHit(ItemStack itemStack, DamageSource source, LivingEntity targetEntity, float amount) {
		// While mostly useful against mobs, being able to trigger this effect for all kinds of damage
		// like fall damage seems like an awesome mechanic
		process(type, source, targetEntity);
		targetEntity.getWorld().playSound(null, targetEntity.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		targetEntity.getWorld().playSound(null, targetEntity.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
		
		itemStack.damage(2, targetEntity, (e) -> e.sendEquipmentBreakStatus(type.getEquipmentSlot()));
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		addTooltip(tooltip, type);
	}
	
	private void process(@NotNull ArmorItem.Type type, DamageSource source, LivingEntity targetEntity) {
		switch (type) {
			case HELMET -> {
				if (source.getAttacker() instanceof LivingEntity) {
					StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.WEAKNESS, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorWeaknessAmplifier);
					((LivingEntity) source.getAttacker()).addStatusEffect(statusEffectInstance);
					statusEffectInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorSlownessAmplifier);
					((LivingEntity) source.getAttacker()).addStatusEffect(statusEffectInstance);
				}
			}
			case CHESTPLATE -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.ABSORPTION, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorAbsorptionAmplifier);
				targetEntity.addStatusEffect(statusEffectInstance);
				statusEffectInstance = new StatusEffectInstance(StatusEffects.RESISTANCE, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorResistanceAmplifier);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
			case LEGGINGS -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorRegenerationAmplifier);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
			case BOOTS -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SPEED, 5 * 20, SpectrumCommon.CONFIG.GemstoneArmorSpeedAmplifier);
				targetEntity.addStatusEffect(statusEffectInstance);
				statusEffectInstance = new StatusEffectInstance(StatusEffects.INVISIBILITY, 5 * 20, 0);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
		}
	}
	
	public void addTooltip(List<Text> tooltip, @NotNull ArmorItem.Type equipmentSlot) {
		switch (equipmentSlot) {
			case HELMET ->
					tooltip.add(Text.translatable("item.spectrum.fetchling_helmet.tooltip").formatted(Formatting.GRAY));
			case CHESTPLATE ->
					tooltip.add(Text.translatable("item.spectrum.ferocious_chestplate.tooltip").formatted(Formatting.GRAY));
			case LEGGINGS ->
					tooltip.add(Text.translatable("item.spectrum.sylph_leggings.tooltip").formatted(Formatting.GRAY));
			case BOOTS ->
					tooltip.add(Text.translatable("item.spectrum.oread_boots.tooltip").formatted(Formatting.GRAY));
		}
	}
	
}
