package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import de.dafuqs.spectrum.config.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.util.*;

import java.util.function.Supplier;

public enum SpectrumToolTiers implements Tier {
	
	GEMSTONE(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumConfig.CONFIG.GemstoneDurability.get(),
			SpectrumConfig.CONFIG.GemstoneMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.GemstoneAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.GemstoneEnchantability.get(),
			Ingredient::of
	),
	
	GEMSTONE_MINING_LEVEL_4(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.GemstoneDurability.get(),
			SpectrumConfig.CONFIG.GemstoneMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.GemstoneAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.GemstoneEnchantability.get(),
			Ingredient::of
	),
	
	OBLIVION(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.OblivionDurability.get(),
			SpectrumConfig.CONFIG.OblivionMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.OblivionAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.OblivionEnchantability.get(),
			Ingredient::of
	),
	
	BEDROCK(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			0,
			SpectrumConfig.CONFIG.BedrockMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.BedrockAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.BedrockEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.BEDROCK_DUST)
	),
	
	DRACONIC(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.DraconicDurability.get(),
			SpectrumConfig.CONFIG.DraconicMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.DraconicAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.DraconicEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_BLOODSTONE)
	),
	
	MALACHITE(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.MalachiteDurability.get(),
			SpectrumConfig.CONFIG.MalachiteMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.MalachiteAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.MalachiteEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	GLASS_CREST(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.GlassCrestDurability.get(),
			SpectrumConfig.CONFIG.GlassCrestMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.GlassCrestAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.GlassCrestEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	KNOTTED_SWORD(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.KnottedSwordDurability.get(),
			SpectrumConfig.CONFIG.KnottedSwordMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.KnottedSwordAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.KnottedSwordEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.NIGHTDEW_SPROUT)
	),
	
	NECTAR_LANCE(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.NectarLanceDurability.get(),
			SpectrumConfig.CONFIG.NectarLanceMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.NectarLanceAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.NectarLanceEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.AETHER_VESTIGES)
	),
	
	DREAMFLAYER(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumConfig.CONFIG.DreamflayerDurability.get(),
			SpectrumConfig.CONFIG.DreamflayerMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.DreamflayerAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.DreamflayerEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.BISMUTH_CRYSTAL)
	),
	
	NIGHTFALLS_BLADE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.NightfallsBladeDurability.get(),
			SpectrumConfig.CONFIG.NightfallsBladeMiningSpeed.get().floatValue(),
			SpectrumConfig.CONFIG.NightfallsBladeAttackDamage.get().floatValue(),
			SpectrumConfig.CONFIG.NightfallsBladeEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.MIDNIGHT_CHIP)
	);
	
	private final TagKey<Block> inverseTag;
	private final int itemDurability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairIngredient;
	
	SpectrumToolTiers(final TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
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
	
	public static TriState supportsBedrockTierEnchantment(Holder<Enchantment> holder) {
		if (holder.getKey().equals(Enchantments.UNBREAKING)) {
			return TriState.FALSE;
		}
		return TriState.DEFAULT;
	}
}
