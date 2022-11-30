package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum SpectrumArmorMaterials implements ArmorMaterial {
	
	GEMSTONE("gemstone", 9,
			new int[]{SpectrumCommon.CONFIG.GemstoneArmorBootsProtection, SpectrumCommon.CONFIG.GemstoneArmorLeggingsProtection, SpectrumCommon.CONFIG.GemstoneArmorChestplateProtection, SpectrumCommon.CONFIG.GemstoneArmorHelmetProtection},
			15, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SpectrumCommon.CONFIG.GemstoneArmorToughness, SpectrumCommon.CONFIG.GemstoneArmorKnockbackResistance, () -> Ingredient.fromTag(SpectrumItemTags.GEMSTONE_SHARDS)),
	BEDROCK("bedrock", 70,
			new int[]{SpectrumCommon.CONFIG.BedrockArmorBootsProtection, SpectrumCommon.CONFIG.BedrockArmorLeggingsProtection, SpectrumCommon.CONFIG.BedrockArmorChestplateProtection, SpectrumCommon.CONFIG.BedrockArmorHelmetProtection},
			5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SpectrumCommon.CONFIG.BedrockArmorToughness, SpectrumCommon.CONFIG.BedrockArmorKnockbackResistance, Ingredient::empty);
	
	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Lazy<Ingredient> repairIngredientSupplier;
	
	SpectrumArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
	}
	
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
	}
	
	public int getProtectionAmount(EquipmentSlot slot) {
		return this.protectionAmounts[slot.getEntitySlotId()];
	}
	
	public int getEnchantability() {
		return this.enchantability;
	}
	
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}
	
	public Ingredient getRepairIngredient() {
		return this.repairIngredientSupplier.get();
	}
	
	public String getName() {
		return this.name;
	}
	
	public float getToughness() {
		return this.toughness;
	}
	
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
