package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class DreamflayerItem extends SwordItem {
	
	/**
	 * The less armor the attacker with this sword has and the more
	 * the one that gets attacked, the higher the damage will be
	 *
	 * See LivingEntityMixin spectrum$applyDreamflayerDamage
	 */
	public static float ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER = 2.5F;
	
	public static final ToolMaterial TOOL_MATERIAL = new ToolMaterial() {
		@Override
		public int getDurability() {
			return 650;
		}
		
		@Override
		public float getMiningSpeedMultiplier() {
			return 6.0F;
		}
		
		@Override
		public float getAttackDamage() {
			return 2.0F;
		}
		
		@Override
		public int getMiningLevel() {
			return 3;
		}
		
		@Override
		public int getEnchantability() {
			return 25;
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(SpectrumItems.BISMUTH_CRYSTAL);
		}
	};
	
	public DreamflayerItem(int attackDamage, float attackSpeed, Settings settings) {
		super(TOOL_MATERIAL, attackDamage, attackSpeed, settings);
	}
	
}
