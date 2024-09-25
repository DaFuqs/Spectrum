package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import net.minecraft.recipe.*;

import java.util.function.Supplier;

import static net.minecraft.item.ToolMaterials.*;

public class SpectrumToolMaterials {
	
	public enum ToolMaterial implements net.minecraft.item.ToolMaterial {
		LOW_HEALTH(IRON.getMiningLevel(), 16, 4.0F, 2.0F, 10, Ingredient::empty),
		VOIDING(DIAMOND.getMiningLevel(), 1143, 20.0F, 1.0F, 5, Ingredient::empty),

		BEDROCK(4, 0, 15.0F, 5.0F, 3, () -> Ingredient.ofItems(SpectrumItems.BEDROCK_DUST)),
		DRACONIC(6, 10000, 11.5F, 7.0F, 1, () -> Ingredient.ofItems(SpectrumItems.REFINED_BLOODSTONE)),
		MALACHITE(5, 1536, 14.0F, 5.0F, 20, () -> Ingredient.ofItems(SpectrumItems.REFINED_MALACHITE)),
		GLASS_CREST(5, 1536 * 4, 18.0F, 10.0F, 5, () -> Ingredient.ofItems(SpectrumItems.REFINED_MALACHITE)),

		VERDIGRIS(3, 1536, 7.0F, 2.5F, 14, () -> Ingredient.ofItems(SpectrumItems.NIGHTDEW_SPROUT)),
		NECTAR(6, GLASS_CREST.itemDurability, 9.5F, 9.0F, 30, () -> Ingredient.ofItems(SpectrumItems.AETHER_VESTIGES)),

		DREAMFLAYER(IRON.getMiningLevel(), 650, 5.0F, 2.0F, 20, () -> Ingredient.ofItems(SpectrumItems.BISMUTH_CRYSTAL)),
		NIGHTFALL(IRON.getMiningLevel(), 650, 2.0F, 1.0F, 0, () -> Ingredient.ofItems(SpectrumItems.MIDNIGHT_CHIP));
		
		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Supplier<Ingredient> repairIngredient;
		
		ToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairIngredient = Suppliers.memoize(repairIngredient::get);
		}
		
		@Override
		public int getDurability() {
			return this.itemDurability;
		}
		
		@Override
		public float getMiningSpeedMultiplier() {
			return this.miningSpeed;
		}
		
		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}
		
		@Override
		public int getMiningLevel() {
			return this.miningLevel;
		}
		
		@Override
		public int getEnchantability() {
			return this.enchantability;
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
	
}
