package de.dafuqs.spectrum.energy;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InkPoweredStatusEffectInstance {
	
	private final StatusEffectInstance statusEffectInstance;
	private final InkCost cost;
	
	public InkPoweredStatusEffectInstance(StatusEffectInstance statusEffectInstance, InkCost cost) {
		this.statusEffectInstance = statusEffectInstance;
		this.cost = cost;
	}
	
	public StatusEffectInstance getStatusEffectInstance() {
		return statusEffectInstance;
	}
	
	public InkCost getInkCost() {
		return cost;
	}
	
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		this.statusEffectInstance.writeNbt(nbt);
		this.cost.writeNbt(nbt);
		return nbt;
	}
	
	public static InkPoweredStatusEffectInstance fromNbt(NbtCompound nbt) {
		StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbt);
		InkCost cost = InkCost.fromNbt(nbt);
		return new InkPoweredStatusEffectInstance(statusEffectInstance, cost);
	}
	
	public static List<InkPoweredStatusEffectInstance> getEffects(ItemStack stack) {
		return getEffects(stack.getNbt());
	}
	
	public static List<InkPoweredStatusEffectInstance> getEffects(@Nullable NbtCompound nbt) {
		List<InkPoweredStatusEffectInstance> list = new ArrayList<>();
		if (nbt != null && nbt.contains("InkPoweredStatusEffects", NbtElement.LIST_TYPE)) {
			NbtList nbtList = nbt.getList("InkPoweredStatusEffects", NbtElement.COMPOUND_TYPE);
			
			for (int i = 0; i < nbtList.size(); ++i) {
				NbtCompound nbtCompound = nbtList.getCompound(i);
				InkPoweredStatusEffectInstance instance = InkPoweredStatusEffectInstance.fromNbt(nbtCompound);
				list.add(instance);
			}
		}
		return list;
	}
	
	public static void setEffects(ItemStack stack, Collection<InkPoweredStatusEffectInstance> effects) {
		if (!effects.isEmpty()) {
			NbtCompound nbtCompound = stack.getOrCreateNbt();
			NbtList nbtList = nbtCompound.getList("InkPoweredStatusEffects", NbtElement.LIST_TYPE);
			
			for (InkPoweredStatusEffectInstance effect : effects) {
				nbtList.add(effect.toNbt());
			}
			
			nbtCompound.put("InkPoweredStatusEffects", nbtList);
		}
	}
	
	public static void buildTooltip(List<Text> tooltip, List<InkPoweredStatusEffectInstance> effects, MutableText attributeModifierText, boolean showDuration) {
		if (effects.size() > 0) {
			List<Pair<EntityAttribute, EntityAttributeModifier>> attributeModifiers = Lists.newArrayList();
			for (InkPoweredStatusEffectInstance entry : effects) {
				StatusEffectInstance effect = entry.getStatusEffectInstance();
				InkCost cost = entry.getInkCost();
				
				MutableText mutableText = Text.translatable(effect.getTranslationKey());
				if (effect.getAmplifier() > 0) {
					mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + effect.getAmplifier()));
				}
				if (showDuration && effect.getDuration() > 20) {
					mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.durationToString(effect, 1.0F));
				}
				mutableText.formatted(effect.getEffectType().getCategory().getFormatting());
				mutableText.append(Text.translatable("spectrum.tooltip.ink_cost." + cost.getColor().toString().toLowerCase(Locale.ROOT), Support.getShortenedNumberString(cost.getCost())).formatted(Formatting.GRAY));
				tooltip.add(mutableText);
				
				Map<EntityAttribute, EntityAttributeModifier> map = effect.getEffectType().getAttributeModifiers();
				for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
					EntityAttributeModifier entityAttributeModifier = entityAttributeEntityAttributeModifierEntry.getValue();
					EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), effect.getEffectType().adjustModifierAmount(effect.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
					attributeModifiers.add(new Pair<>(entityAttributeEntityAttributeModifierEntry.getKey(), entityAttributeModifier2));
				}
			}
			
			if (!attributeModifiers.isEmpty()) {
				tooltip.add(Text.empty());
				tooltip.add(attributeModifierText.formatted(Formatting.DARK_PURPLE));
				
				for (Pair<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierPair : attributeModifiers) {
					EntityAttributeModifier mutableText = entityAttributeEntityAttributeModifierPair.getRight();
					double statusEffect = mutableText.getValue();
					double d;
					if (mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
						d = mutableText.getValue();
					} else {
						d = mutableText.getValue() * 100.0D;
					}
					
					if (statusEffect > 0.0D) {
						tooltip.add((Text.translatable("attribute.modifier.plus." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), Text.translatable((entityAttributeEntityAttributeModifierPair.getLeft()).getTranslationKey()))).formatted(Formatting.BLUE));
					} else if (statusEffect < 0.0D) {
						d *= -1.0D;
						tooltip.add((Text.translatable("attribute.modifier.take." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), Text.translatable((entityAttributeEntityAttributeModifierPair.getLeft()).getTranslationKey()))).formatted(Formatting.RED));
					}
				}
			}
		}
	}
	
}