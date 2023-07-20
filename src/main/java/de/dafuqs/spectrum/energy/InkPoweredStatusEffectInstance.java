package de.dafuqs.spectrum.energy;

import com.google.common.collect.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkPoweredStatusEffectInstance {
	
	private final StatusEffectInstance statusEffectInstance;
	private final InkCost cost;
	private final boolean unidentifiable;
	private final int customColor; // -1: use effect default
	
	public InkPoweredStatusEffectInstance(StatusEffectInstance statusEffectInstance, InkCost cost, int customColor, boolean unidentifiable) {
		this.statusEffectInstance = statusEffectInstance;
		this.cost = cost;
		this.customColor = customColor;
		this.unidentifiable = unidentifiable;
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
		if (customColor != -1) {
			nbt.putInt("CustomColor", this.customColor);
		}
		if (unidentifiable) {
			nbt.putBoolean("Unidentifiable", true);
		}
		return nbt;
	}
	
	public static InkPoweredStatusEffectInstance fromNbt(NbtCompound nbt) {
		StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbt);
		InkCost cost = InkCost.fromNbt(nbt);
		int customColor = -1;
		if (nbt.contains("CustomColor", NbtElement.INT_TYPE)) {
			customColor = nbt.getInt("CustomColor");
		}
		boolean unidentifiable = false;
		if (nbt.contains("Unidentifiable")) {
			unidentifiable = nbt.getBoolean("Unidentifiable");
		}
		return new InkPoweredStatusEffectInstance(statusEffectInstance, cost, customColor, unidentifiable);
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
				if (entry.isUnidentifiable()) {
					tooltip.add(Text.translatable("item.spectrum.potion.tooltip.unidentifiable"));
					continue;
				}
				
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
	
	public int getColor() {
		if (this.customColor == -1) {
			return statusEffectInstance.getEffectType().getColor();
		}
		return this.customColor;
	}
	
	public boolean isUnidentifiable() {
		return this.unidentifiable;
	}
	
}