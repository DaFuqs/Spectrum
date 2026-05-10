package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WhispyCircletItem extends SpectrumTrinketItem {
	
	private final static int TRIGGER_EVERY_X_TICKS = 100;
	private final static int NEGATIVE_EFFECT_SHORTENING_TICKS = 200;
	
	public WhispyCircletItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/whispy_circlet"));
	}
	
	public static void removeSingleStatusEffect(@NotNull LivingEntity entity, MobEffectCategory category) {
		Collection<MobEffectInstance> currentEffects = entity.getActiveEffects();
		if (currentEffects.isEmpty()) {
			return;
		}
		
		List<MobEffectInstance> negativeEffects = new ArrayList<>();
		for (MobEffectInstance statusEffectInstance : currentEffects) {
			Holder<MobEffect> effect = statusEffectInstance.getEffect();
			if (effect.value().getCategory() == category && !effect.is(SpectrumStatusEffectTags.SOPORIFIC) && !effect.is(SpectrumStatusEffectTags.BYPASSES_WHISPY_CIRCLET)) {
				negativeEffects.add(statusEffectInstance);
			}
		}
		
		if (negativeEffects.isEmpty()) {
			return;
		}
		
		Level world = entity.level();
		int randomIndex = world.getRandom().nextInt(negativeEffects.size());
		entity.removeEffect(negativeEffects.get(randomIndex).getEffect());
	}
	
	public static void removeNegativeStatusEffects(@NotNull LivingEntity entity) {
		Set<Holder<MobEffect>> effectsToRemove = new HashSet<>();
		for (var instance : entity.getActiveEffects()) {
			if (affects(instance.getEffect())) {
				effectsToRemove.add(instance.getEffect());
			}
		}
		
		for (Holder<MobEffect> effect : effectsToRemove) {
			entity.removeEffect(effect);
		}
	}
	
	public static void shortenNegativeStatusEffects(@NotNull LivingEntity entity, int duration) {
		Collection<MobEffectInstance> newEffects = new ArrayList<>();
		Collection<Holder<MobEffect>> effectTypesToClear = new ArrayList<>();
		
		// remove them first, so hidden "stacked" effects are preserved
		for (MobEffectInstance instance : entity.getActiveEffects()) {
			if (affects(instance.getEffect())) {
				int newDurationTicks = instance.getDuration() - duration;
				if (newDurationTicks > 0) {
					newEffects.add(new MobEffectInstance(instance.getEffect(), newDurationTicks, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()));
				}
				if (!effectTypesToClear.contains(instance.getEffect())) {
					effectTypesToClear.add(instance.getEffect());
				}
			}
		}
		
		for (var effectTypeToClear : effectTypesToClear) {
			entity.removeEffect(effectTypeToClear);
		}
		for (MobEffectInstance newEffect : newEffects) {
			entity.addEffect(newEffect);
		}
	}
	
	public static boolean affects(Holder<MobEffect> effect) {
		return effect.value().getCategory() == MobEffectCategory.HARMFUL && !effect.is(SpectrumStatusEffectTags.BYPASSES_WHISPY_CIRCLET);
	}
	
	public static void preventPhantomSpawns(@NotNull ServerPlayer serverPlayerEntity) {
		serverPlayerEntity.getStats().setValue(serverPlayerEntity, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip3").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		Level world = entity.level();
		if (!world.isClientSide()) {
			long time = entity.level().getGameTime();
			if (time % TRIGGER_EVERY_X_TICKS == 0) {
				shortenNegativeStatusEffects(entity, NEGATIVE_EFFECT_SHORTENING_TICKS);
			}
			if (time % 10000 == 0 && entity instanceof ServerPlayer serverPlayerEntity) {
				preventPhantomSpawns(serverPlayerEntity);
			}
		}
	}
	
	public static final ResourceLocation ATTRIBUTE_ID = SpectrumCommon.locate("whispy_circlet_mental_presence");
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new AttributeModifier(ATTRIBUTE_ID, 0.3, AttributeModifier.Operation.ADD_VALUE));
		return modifiers;
	}

}
