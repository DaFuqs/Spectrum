package de.dafuqs.spectrum.registries;

import net.fabricmc.yarn.constants.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

import java.util.function.*;

public class SpectrumToolMaterials {
	
	public enum ToolMaterial implements net.minecraft.item.ToolMaterial {
		LOW_HEALTH(MiningLevels.IRON, 16, 4.0F, 2.0F, 10, Ingredient::empty),
		VOIDING(MiningLevels.DIAMOND, 1143, 20.0F, 1.0F, 5, Ingredient::empty),
		
		BEDROCK(4, 0, 15.0F, 8.0F, 3, () -> Ingredient.ofItems(SpectrumItems.BEDROCK_DUST)),
		MALACHITE(5, 1536, 9.0F, 5.0F, 20, () -> Ingredient.ofItems(SpectrumItems.MALACHITE_CRYSTAL)),
		GLASS_CREST(5, 1536 * 4, 18.0F, 10.0F, 5, () -> Ingredient.ofItems(SpectrumItems.MALACHITE_CRYSTAL)),
		
		DREAMFLAYER(MiningLevels.IRON, 650, 5.0F, 2.0F, 20, () -> Ingredient.ofItems(SpectrumItems.BISMUTH_CRYSTAL)),
		NIGHTFALL(MiningLevels.IRON, 650, 2.0F, 1.0F, 0, () -> Ingredient.ofItems(SpectrumItems.MIDNIGHT_CHIP));
		
		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Lazy<Ingredient> repairIngredient;
		
		ToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairIngredient = new Lazy<>(repairIngredient);
		}
		
		public int getDurability() {
			return this.itemDurability;
		}
		
		public float getMiningSpeedMultiplier() {
			return this.miningSpeed;
		}
		
		public float getAttackDamage() {
			return this.attackDamage;
		}
		
		public int getMiningLevel() {
			return this.miningLevel;
		}
		
		public int getEnchantability() {
			return this.enchantability;
		}
		
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
	
}
