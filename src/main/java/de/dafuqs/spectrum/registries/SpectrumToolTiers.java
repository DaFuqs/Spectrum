package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import de.dafuqs.spectrum.config.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

import java.util.function.Supplier;

public enum SpectrumToolTiers implements Tier {
	
	LOW_HEALTH(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumConfig.CONFIG.LowHealthDurability.get(),
			SpectrumConfig.CONFIG.LowHealthMiningSpeed.get(),
			SpectrumConfig.CONFIG.LowHealthAttackDamage.get(),
			SpectrumConfig.CONFIG.LowHealthEnchantability.get(),
			Ingredient::of
	),
	
	LOW_HEALTH_MINING_LEVEL_4(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.LowHealthDurability.get(),
			SpectrumConfig.CONFIG.LowHealthMiningSpeed.get(),
			SpectrumConfig.CONFIG.LowHealthAttackDamage.get(),
			SpectrumConfig.CONFIG.LowHealthEnchantability.get(),
			Ingredient::of
	),
	
	VOIDING(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.VoidingDurability.get(),
			SpectrumConfig.CONFIG.VoidingMiningSpeed.get(),
			SpectrumConfig.CONFIG.VoidingAttackDamage.get(),
			SpectrumConfig.CONFIG.VoidingEnchantability.get(),
			Ingredient::of
	),
	
	BEDROCK(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			0,
			SpectrumConfig.CONFIG.BedrockMiningSpeed.get(),
			SpectrumConfig.CONFIG.BedrockAttackDamage.get(),
			SpectrumConfig.CONFIG.BedrockEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.BEDROCK_DUST)
	),
	
	DRACONIC(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.DraconicDurability.get(),
			SpectrumConfig.CONFIG.DraconicMiningSpeed.get(),
			SpectrumConfig.CONFIG.DraconicAttackDamage.get(),
			SpectrumConfig.CONFIG.DraconicEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_BLOODSTONE)
	),
	
	MALACHITE(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.MalachiteDurability.get(),
			SpectrumConfig.CONFIG.MalachiteMiningSpeed.get(),
			SpectrumConfig.CONFIG.MalachiteAttackDamage.get(),
			SpectrumConfig.CONFIG.MalachiteEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	GLASS_CREST(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			SpectrumConfig.CONFIG.GlassCrestDurability.get(),
			SpectrumConfig.CONFIG.GlassCrestMiningSpeed.get(),
			SpectrumConfig.CONFIG.GlassCrestAttackDamage.get(),
			SpectrumConfig.CONFIG.GlassCrestEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.PURE_MALACHITE)
	),
	
	VERDIGRIS(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.VerdigrisDurability.get(),
			SpectrumConfig.CONFIG.VerdigrisMiningSpeed.get(),
			SpectrumConfig.CONFIG.VerdigrisAttackDamage.get(),
			SpectrumConfig.CONFIG.VerdigrisEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.NIGHTDEW_SPROUT)
	),
	
	NECTAR(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
			SpectrumConfig.CONFIG.NectarDurability.get(),
			SpectrumConfig.CONFIG.NectarMiningSpeed.get(),
			SpectrumConfig.CONFIG.NectarAttackDamage.get(),
			SpectrumConfig.CONFIG.NectarEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.AETHER_VESTIGES)
	),
	
	DREAMFLAYER(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumConfig.CONFIG.DreamflayerDurability.get(),
			SpectrumConfig.CONFIG.DreamflayerMiningSpeed.get(),
			SpectrumConfig.CONFIG.DreamflayerAttackDamage.get(),
			SpectrumConfig.CONFIG.DreamflayerEnchantability.get(),
			() -> Ingredient.of(SpectrumItems.BISMUTH_CRYSTAL)
	),
	
	NIGHTFALL(BlockTags.INCORRECT_FOR_IRON_TOOL,
			SpectrumConfig.CONFIG.NightfallDurability.get(),
			SpectrumConfig.CONFIG.NightfallMiningSpeed.get(),
			SpectrumConfig.CONFIG.NightfallAttackDamage.get(),
			SpectrumConfig.CONFIG.NightfallEnchantability.get(),
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
