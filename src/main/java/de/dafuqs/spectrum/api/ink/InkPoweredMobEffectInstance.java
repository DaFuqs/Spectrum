package de.dafuqs.spectrum.api.ink;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.*;

public record InkPoweredMobEffectInstance(MobEffectInstance statusEffectInstance, InkAmount cost, Optional<Integer> customColor, boolean unidentifiable) {
	
	public static final Codec<InkPoweredMobEffectInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			MobEffectInstance.CODEC.fieldOf("effect").forGetter(c -> c.statusEffectInstance),
			InkAmount.CODEC.fieldOf("ink_cost").forGetter(c -> c.cost),
			Codec.INT.optionalFieldOf("custom_color").forGetter(c -> c.customColor),
			Codec.BOOL.optionalFieldOf("unidentifiable", false).forGetter(c -> c.unidentifiable)
	).apply(i, InkPoweredMobEffectInstance::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InkPoweredMobEffectInstance> PACKET_CODEC = StreamCodec.composite(
			MobEffectInstance.STREAM_CODEC, c -> c.statusEffectInstance,
			InkAmount.STREAM_CODEC, c -> c.cost,
			ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), c -> c.customColor,
			ByteBufCodecs.BOOL, c -> c.unidentifiable,
			InkPoweredMobEffectInstance::new
	);
	
	public MobEffectInstance getStatusEffectInstance() {
		return statusEffectInstance;
	}
	
	public InkAmount getInkCost() {
		return cost;
	}
	
	public int getColor() {
		if(this.customColor.isPresent()) {
			return this.customColor.get();
		}
		return statusEffectInstance.getEffect().value().getColor();
	}
	
	public static void buildTooltip(List<Component> tooltip, List<InkPoweredMobEffectInstance> effects, MutableComponent attributeModifierText, boolean showDuration, float tickRate) {
		if (!effects.isEmpty()) {
			List<Tuple<Holder<Attribute>, AttributeModifier>> attributeModifiers = Lists.newArrayList();
			for (InkPoweredMobEffectInstance entry : effects) {
				MobEffectInstance effect = entry.getStatusEffectInstance();
				InkAmount cost = entry.getInkCost();
				
				MutableComponent mutableText = Component.translatable(effect.getDescriptionId());
				if (effect.getAmplifier() > 0) {
					mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + effect.getAmplifier()));
				}
				if (showDuration && effect.getDuration() > 20) {
					mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(effect, 1.0F, tickRate));
				}
				mutableText.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting());
				mutableText.append(Component.translatable("spectrum.tooltip.ink_cost", Support.getShortenedNumberString(cost.amount()), cost.color().getColoredInkName()).withStyle(ChatFormatting.GRAY));
				tooltip.add(mutableText);
				
				effect.getEffect().value().createModifiers(effect.getAmplifier(), (attribute, modifier) ->
					attributeModifiers.add(new Tuple<>(attribute, modifier))
				);
			}
			
			if (!attributeModifiers.isEmpty()) {
				tooltip.add(Component.empty());
				tooltip.add(attributeModifierText.withStyle(ChatFormatting.DARK_PURPLE));
				
				for (var pair : attributeModifiers) {
					var translatedAttribute = Component.translatable(pair.getA().value().getDescriptionId());
					var mutableText = pair.getB();
					
					double statusEffect = mutableText.amount();
					double d;
					if (mutableText.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && mutableText.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
						d = mutableText.amount();
					} else {
						d = mutableText.amount() * 100.0D;
					}
					
					if (statusEffect > 0.0D) {
						tooltip.add((Component.translatable("attribute.modifier.plus." + mutableText.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d), translatedAttribute)).withStyle(ChatFormatting.BLUE));
					} else if (statusEffect < 0.0D) {
						d *= -1.0D;
						tooltip.add((Component.translatable("attribute.modifier.take." + mutableText.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d), translatedAttribute)).withStyle(ChatFormatting.RED));
					}
				}
			}
		}
	}
	
}
