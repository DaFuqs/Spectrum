package de.dafuqs.spectrum.enchantments;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public abstract class SpectrumEnchantment extends Enchantment {
	
	protected final Identifier unlockAdvancementIdentifier;
	
	protected SpectrumEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, Identifier unlockAdvancementIdentifier) {
		super(weight, type, slotTypes);
		this.unlockAdvancementIdentifier = unlockAdvancementIdentifier;
	}
	
	@Override
	public boolean isTreasure() {
		return false;
	}
	
	@Override
	public boolean isAvailableForEnchantedBookOffer() {
		return false;
	}
	
	@Override
	public boolean isAvailableForRandomSelection() {
		return false;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
    public Text getName(int level) {
		Text text = super.getName(level);
		if (canEntityUse(MinecraftClient.getInstance().player)) {
			return text;
		}
		
		if (SpectrumCommon.CONFIG.NameForUnrevealedEnchantments.isBlank() && text instanceof MutableText mutableText) {
			return mutableText.formatted(Formatting.byCode('k'));
		} else {
			return Text.literal(SpectrumCommon.CONFIG.NameForUnrevealedEnchantments).setStyle(text.getStyle());
		}
	}
	
	public boolean canEntityUse(Entity entity) {
		if (entity instanceof PlayerEntity playerEntity) {
			return AdvancementHelper.hasAdvancement(playerEntity, unlockAdvancementIdentifier);
		} else {
			return false;
		}
	}
	
	public Identifier getUnlockAdvancementIdentifier() {
		return unlockAdvancementIdentifier;
	}
	
}
