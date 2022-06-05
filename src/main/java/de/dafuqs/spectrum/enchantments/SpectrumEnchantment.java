package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.helpers.Support;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public abstract class SpectrumEnchantment extends Enchantment {
	
	protected Identifier unlockAdvancementIdentifier;
	
	protected SpectrumEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, Identifier unlockAdvancementIdentifier) {
		super(weight, type, slotTypes);
		this.unlockAdvancementIdentifier = unlockAdvancementIdentifier;
	}
	
	public boolean isTreasure() {
		return false;
	}
	
	public boolean isAvailableForEnchantedBookOffer() {
		return false;
	}
	
	public boolean isAvailableForRandomSelection() {
		return false;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public Text getName(int level) {
		MutableText mutableText = new TranslatableText(this.getTranslationKey());
		if (this.isCursed()) {
			mutableText.formatted(Formatting.RED);
		} else {
			mutableText.formatted(Formatting.GRAY);
		}
		if (!canEntityUse(MinecraftClient.getInstance().player)) {
			mutableText.formatted(Formatting.byCode('k'));
		}
		
		if (level != 1 || this.getMaxLevel() != 1) {
			mutableText.append(" ").append(new TranslatableText("enchantment.level." + level));
		}
		
		return mutableText;
	}
	
	public boolean canEntityUse(Entity entity) {
		if (entity instanceof PlayerEntity playerEntity) {
			return Support.hasAdvancement(playerEntity, unlockAdvancementIdentifier);
		} else {
			return false;
		}
	}
	
	public Identifier getUnlockAdvancementIdentifier() {
		return unlockAdvancementIdentifier;
	}
	
}
