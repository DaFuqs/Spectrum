package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;

import java.util.function.*;
import java.util.function.Supplier;

public enum SpectrumToolMaterial implements Tier {
	
	LOW_HEALTH(BlockTags.INCORRECT_FOR_IRON_TOOL, 16, 4.0F, 2.0F, 10, Ingredient::of),
	LOW_HEALTH_MINING_LEVEL_4(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 16, 4.0F, 2.0F, 10, Ingredient::of),
	VOIDING(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1143, 20.0F, 1.0F, 5, Ingredient::of),
	BEDROCK(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0, 15.0F, 5.0F, 3, () -> Ingredient.of(SpectrumItems.BEDROCK_DUST)),
	DRACONIC(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 10000, 11.5F, 7.0F, 1, () -> Ingredient.of(SpectrumItems.PURE_BLOODSTONE)),
	MALACHITE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1536, 14.0F, 5.0F, 20, () -> Ingredient.of(SpectrumItems.PURE_MALACHITE)),
	GLASS_CREST(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1536 * 4, 18.0F, 10.0F, 5, () -> Ingredient.of(SpectrumItems.PURE_MALACHITE)),
	VERDIGRIS(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1536, 7.0F, 2.5F, 14, () -> Ingredient.of(SpectrumItems.NIGHTDEW_SPROUT)),
	NECTAR(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, GLASS_CREST.itemDurability, 9.5F, 9.0F, 30, () -> Ingredient.of(SpectrumItems.AETHER_VESTIGES)),
	DREAMFLAYER(BlockTags.INCORRECT_FOR_IRON_TOOL, 650, 5.0F, 2.0F, 20, () -> Ingredient.of(SpectrumItems.BISMUTH_CRYSTAL)),
	NIGHTFALL(BlockTags.INCORRECT_FOR_IRON_TOOL, 650, 2.0F, 1.0F, 0, () -> Ingredient.of(SpectrumItems.MIDNIGHT_CHIP));
	
	private final TagKey<Block> inverseTag;
	private final int itemDurability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairIngredient;
	
	SpectrumToolMaterial(final TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
		this.inverseTag = inverseTag;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairIngredient = Suppliers.memoize(repairIngredient::get);
	}
	
	@Override
	public int getUses() {
		return this.itemDurability;
	}
	
	@Override
	public float getSpeed() {
		return this.miningSpeed;
	}
	
	@Override
	public float getAttackDamageBonus() {
		return this.attackDamage;
	}
	
	@Override
	public TagKey<Block> getIncorrectBlocksForDrops() {
		return this.inverseTag;
	}
	
	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}
	
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}
