package de.dafuqs.spectrum.registries;

import com.google.common.base.Suppliers;
import de.dafuqs.spectrum.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.sound.*;

import java.util.function.*;

public enum SpectrumArmorMaterials implements ArmorMaterial {
	
	GEMSTONE("gemstone", 9,
			new int[]{SpectrumCommon.CONFIG.GemstoneArmorBootsProtection, SpectrumCommon.CONFIG.GemstoneArmorLeggingsProtection, SpectrumCommon.CONFIG.GemstoneArmorChestplateProtection, SpectrumCommon.CONFIG.GemstoneArmorHelmetProtection},
			15, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SpectrumCommon.CONFIG.GemstoneArmorToughness, SpectrumCommon.CONFIG.GemstoneArmorKnockbackResistance, () -> Ingredient.fromTag(SpectrumItemTags.GEMSTONE_SHARDS)),
	BEDROCK("bedrock", 70,
			new int[]{SpectrumCommon.CONFIG.BedrockArmorBootsProtection, SpectrumCommon.CONFIG.BedrockArmorLeggingsProtection, SpectrumCommon.CONFIG.BedrockArmorChestplateProtection, SpectrumCommon.CONFIG.BedrockArmorHelmetProtection},
			5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SpectrumCommon.CONFIG.BedrockArmorToughness, SpectrumCommon.CONFIG.BedrockArmorKnockbackResistance, () -> Ingredient.ofItems(SpectrumItems.BEDROCK_DUST));
	
	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Supplier<Ingredient> repairIngredientSupplier;
	
	SpectrumArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredientSupplier = Suppliers.memoize(repairIngredientSupplier::get);
	}

	public int getProtectionAmount(ArmorItem.Type type) {
		return this.protectionAmounts[type.getEquipmentSlot().getEntitySlotId()];
	}
	
	@Override
	public int getDurability(ArmorItem.Type type) {
		return BASE_DURABILITY[type.getEquipmentSlot().getEntitySlotId()] * this.durabilityMultiplier;
	}
	
	@Override
	public int getProtection(ArmorItem.Type type) {
		return 0;
	}
	
	@Override
	public int getEnchantability() {
		return this.enchantability;
	}
	
	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}
	
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredientSupplier.get();
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public float getToughness() {
		return this.toughness;
	}
	
	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
