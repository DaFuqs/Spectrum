package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import de.dafuqs.spectrum.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

import java.util.function.Supplier;

public enum SpectrumToolMaterial implements Tier {
	
	LOW_HEALTH(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumCommon.CONFIG.LowHealthDurability,
			SpectrumCommon.CONFIG.LowHealthMiningSpeed,
			SpectrumCommon.CONFIG.LowHealthAttackDamage,
			SpectrumCommon.CONFIG.LowHealthEnchantability,
			Ingredient::of
	),
	
	LOW_HEALTH_MINING_LEVEL_4(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumCommon.CONFIG.LowHealthDurability,
			SpectrumCommon.CONFIG.LowHealthMiningSpeed,
			SpectrumCommon.CONFIG.LowHealthAttackDamage,
			SpectrumCommon.CONFIG.LowHealthEnchantability,
			Ingredient::of
	),
	
	VOIDING(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumCommon.CONFIG.VoidingDurability,
			SpectrumCommon.CONFIG.VoidingMiningSpeed,
			SpectrumCommon.CONFIG.VoidingAttackDamage,
			SpectrumCommon.CONFIG.VoidingEnchantability,
			Ingredient::of
	),
	
	BEDROCK(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumCommon.CONFIG.BedrockDurability,
			SpectrumCommon.CONFIG.BedrockMiningSpeed,
			SpectrumCommon.CONFIG.BedrockAttackDamage,
			SpectrumCommon.CONFIG.BedrockEnchantability,
			() -> Ingredient.of(SpectrumItems.BEDROCK_DUST)
	),
	
	DRACONIC(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumCommon.CONFIG.DraconicDurability,
			SpectrumCommon.CONFIG.DraconicMiningSpeed,
			SpectrumCommon.CONFIG.DraconicAttackDamage,
			SpectrumCommon.CONFIG.DraconicEnchantability,
			() -> Ingredient.of(SpectrumItems.PURE_BLOODSTONE)
	),
	
	MALACHITE(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumCommon.CONFIG.MalachiteDurability,
			SpectrumCommon.CONFIG.MalachiteMiningSpeed,
			SpectrumCommon.CONFIG.MalachiteAttackDamage,
			SpectrumCommon.CONFIG.MalachiteEnchantability,
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	GLASS_CREST(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumCommon.CONFIG.GlassCrestDurability,
			SpectrumCommon.CONFIG.GlassCrestMiningSpeed,
			SpectrumCommon.CONFIG.GlassCrestAttackDamage,
			SpectrumCommon.CONFIG.GlassCrestEnchantability,
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	VERDIGRIS(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumCommon.CONFIG.VerdigrisDurability,
			SpectrumCommon.CONFIG.VerdigrisMiningSpeed,
			SpectrumCommon.CONFIG.VerdigrisAttackDamage,
			SpectrumCommon.CONFIG.VerdigrisEnchantability,
			() -> Ingredient.of(SpectrumItems.NIGHTDEW_SPROUT)
	),
	
	NECTAR(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumCommon.CONFIG.NectarDurability,
			SpectrumCommon.CONFIG.NectarMiningSpeed,
			SpectrumCommon.CONFIG.NectarAttackDamage,
			SpectrumCommon.CONFIG.NectarEnchantability,
			() -> Ingredient.of(SpectrumItems.AETHER_VESTIGES)
	),
	
	DREAMFLAYER(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumCommon.CONFIG.DreamflayerDurability,
			SpectrumCommon.CONFIG.DreamflayerMiningSpeed,
			SpectrumCommon.CONFIG.DreamflayerAttackDamage,
			SpectrumCommon.CONFIG.DreamflayerEnchantability,
			() -> Ingredient.of(SpectrumItems.BISMUTH_CRYSTAL)
	),
	
	NIGHTFALL(BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumCommon.CONFIG.NightfallDurability,
			SpectrumCommon.CONFIG.NightfallMiningSpeed,
			SpectrumCommon.CONFIG.NightfallAttackDamage,
			SpectrumCommon.CONFIG.NightfallEnchantability,
			() -> Ingredient.of(SpectrumItems.MIDNIGHT_CHIP)
	);
	
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
	public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
		return this.inverseTag;
	}
	
	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}
	
	@Override
	public @NotNull Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}
