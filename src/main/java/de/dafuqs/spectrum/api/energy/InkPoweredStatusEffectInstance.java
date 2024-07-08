package de.dafuqs.spectrum.api.energy;

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
	
	public static final String NBT_KEY = "InkPoweredStatusEffects";
	public static final String UNIDENTIFIABLE_NBT_KEY = "Unidentifiable";
	public static final String CUSTOM_COLOR_NBT_KEY = "CustomColor";

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
			nbt.putInt(CUSTOM_COLOR_NBT_KEY, this.customColor);
		}
		if (unidentifiable) {
			nbt.putBoolean(UNIDENTIFIABLE_NBT_KEY, true);
		}
		return nbt;
	}
	
	public static InkPoweredStatusEffectInstance fromNbt(NbtCompound nbt) {
		StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbt);
		InkCost cost = InkCost.fromNbt(nbt);
		int customColor = -1;
		if (nbt.contains(CUSTOM_COLOR_NBT_KEY, NbtElement.NUMBER_TYPE)) {
			customColor = nbt.getInt(CUSTOM_COLOR_NBT_KEY);
		}
		boolean unidentifiable = false;
		if (nbt.contains(UNIDENTIFIABLE_NBT_KEY)) {
			unidentifiable = nbt.getBoolean(UNIDENTIFIABLE_NBT_KEY);
		}
		return new InkPoweredStatusEffectInstance(statusEffectInstance, cost, customColor, unidentifiable);
	}
	
	public static List<InkPoweredStatusEffectInstance> getEffects(ItemStack stack) {
		return getEffects(stack.getNbt());
	}
	
	public static List<InkPoweredStatusEffectInstance> getEffects(@Nullable NbtCompound nbt) {
		List<InkPoweredStatusEffectInstance> list = new ArrayList<>();
		if (nbt != null && nbt.contains(NBT_KEY, NbtElement.LIST_TYPE)) {
			NbtList nbtList = nbt.getList(NBT_KEY, NbtElement.COMPOUND_TYPE);
			
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
			NbtList nbtList = nbtCompound.getList(NBT_KEY, NbtElement.LIST_TYPE);
			
			for (InkPoweredStatusEffectInstance effect : effects) {
				nbtList.add(effect.toNbt());
			}
			
			nbtCompound.put(NBT_KEY, nbtList);
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
					mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(effect, 1.0F));
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