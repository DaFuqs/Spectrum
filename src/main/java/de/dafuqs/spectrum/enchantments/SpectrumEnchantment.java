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
	@SuppressWarnings("resource")
	public Text getName(int level) {
		MinecraftClient client = MinecraftClient.getInstance();
		MutableText mutableText = Text.translatable(this.getTranslationKey());
		if (this.isCursed()) {
			mutableText.formatted(Formatting.RED);
		} else {
			mutableText.formatted(Formatting.GRAY);
		}
		if (!canEntityUse(client.player)) {
			if (SpectrumCommon.CONFIG.NameForUnrevealedEnchantments.isBlank()) {
				mutableText.formatted(Formatting.byCode('k'));
			} else {
				return Text.literal(SpectrumCommon.CONFIG.NameForUnrevealedEnchantments).formatted(Formatting.GRAY);
			}
		}
		
		if (level != 1 || this.getMaxLevel() != 1) {
			mutableText.append(" ").append(Text.translatable("enchantment.level." + level));
		}
		
		return mutableText;
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
