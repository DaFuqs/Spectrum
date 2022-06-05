package de.dafuqs.spectrum.registries;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class SpectrumToolMaterials {
	
	public enum ToolMaterial implements net.minecraft.item.ToolMaterial {
		BEDROCK(5, 0, 15.0F, 8.0F, 3, Ingredient::empty),
		LOW_HEALTH(MiningLevels.IRON, 16, 4.0F, 2.0F, 10, Ingredient::empty),
		VOIDING(MiningLevels.DIAMOND, 1143, 20.0F, 1.0F, 5, Ingredient::empty);
		
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
			this.repairIngredient = new Lazy(repairIngredient);
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
