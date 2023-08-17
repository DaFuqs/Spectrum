package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.interfaces.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionPendantItem extends SpectrumTrinketItem implements PotionFillable {
	
	private final static int TRIGGER_EVERY_X_TICKS = 300;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 220; // always keeps the effect active & prevents the 10 seconds of screen flashing when night vision runs out
	
	int maxEffectCount;
	int maxAmplifier;
	
	public PotionPendantItem(Settings settings, int maxEffectCount, int maxAmplifier, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
		this.maxEffectCount = maxEffectCount;
		this.maxAmplifier = maxAmplifier;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
		if (effects.size() > 0) {
			List<Pair<EntityAttribute, EntityAttributeModifier>> attributeModifiers = Lists.newArrayList();
			for (StatusEffectInstance effect : effects) {
				TranslatableText mutableText = new TranslatableText(effect.getTranslationKey());
				
				if (effect.getAmplifier() > 0) {
					mutableText = new TranslatableText("potion.withAmplifier", mutableText, new TranslatableText("potion.potency." + effect.getAmplifier()));
				}
				tooltip.add(mutableText.formatted(effect.getEffectType().getCategory().getFormatting()));
				
				Map<EntityAttribute, EntityAttributeModifier> map = effect.getEffectType().getAttributeModifiers();
				for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
					Map.Entry<EntityAttribute, EntityAttributeModifier> entry = entityAttributeEntityAttributeModifierEntry;
					EntityAttributeModifier entityAttributeModifier = entry.getValue();
					EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), effect.getEffectType().adjustModifierAmount(effect.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
					attributeModifiers.add(new Pair(entry.getKey(), entityAttributeModifier2));
				}
			}
			
			if (!attributeModifiers.isEmpty()) {
				tooltip.add(LiteralText.EMPTY);
				tooltip.add((new TranslatableText("potion.whenDrank")).formatted(Formatting.DARK_PURPLE));
				
				for (Pair<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierPair : attributeModifiers) {
					EntityAttributeModifier mutableText = entityAttributeEntityAttributeModifierPair.getSecond();
					double statusEffect = mutableText.getValue();
					double d;
					if (mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
						d = mutableText.getValue();
					} else {
						d = mutableText.getValue() * 100.0D;
					}
					
					if (statusEffect > 0.0D) {
						tooltip.add((new TranslatableText("attribute.modifier.plus." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), new TranslatableText((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.BLUE));
					} else if (statusEffect < 0.0D) {
						d *= -1.0D;
						tooltip.add((new TranslatableText("attribute.modifier.take." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), new TranslatableText((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.RED));
					}
				}
			}
		}
		
		int maxEffectCount = maxEffectCount();
		if (effects.size() < maxEffectCount) {
			if (maxEffectCount == 1) {
				tooltip.add(new TranslatableText("item.spectrum.potion_pendant.tooltip_not_full_one"));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.potion_pendant.tooltip_not_full_count", maxEffectCount));
			}
			tooltip.add(new TranslatableText("item.spectrum.potion_pendant.tooltip_max_level").append(new TranslatableText("enchantment.level." + (this.maxAmplifier + 1))));
		}
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || PotionUtil.getCustomPotionEffects(stack).size() > 0;
	}
	
	@Override
	public int maxEffectCount() {
		return maxEffectCount;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return maxAmplifier;
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if (!entity.getWorld().isClient && entity.getWorld().getTime() % TRIGGER_EVERY_X_TICKS == 0) {
			giveEffects(stack, entity);
		}
	}
	
	private void giveEffects(ItemStack stack, LivingEntity entity) {
		List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
		for (StatusEffectInstance effect : effects) {
			entity.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), EFFECT_DURATION, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), true));
		}
	}
	
}