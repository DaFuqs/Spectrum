package de.dafuqs.spectrum.registries;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.blocks.rock_candy.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.items.food.beverages.*;
import de.dafuqs.spectrum.items.item_frame.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.magic_items.ampoules.*;
import de.dafuqs.spectrum.items.map.*;
import de.dafuqs.spectrum.items.misc.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.registry.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.enchantment.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

import static de.dafuqs.spectrum.registries.SpectrumFluids.*;

@SuppressWarnings("UnstableApiUsage")
public class SpectrumItems {
	
	public static class IS {
		
		public static FabricItemSettings of() {
			return new FabricItemSettings();
		}
		
		public static FabricItemSettings of(int maxCount) {
			return new FabricItemSettings().maxCount(maxCount);
		}
		
		public static FabricItemSettings of(Rarity rarity) {
			return new FabricItemSettings().rarity(rarity);
		}
		
		public static FabricItemSettings of(int maxCount, Rarity rarity) {
			return new FabricItemSettings().maxCount(maxCount).rarity(rarity);
		}
		
	}
	
	// Main items
	public static final Item GUIDEBOOK = new GuidebookItem(IS.of(1));
	public static final Item PAINTBRUSH = new PaintbrushItem(IS.of(1));
	public static final Item TUNING_STAMP = new TuningStampItem(IS.of(1));
	public static final Item CRAFTING_TABLET = new CraftingTabletItem(IS.of(1));
	
	public static final Item PEDESTAL_TIER_1_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_SIMPLE);
	public static final Item PEDESTAL_TIER_2_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_ADVANCED);
	public static final Item PEDESTAL_TIER_3_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_COMPLEX);
	public static final Item FUSION_SHRINE_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.FUSION_SHRINE);
	public static final Item ENCHANTER_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.ENCHANTER);
	public static final Item SPIRIT_INSTILLER_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.SPIRIT_INSTILLER);
	public static final Item CINDERHEARTH_STRUCTURE_PLACER = new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.CINDERHEARTH);
	
	// Gem shards
	public static final Item TOPAZ_SHARD = new Item(IS.of());
	public static final Item CITRINE_SHARD = new Item(IS.of());
	public static final Item ONYX_SHARD = new CloakedItem(IS.of(), SpectrumCommon.locate("collect_all_basic_pigments_besides_brown"), Items.BLACK_DYE);
	public static final Identifier BREA_DECAYED_BEDROCK = SpectrumCommon.locate("midgame/break_decayed_bedrock");
	public static final Item MOONSTONE_SHARD = new CloakedItem(IS.of(), BREA_DECAYED_BEDROCK, Items.WHITE_DYE);
	public static final Item SPECTRAL_SHARD = new Item(IS.of(Rarity.RARE));
	
	public static final Item TOPAZ_POWDER = new GemstonePowderItem(IS.of(), SpectrumCommon.locate("hidden/collect_shards/topaz"), BuiltinGemstoneColor.CYAN);
	public static final Item AMETHYST_POWDER = new GemstonePowderItem(IS.of(), SpectrumCommon.locate("hidden/collect_shards/amethyst"), BuiltinGemstoneColor.MAGENTA);
	public static final Item CITRINE_POWDER = new GemstonePowderItem(IS.of(), SpectrumCommon.locate("hidden/collect_shards/citrine"), BuiltinGemstoneColor.YELLOW);
	public static final Item ONYX_POWDER = new GemstonePowderItem(IS.of(), SpectrumCommon.locate("create_onyx_shard"), BuiltinGemstoneColor.BLACK);
	public static final Item MOONSTONE_POWDER = new GemstonePowderItem(IS.of(), SpectrumCommon.locate("lategame/collect_moonstone"), BuiltinGemstoneColor.WHITE);
	
	// Pigment
	public static final Item BLACK_PIGMENT = new PigmentItem(IS.of(), DyeColor.BLACK);
	public static final Item BLUE_PIGMENT = new PigmentItem(IS.of(), DyeColor.BLUE);
	public static final Item BROWN_PIGMENT = new PigmentItem(IS.of(), DyeColor.BROWN);
	public static final Item CYAN_PIGMENT = new PigmentItem(IS.of(), DyeColor.CYAN);
	public static final Item GRAY_PIGMENT = new PigmentItem(IS.of(), DyeColor.GRAY);
	public static final Item GREEN_PIGMENT = new PigmentItem(IS.of(), DyeColor.GREEN);
	public static final Item LIGHT_BLUE_PIGMENT = new PigmentItem(IS.of(), DyeColor.LIGHT_BLUE);
	public static final Item LIGHT_GRAY_PIGMENT = new PigmentItem(IS.of(), DyeColor.LIGHT_GRAY);
	public static final Item LIME_PIGMENT = new PigmentItem(IS.of(), DyeColor.LIME);
	public static final Item MAGENTA_PIGMENT = new PigmentItem(IS.of(), DyeColor.MAGENTA);
	public static final Item ORANGE_PIGMENT = new PigmentItem(IS.of(), DyeColor.ORANGE);
	public static final Item PINK_PIGMENT = new PigmentItem(IS.of(), DyeColor.PINK);
	public static final Item PURPLE_PIGMENT = new PigmentItem(IS.of(), DyeColor.PURPLE);
	public static final Item RED_PIGMENT = new PigmentItem(IS.of(), DyeColor.RED);
	public static final Item WHITE_PIGMENT = new PigmentItem(IS.of(), DyeColor.WHITE);
	public static final Item YELLOW_PIGMENT = new PigmentItem(IS.of(), DyeColor.YELLOW);
	
	// Preenchanted tools
	public static final Item MULTITOOL = new PreenchantedMultiToolItem(ToolMaterials.IRON, 2, -2.4F, IS.of(Rarity.UNCOMMON).maxDamage(ToolMaterials.IRON.getDurability()));
	public static final Item TENDER_PICKAXE = new GlintlessPickaxe(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability())) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.SILK_TOUCH, 1);
		}
	};
	public static final Item LUCKY_PICKAXE = new GlintlessPickaxe(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 1, -2.8F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability())) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FORTUNE, 3);
		}
	};
	public static final Item RAZOR_FALCHION = new RazorFalchionItem(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH, 4, -2.2F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability()));
	public static final Item OBLIVION_PICKAXE = new OblivionPickaxeItem(SpectrumToolMaterials.ToolMaterial.VOIDING, 1, -2.8F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VOIDING.getDurability()));
	public static final Item RESONANT_PICKAXE = new GlintlessPickaxe(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH_MINING_LEVEL_4, 1, -2.8F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.LOW_HEALTH.getDurability())) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(SpectrumEnchantments.RESONANCE, 1);
		}
	};
	public static final Item DRAGONRENDING_PICKAXE = new GlintlessPickaxe(SpectrumToolMaterials.ToolMaterial.DRACONIC, 1, -2.8F, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.DRACONIC.getDurability())) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(SpectrumEnchantments.RAZING, 3);
		}
	};
	public static final SpectrumFishingRodItem LAGOON_ROD = new LagoonRodItem(IS.of().maxDamage(256));
	public static final SpectrumFishingRodItem MOLTEN_ROD = new MoltenRodItem(IS.of().maxDamage(256));
	
	// Bedrock Tools
	public static final SpectrumToolMaterials.ToolMaterial BEDROCK_MATERIAL = SpectrumToolMaterials.ToolMaterial.BEDROCK;
	public static final Item BEDROCK_PICKAXE = new BedrockPickaxeItem(BEDROCK_MATERIAL, 1, -2.8F, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_AXE = new BedrockAxeItem(BEDROCK_MATERIAL, 5, -3.0F, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_SHOVEL = new BedrockShovelItem(BEDROCK_MATERIAL, 1, -3.0F, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_SWORD = new BedrockSwordItem(BEDROCK_MATERIAL, 4, -2.4F, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_HOE = new BedrockHoeItem(BEDROCK_MATERIAL, -2, -0.0F, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_BOW = new BedrockBowItem(IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_CROSSBOW = new BedrockCrossbowItem(IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_SHEARS = new BedrockShearsItem(IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	public static final Item BEDROCK_FISHING_ROD = new BedrockFishingRodItem(IS.of(Rarity.UNCOMMON).fireproof().maxDamage(SpectrumToolMaterials.ToolMaterial.BEDROCK.getDurability()));
	
	public static final SpectrumToolMaterials.ToolMaterial MALACHITE = SpectrumToolMaterials.ToolMaterial.MALACHITE;
	public static final Item MALACHITE_WORKSTAFF = new WorkstaffItem(MALACHITE, 1, -3.2F, IS.of(1, Rarity.UNCOMMON));
	public static final Item MALACHITE_ULTRA_GREATSWORD = new GreatswordItem(MALACHITE, 7, -2.8F, 1.0F, IS.of(1, Rarity.UNCOMMON));
	public static final Item MALACHITE_CROSSBOW = new MalachiteCrossbowItem(IS.of(1, Rarity.UNCOMMON).fireproof().maxDamage(MALACHITE.getDurability()));
	public static final Item MALACHITE_BIDENT = new MalachiteBidentItem(IS.of(1, Rarity.UNCOMMON).maxDamage(MALACHITE.getDurability()), -2.4, 9, 0.25F, 0F);
	
	// variants by socketing a moonstone core
	public static final SpectrumToolMaterials.ToolMaterial GLASS_CREST = SpectrumToolMaterials.ToolMaterial.GLASS_CREST;
	public static final Item GLASS_CREST_WORKSTAFF = new GlassCrestWorkstaffItem(GLASS_CREST, 1, -2.8F, IS.of(1, Rarity.UNCOMMON));
	public static final Item GLASS_CREST_ULTRA_GREATSWORD = new GlassCrestGreatswordItem(GLASS_CREST, 5, -2.8F, 1.0F, IS.of(1, Rarity.UNCOMMON));
	public static final Item GLASS_CREST_CROSSBOW = new GlassCrestCrossbowItem(IS.of(1, Rarity.UNCOMMON).fireproof().maxDamage(GLASS_CREST.getDurability()));
	public static final Item FEROCIOUS_GLASS_CREST_BIDENT = new FerociousBidentItem(IS.of(1, Rarity.UNCOMMON).maxDamage(GLASS_CREST.getDurability()), -2.2, 13, 0.33F, 0.33F);
	public static final Item FRACTAL_GLASS_CREST_BIDENT = new FractalBidentItem(IS.of(1, Rarity.UNCOMMON).maxDamage(GLASS_CREST.getDurability()), -2.4, 6.5, 0.25F, 0.25F);
	
	public static final Item MALACHITE_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.MALACHITE, SpectrumParticleTypes.LIME_CRAFTING);
	public static final Item TOPAZ_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.TOPAZ, SpectrumParticleTypes.CYAN_CRAFTING);
	public static final Item AMETHYST_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.AMETHYST, SpectrumParticleTypes.MAGENTA_CRAFTING);
	public static final Item CITRINE_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.CITRINE, SpectrumParticleTypes.YELLOW_CRAFTING);
	public static final Item ONYX_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.ONYX, SpectrumParticleTypes.BLACK_CRAFTING);
	public static final Item MOONSTONE_GLASS_ARROW = new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.MOONSTONE, SpectrumParticleTypes.WHITE_CRAFTING);
	
	public static final Item OMNI_ACCELERATOR = new OmniAcceleratorItem(IS.of(1, Rarity.UNCOMMON));

	public static final Item AZURITE_GLASS_AMPOULE = new GlassAmpouleItem(IS.of(Rarity.UNCOMMON));
	public static final Item MALACHITE_GLASS_AMPOULE = new MalachiteGlassAmpouleItem(IS.of(Rarity.UNCOMMON));
	public static final Item BLOODSTONE_GLASS_AMPOULE = new BloodstoneGlassAmpouleItem(IS.of(Rarity.UNCOMMON));
	
	// Special tools
	public static final Item DREAMFLAYER = new DreamflayerItem(SpectrumToolMaterials.ToolMaterial.DREAMFLAYER, 3, -1.8F, IS.of(1, Rarity.UNCOMMON));
	public static final Item NIGHTFALLS_BLADE = new NightfallsBladeItem(SpectrumToolMaterials.ToolMaterial.NIGHTFALL, 0, -3.4F, IS.of(1, Rarity.UNCOMMON));
	public static final DraconicTwinswordItem DRACONIC_TWINSWORD = new DraconicTwinswordItem(SpectrumToolMaterials.ToolMaterial.DRACONIC, 6, -3.0F, IS.of(1, Rarity.RARE));
	public static final DragonTalonItem DRAGON_TALON = new DragonTalonItem(SpectrumToolMaterials.ToolMaterial.DRACONIC, -3.0, -1.0, IS.of(1, Rarity.RARE).maxDamage(SpectrumToolMaterials.ToolMaterial.DRACONIC.getDurability()));
	public static final LightGreatswordItem KNOTTED_SWORD = new LightGreatswordItem(SpectrumToolMaterials.ToolMaterial.VERDIGRIS, 3, -2.4F, 0.25F, 0.5F, 0xFFd4d6ff, IS.of(1, Rarity.UNCOMMON).maxDamage(SpectrumToolMaterials.ToolMaterial.VERDIGRIS.getDurability()));
	public static final LightGreatswordItem NECTAR_LANCE = new NectarLanceItem(SpectrumToolMaterials.ToolMaterial.NECTAR, 0, -2.4F, 0.5F, 1.5F, 0xFFf8e8ff, IS.of(1, Rarity.EPIC).maxDamage(SpectrumToolMaterials.ToolMaterial.NECTAR.getDurability()));

	// Bedrock Armor
	public static final Item BEDROCK_HELMET = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.HELMET, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(-1)) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROJECTILE_PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_CHESTPLATE = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.CHESTPLATE, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(-1)) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_LEGGINGS = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.LEGGINGS, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(-1)) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.BLAST_PROTECTION, 5);
		}
	};
	public static final Item BEDROCK_BOOTS = new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.BOOTS, IS.of(Rarity.UNCOMMON).fireproof().maxDamage(-1)) {
		@Override
		public Map<Enchantment, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FIRE_PROTECTION, 5);
		}
	};
	
	// Armor
	public static final Item FETCHLING_HELMET = new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.HELMET, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GEMSTONE.getDurability(ArmorItem.Type.HELMET)));
	public static final Item FEROCIOUS_CHESTPLATE = new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.CHESTPLATE, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GEMSTONE.getDurability(ArmorItem.Type.CHESTPLATE)));
	public static final Item SYLPH_LEGGINGS = new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.LEGGINGS, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GEMSTONE.getDurability(ArmorItem.Type.LEGGINGS)));
	public static final Item OREAD_BOOTS = new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.BOOTS, IS.of(Rarity.UNCOMMON).maxDamage(SpectrumArmorMaterials.GEMSTONE.getDurability(ArmorItem.Type.BOOTS)));
	
	// Decay drops
	public static final Item VEGETAL = new CloakedItemWithLoomPattern(IS.of(), SpectrumAdvancements.CRAFT_BOTTLE_OF_FADING, Items.GUNPOWDER, SpectrumBannerPatterns.VEGETAL);
	public static final Item NEOLITH = new CloakedItemWithLoomPattern(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CRAFT_BOTTLE_OF_FAILING, Items.GUNPOWDER, SpectrumBannerPatterns.NEOLITH);
	public static final Item BEDROCK_DUST = new CloakedItemWithLoomPattern(IS.of(Rarity.UNCOMMON), BREA_DECAYED_BEDROCK, Items.GUNPOWDER, SpectrumBannerPatterns.BEDROCK_DUST);
	
	public static final MidnightAberrationItem MIDNIGHT_ABERRATION = new MidnightAberrationItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CREATE_MIDNIGHT_ABERRATION, SpectrumItems.SPECTRAL_SHARD);
	public static final Item MIDNIGHT_CHIP = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CREATE_MIDNIGHT_ABERRATION, Items.GRAY_DYE);
	
	public static final Item BISMUTH_FLAKE = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.ENTER_DIMENSION, Items.CYAN_DYE);
	public static final Item BISMUTH_CRYSTAL = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.ENTER_DIMENSION, Items.CYAN_DYE);
	public static final Item RAW_MALACHITE = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.REVEAL_MALACHITE, Items.GREEN_DYE);
	public static final Item REFINED_MALACHITE = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.REVEAL_MALACHITE, Items.GREEN_DYE);
	
	// Fluid Buckets
	public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(LIQUID_CRYSTAL, IS.of(1).recipeRemainder(Items.BUCKET));
	public static final Item MUD_BUCKET = new BucketItem(MUD, IS.of(1).recipeRemainder(Items.BUCKET));
	public static final Item MIDNIGHT_SOLUTION_BUCKET = new BucketItem(MIDNIGHT_SOLUTION, IS.of(1).recipeRemainder(Items.BUCKET));
	public static final Item DRAGONROT_BUCKET = new BucketItem(DRAGONROT, IS.of(1).recipeRemainder(Items.BUCKET));
	
	// Decay bottles
	public static final Item BOTTLE_OF_FADING = new DecayPlacerItem(SpectrumBlocks.FADING, IS.of(16), List.of(Text.translatable("item.spectrum.bottle_of_fading.tooltip")));
	public static final Item BOTTLE_OF_FAILING = new DecayPlacerItem(SpectrumBlocks.FAILING, IS.of(16), List.of(Text.translatable("item.spectrum.bottle_of_failing.tooltip")));
	public static final Item BOTTLE_OF_RUIN = new DecayPlacerItem(SpectrumBlocks.RUIN, IS.of(16), List.of(Text.translatable("item.spectrum.bottle_of_ruin.tooltip")));
	public static final Item BOTTLE_OF_FORFEITURE = new DecayPlacerItem(SpectrumBlocks.FORFEITURE, IS.of(16), List.of(CreativeOnlyItem.DESCRIPTION, Text.translatable("item.spectrum.bottle_of_forfeiture.tooltip")));
	public static final Item BOTTLE_OF_DECAY_AWAY = new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY, IS.of(16), List.of(Text.translatable("item.spectrum.bottle_of_decay_away.tooltip")));
	
	// Resources
	public static final Item SHIMMERSTONE_GEM = new CloakedItemWithLoomPattern(IS.of(), ((RevelationAware) SpectrumBlocks.SHIMMERSTONE_ORE).getCloakAdvancementIdentifier(), Items.YELLOW_DYE, SpectrumBannerPatterns.SHIMMERSTONE);
	public static final Item RAW_AZURITE = new CloakedItemWithLoomPattern(IS.of(), ((RevelationAware) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE, SpectrumBannerPatterns.RAW_AZURITE);
	public static final Item REFINED_AZURITE = new CloakedItem(IS.of(), ((RevelationAware) SpectrumBlocks.AZURITE_ORE).getCloakAdvancementIdentifier(), Items.BLUE_DYE);
	public static final CloakedFloatItem STRATINE_FRAGMENTS = new CloakedFloatItem(IS.of().fireproof(), -0.00125F, ((RevelationAware) SpectrumBlocks.STRATINE_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedFloatItem STRATINE_GEM = new CloakedFloatItem(IS.of(16).fireproof(), -0.01F, ((RevelationAware) SpectrumBlocks.STRATINE_ORE).getCloakAdvancementIdentifier(), Items.RED_DYE);
	public static final CloakedFloatItem PALTAERIA_FRAGMENTS = new CloakedFloatItem(IS.of(), 0.00125F, ((RevelationAware) SpectrumBlocks.PALTAERIA_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	public static final CloakedFloatItem PALTAERIA_GEM = new CloakedFloatItem(IS.of(16), 0.01F, ((RevelationAware) SpectrumBlocks.PALTAERIA_ORE).getCloakAdvancementIdentifier(), Items.CYAN_DYE);
	public static final Item PYRITE_CHUNK = new Item(IS.of());
	public static final Item DRAGONBONE_CHUNK = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.BREAK_CRACKED_DRAGONBONE, Items.GRAY_DYE);
	public static final Item BONE_ASH = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.BREAK_CRACKED_DRAGONBONE, Items.GRAY_DYE);
	public static final Item EFFULGENT_FEATHER = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_EFFULGENT_FEATHER, Items.RED_DYE);
	public static final Item RAW_BLOODSTONE = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_EFFULGENT_FEATHER, Items.RED_DYE);
	public static final Item REFINED_BLOODSTONE = new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_EFFULGENT_FEATHER, Items.RED_DYE);
	public static final Item DOWNSTONE_FRAGMENTS = new CloakedItem(IS.of(16, Rarity.UNCOMMON), SpectrumAdvancements.FIND_EXCAVATION_SITE, Items.LIGHT_GRAY_DYE);
	public static final Item RESONANCE_SHARD = new CloakedItem(IS.of(16, Rarity.UNCOMMON), SpectrumAdvancements.STRIKE_UP_HUMMINGSTONE_HYMN, Items.LIGHT_BLUE_DYE);
	public static final Item AETHER_VESTIGES = new AetherVestigesItem(IS.of(1, Rarity.EPIC).fireproof(), "item.spectrum.aether_vestiges.tooltip");
	
	public static final Item QUITOXIC_POWDER = new CloakedItem(IS.of(), SpectrumAdvancements.REVEAL_QUITOXIC_REEDS, Items.PURPLE_DYE);
	public static final Item STORM_STONE = new StormStoneItem(IS.of(), SpectrumAdvancements.REVEAL_STORM_STONES, Items.YELLOW_DYE);
	public static final Item MERMAIDS_GEM = new MermaidsGemItem(SpectrumBlocks.MERMAIDS_BRUSH, IS.of());
	public static final CloakedItem STAR_FRAGMENT = new CloakedItem(IS.of(16), SpectrumAdvancements.UNLOCK_SHOOTING_STARS, Items.PURPLE_DYE);
	public static final Item STARDUST = new CloakedItemWithLoomPattern(IS.of(), SpectrumAdvancements.UNLOCK_SHOOTING_STARS, Items.PURPLE_DYE, SpectrumBannerPatterns.SHIMMER);
	public static final Item ASH_FLAKES = new AshItem(IS.of());
	
	public static final Item HIBERNATING_JADE_VINE_BULB = new ItemWithTooltip(IS.of(16), "item.spectrum.hibernating_jade_vine_bulb.tooltip");
	public static final Item GERMINATED_JADE_VINE_BULB = new GerminatedJadeVineBulbItem(IS.of(16), SpectrumAdvancements.COLLECT_HIBERNATING_JADE_VINE_BULB, Items.LIME_DYE);
	public static final Item JADE_VINE_PETALS = new CloakedItemWithLoomPattern(IS.of(), SpectrumCommon.locate("midgame/build_spirit_instiller_structure"), Items.LIME_DYE, SpectrumBannerPatterns.JADE_VINE); // TODO: Funky unlock?
	
	public static final Item JADEITE_PETALS = new Item(IS.of(Rarity.UNCOMMON));
	
	public static final Item BLOOD_ORCHID_PETAL = new CloakedItem(IS.of(), SpectrumAdvancements.REVEAL_BLOOD_ORCHID_PETALS, Items.RED_DYE);

	public static final Item ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.ROCK_CANDY), RockCandy.RockCandyVariant.SUGAR);
	public static final Item TOPAZ_ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.TOPAZ_ROCK_CANDY), RockCandy.RockCandyVariant.TOPAZ);
	public static final Item AMETHYST_ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.AMETHYST_ROCK_CANDY), RockCandy.RockCandyVariant.AMETHYST);
	public static final Item CITRINE_ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.CITRINE_ROCK_CANDY), RockCandy.RockCandyVariant.CITRINE);
	public static final Item ONYX_ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.ONYX_ROCK_CANDY), RockCandy.RockCandyVariant.ONYX);
	public static final Item MOONSTONE_ROCK_CANDY = new RockCandyItem(IS.of().food(SpectrumFoodComponents.MOONSTONE_ROCK_CANDY), RockCandy.RockCandyVariant.MOONSTONE);
	
	public static final Item BLOODBOIL_SYRUP = new StatusEffectDrinkItem(IS.of().food(SpectrumFoodComponents.BLOODBOIL_SYRUP).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item MILKY_RESIN = new Item(IS.of(Rarity.UNCOMMON));
	
	// Food & drinks
	public static final Item MOONSTRUCK_NECTAR = new MoonstruckNectarItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.MOONSTRUCK_NECTAR).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item JADE_JELLY = new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.JADE_JELLY), "item.spectrum.jade_jelly.tooltip");
	public static final Item GLASS_PEACH = new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.GLASS_PEACH), "item.spectrum.glass_peach.tooltip");
	public static final Item FISSURE_PLUM = new AliasedTooltipItem(SpectrumBlocks.ABYSSAL_VINES, IS.of().food(SpectrumFoodComponents.FISSURE_PLUM), "item.spectrum.fissure_plum.tooltip");
	public static final Item NIGHTDEW_SPROUT = new AliasedTooltipItem(SpectrumBlocks.NIGHTDEW, IS.of().food(SpectrumFoodComponents.NIGHTDEW_SPROUT), "item.spectrum.nightdew_sprout.tooltip");
	public static final Item NECTARDEW_BURGEON = new NectardewBurgeonItem(IS.of().food(SpectrumFoodComponents.NECTARDEW_BURGEON), "item.spectrum.nectardew_burgeon.tooltip", SpectrumAdvancements.COLLECT_NECTARDEW, SpectrumItems.NIGHTDEW_SPROUT);
	public static final Item RESTORATION_TEA = new RestorationTeaItem(IS.of(16).food(SpectrumFoodComponents.RESTORATION_TEA).recipeRemainder(Items.GLASS_BOTTLE), SpectrumFoodComponents.RESTORATION_TEA_SCONE_BONUS);
	public static final Item KIMCHI = new KimchiItem(IS.of().food(SpectrumFoodComponents.KIMCHI));
	public static final Item CLOTTED_CREAM = new ClottedCreamItem(IS.of().food(SpectrumFoodComponents.CLOTTED_CREAM), new String[]{"item.spectrum.clotted_cream.tooltip", "item.spectrum.clotted_cream.tooltip2"});
	public static final Item FRESH_CHOCOLATE = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.FRESH_CHOCOLATE), 10);
	public static final Item HOT_CHOCOLATE = new TeaItem(IS.of(16).food(SpectrumFoodComponents.HOT_CHOCOLATE), SpectrumFoodComponents.HOT_CHOCOLATE_SCONE_BONUS);
	public static final Item KARAK_CHAI = new TeaItem(IS.of(16).food(SpectrumFoodComponents.KARAK_CHAI), SpectrumFoodComponents.KARAK_CHAI_SCONE_BONUS);
	public static final Item AZALEA_TEA = new AzaleaTeaItem(IS.of(16).food(SpectrumFoodComponents.AZALEA_TEA), SpectrumFoodComponents.AZALEA_TEA_SCONE_BONUS);
	public static final Item BODACIOUS_BERRY_BAR = new Item(IS.of().food(SpectrumFoodComponents.BODACIOUS_BERRY_BAR));
	public static final Item DEMON_TEA = new TeaItem(IS.of(16).food(SpectrumFoodComponents.DEMON_TEA), SpectrumFoodComponents.DEMON_TEA_SCONE_BONUS);
	public static final Item SCONE = new Item(IS.of().food(SpectrumFoodComponents.SCONE));
	
	public static final Item CHEONG = new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.CHEONG), "item.spectrum.cheong.tooltip");
	public static final Item MERMAIDS_JAM = new Item(IS.of().food(SpectrumFoodComponents.MERMAIDS_JAM));
	public static final Item MERMAIDS_POPCORN = new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.MERMAIDS_POPCORN), "item.spectrum.mermaids_popcorn.tooltip");
	public static final Item LE_FISHE_AU_CHOCOLAT = new Item(IS.of().food(SpectrumFoodComponents.LE_FISHE_AU_CHOCOLAT));
	//public static final Item STUFFED_PETALS = new Item(IS.of().food(SpectrumFoodComponents.STUFFED_PETALS));
	//public static final Item PASTICHE = new Item(IS.of().food(SpectrumFoodComponents.PASTICHE));
	//public static final Item VITTORIAS_ROAST = new Item(IS.of().food(SpectrumFoodComponents.VITTORIAS_ROAST));

	public static final Item INFUSED_BEVERAGE = new VariantBeverageItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item SUSPICIOUS_BREW = new SuspiciousBrewItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item REPRISE = new RepriseItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item PURE_ALCOHOL = new DrinkItem(IS.of(16, Rarity.UNCOMMON).food(SpectrumFoodComponents.PURE_ALCOHOL).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item JADE_WINE = new JadeWineItem(IS.of(16, Rarity.UNCOMMON).food(SpectrumFoodComponents.BEVERAGE).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item CHRYSOCOLLA = new DrinkItem(IS.of(16, Rarity.UNCOMMON).food(SpectrumFoodComponents.PURE_ALCOHOL).recipeRemainder(Items.GLASS_BOTTLE));

	public static final Item HONEY_PASTRY = new Item(IS.of().food(SpectrumFoodComponents.HONEY_PASTRY));
	public static final Item LUCKY_ROLL = new Item(IS.of(16).food(SpectrumFoodComponents.LUCKY_ROLL));
	public static final Item TRIPLE_MEAT_POT_PIE = new CustomUseTimeItem(IS.of(8).food(SpectrumFoodComponents.TRIPLE_MEAT_POT_PIE), 96);
	public static final Item GLISTERING_JELLY_TEA = new TeaItem(IS.of(16).food(SpectrumFoodComponents.GLISTERING_JELLY_TEA).recipeRemainder(Items.GLASS_BOTTLE), SpectrumFoodComponents.GLISTERING_JELLY_TEA_SCONE_BONUS);
	public static final Item FREIGEIST = new FreigeistItem(IS.of(16).food(SpectrumFoodComponents.FREIGEIST).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Item DIVINATION_HEART = new Item(IS.of().food(SpectrumFoodComponents.DIVINATION_HEART));
	
	public static final Item STAR_CANDY = new StarCandyItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.STAR_CANDY));
	public static final Item PURPLE_STAR_CANDY = new EnchantedStarCandyItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.PURPLE_STAR_CANDY));
	
	public static final Item ENCHANTED_GOLDEN_CARROT = new ItemWithGlint(IS.of(Rarity.EPIC).food(SpectrumFoodComponents.ENCHANTED_GOLDEN_CARROT));
	public static final Item JARAMEL = new Item(IS.of().food(SpectrumFoodComponents.JARAMEL));
	
	public static final Item JARAMEL_TART = new Item(IS.of().food(SpectrumFoodComponents.JARAMEL_TART));
	public static final Item SALTED_JARAMEL_TART = new Item(IS.of().food(SpectrumFoodComponents.SALTED_JARAMEL_TART));
	public static final Item ASHEN_TART = new Item(IS.of().food(SpectrumFoodComponents.ASHEN_TART));
	public static final Item WEEPING_TART = new Item(IS.of().food(SpectrumFoodComponents.WEEPING_TART));
	public static final Item WHISPY_TART = new Item(IS.of().food(SpectrumFoodComponents.WHISPY_TART));
	public static final Item PUFF_TART = new Item(IS.of().food(SpectrumFoodComponents.PUFF_TART));
	
	public static final Item JARAMEL_TRIFLE = new Item(IS.of().food(SpectrumFoodComponents.JARAMEL_TRIFLE));
	public static final Item SALTED_JARAMEL_TRIFLE = new Item(IS.of().food(SpectrumFoodComponents.SALTED_JARAMEL_TRIFLE));
	public static final Item MONSTER_TRIFLE = new Item(IS.of().food(SpectrumFoodComponents.MONSTER_TRIFLE));
	public static final Item DEMON_TRIFLE = new Item(IS.of().food(SpectrumFoodComponents.DEMON_TRIFLE));

	public static final Item MYCEYLON = new CloakedItem(IS.of(), SpectrumCommon.locate("lategame/collect_myceylon"), Items.ORANGE_DYE);
	public static final Item MYCEYLON_APPLE_PIE = new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_APPLE_PIE));
	public static final Item MYCEYLON_PUMPKIN_PIE = new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_PUMPKIN_PIE));
	public static final Item MYCEYLON_COOKIE = new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_COOKIE));
	public static final Item ALOE_LEAF = new AliasedBlockItem(SpectrumBlocks.ALOE, IS.of().food(SpectrumFoodComponents.ALOE_LEAF));
	public static final Item SAWBLADE_HOLLY_BERRY = new AliasedBlockItem(SpectrumBlocks.SAWBLADE_HOLLY_BUSH, IS.of().food(FoodComponents.SWEET_BERRIES));
	public static final Item PRICKLY_BAYLEAF = new Item(IS.of().food(SpectrumFoodComponents.PRICKLY_BAYLEAF));
	public static final Item TRIPLE_MEAT_POT_STEW = new StackableStewItem(IS.of(8).food(SpectrumFoodComponents.TRIPLE_MEAT_POT_STEW), 96);
	public static final Item DRAGONBONE_BROTH = new StackableStewItem(IS.of(8).food(SpectrumFoodComponents.DRAGONBONE_BROTH));
	public static final Item DOOMBLOOM_SEED = new AliasedBlockItem(SpectrumBlocks.DOOMBLOOM, IS.of().fireproof());
	
	public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, IS.of());
	public static final Item AMARANTH_GRAINS = new AliasedBlockItem(SpectrumBlocks.AMARANTH, IS.of());
	
	public static final Item MELOCHITES_COOKBOOK_VOL_1 = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/melochites_cookbook_vol_1");
	public static final Item MELOCHITES_COOKBOOK_VOL_2 = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/melochites_cookbook_vol_2");
	public static final Item IMBRIFER_COOKBOOK = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/imbrifer_cookbook");
	public static final Item IMPERIAL_COOKBOOK = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/imperial_cookbook");
	public static final Item BREWERS_HANDBOOK = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/brewers_handbook");
	//public static final Item VARIA_COOKBOOK = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.UNCOMMON), "cuisine/cookbooks/varia_cookbook");
	public static final Item POISONERS_HANDBOOK = new CookbookItem(IS.of().maxCount(1).rarity(Rarity.EPIC), "dimension/lore/poisoners_handbook", SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR);

	public static final Item AQUA_REGIA = new JadeWineItem(IS.of(16).food(SpectrumFoodComponents.AQUA_REGIA));
	public static final Item BAGNUN = new Item(IS.of().food(SpectrumFoodComponents.BAGNUN));
	public static final Item BANYASH = new Item(IS.of().food(SpectrumFoodComponents.BANYASH));
	public static final Item BERLINER = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.BERLINER), 48);
	public static final Item BRISTLE_MEAD = new SimpleBeverageItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE));
	public static final Item CHAUVE_SOURIS_AU_VIN = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.CHAUVE_SOURIS_AU_VIN), 96);
	public static final Item CRAWFISH = new Item(IS.of().food(SpectrumFoodComponents.CRAWFISH));
	public static final Item CRAWFISH_COCKTAIL = new Item(IS.of().food(SpectrumFoodComponents.CRAWFISH_COCKTAIL));
	public static final Item CREAM_PASTRY = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.CREAM_PASTRY), 24);
	public static final Item FADED_KOI = new Item(IS.of().food(SpectrumFoodComponents.FADED_KOI));
	public static final Item FISHCAKE = new Item(IS.of().food(SpectrumFoodComponents.FISHCAKE));
	public static final Item LIZARD_MEAT = new Item(IS.of().food(SpectrumFoodComponents.LIZARD_MEAT));
	public static final Item COOKED_LIZARD_MEAT = new Item(IS.of().food(SpectrumFoodComponents.COOKED_LIZARD_MEAT));
	public static final Item GOLDEN_BRISTLE_TEA = new TeaItem(IS.of(16).food(SpectrumFoodComponents.GOLDEN_BRISTLE_TEA), SpectrumFoodComponents.GOLDEN_BRISTLE_TEA_SCONE_BONUS);
	public static final Item HARE_ROAST = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.HARE_ROAST), 64);
	public static final Item JUNKET = new Item(IS.of().food(SpectrumFoodComponents.JUNKET));
	public static final Item KOI = new Item(IS.of().food(SpectrumFoodComponents.KOI));
	public static final Item MEATLOAF = new Item(IS.of().food(SpectrumFoodComponents.MEATLOAF));
	public static final Item MEATLOAF_SANDWICH = new Item(IS.of().food(SpectrumFoodComponents.MEATLOAF_SANDWICH));
	public static final Item MELLOW_SHALLOT_SOUP = new Item(IS.of().food(SpectrumFoodComponents.MELLOW_SHALLOT_SOUP));
	public static final Item MORCHELLA = new SimpleBeverageItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE));
	public static final Item NECTERED_VIOGNIER = new JadeWineItem(IS.of(16).food(SpectrumFoodComponents.NECTERED_VIOGNIER));
	public static final Item PEACHES_FLAMBE = new CustomUseTimeItem(IS.of().food(SpectrumFoodComponents.PEACHES_FLAMBE), 64);
	public static final Item PEACH_CREAM = new TeaItem(IS.of(16).food(SpectrumFoodComponents.PEACH_CREAM), SpectrumFoodComponents.PEACH_CREAM_SCONE_BONUS);
	public static final Item PEACH_JAM = new Item(IS.of().food(SpectrumFoodComponents.PEACH_JAM));
	public static final Item RABBIT_CREAM_PIE = new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.RABBIT_CREAM_PIE), "item.spectrum.rabbit_cream_pie.tooltip");
	public static final Item SEDATIVES = new SedativesItem(IS.of().food(SpectrumFoodComponents.SEDATIVES), "item.spectrum.sedatives.tooltip");
	public static final Item SLUSHSLIDE = new SlushslideItem(IS.of(16).food(SpectrumFoodComponents.SLUSHSLIDE));
	public static final Item SURSTROMMING = new Item(IS.of().food(SpectrumFoodComponents.SURSTROMMING));
	public static final Item EVERNECTAR = new EvernectarItem(IS.of(1, Rarity.EPIC).food(SpectrumFoodComponents.EVERNECTAR).recipeRemainder(Items.GLASS_BOTTLE), "item.spectrum.evernectar.tooltip");

	// Banner Patterns
	public static final Item LOGO_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.SPECTRUM_LOGO_TAG, IS.of(1, Rarity.UNCOMMON));
	public static final Item AMETHYST_SHARD_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.AMETHYST_SHARD_TAG, IS.of(1));
	public static final Item AMETHYST_CLUSTER_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.AMETHYST_CLUSTER_TAG, IS.of(1));
	public static final Item ASTROLOGER_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.ASTROLOGER_TAG, IS.of(1, Rarity.UNCOMMON));
	public static final Item VELVET_ASTROLOGER_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.VELVET_ASTROLOGER_TAG, IS.of(1, Rarity.UNCOMMON));
	public static final Item POISONBLOOM_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.POISONBLOOM_TAG, IS.of(1, Rarity.RARE));
	public static final Item DEEP_LIGHT_BANNER_PATTERN = new BannerPatternItem(SpectrumBannerPatterns.DEEP_LIGHT_TAG, IS.of(1, Rarity.RARE));
	
	
	public static final Item BUCKET_OF_ERASER = new EmptyFluidEntityBucketItem(SpectrumEntityTypes.ERASER, Fluids.EMPTY, SoundEvents.ITEM_BUCKET_EMPTY, IS.of(1));
	
	public static final Item EGG_LAYING_WOOLY_PIG_SPAWN_EGG = new SpawnEggItem(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, 0x3a2c38, 0xfff2e0, IS.of());
	public static final Item PRESERVATION_TURRET_SPAWN_EGG = new SpawnEggItem(SpectrumEntityTypes.PRESERVATION_TURRET, 0xf3f6f8, 0xc8c5be, IS.of());
	public static final Item KINDLING_SPAWN_EGG = new SpawnEggItem(SpectrumEntityTypes.KINDLING, 0xda4261, 0xffd452, IS.of());
	public static final Item LIZARD_SPAWN_EGG = new SpawnEggItem(SpectrumEntityTypes.LIZARD, 0x896459, 0x503a40, IS.of());
	public static final Item ERASER_SPAWN_EGG = new SpawnEggItem(SpectrumEntityTypes.ERASER, 0x200d29, 0xc83e93, IS.of());
	
	// Magical Tools
	public static final Item BAG_OF_HOLDING = new BagOfHoldingItem(IS.of(1));
	public static final Item RADIANCE_STAFF = new RadianceStaffItem(IS.of(1, Rarity.UNCOMMON));
	public static final NaturesStaffItem NATURES_STAFF = new NaturesStaffItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item STAFF_OF_REMEMBRANCE = new StaffOfRemembranceItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item CONSTRUCTORS_STAFF = new ConstructorsStaffItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item EXCHANGING_STAFF = new ExchangeStaffItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item BLOCK_FLOODER = new BlockFlooderItem(IS.of(Rarity.UNCOMMON));
	public static final Item PIPE_BOMB = new PipeBombItem(IS.of(1));
	public static final EnderSpliceItem ENDER_SPLICE = new EnderSpliceItem(IS.of(16, Rarity.UNCOMMON));
	public static final Item PERTURBED_EYE = new PerturbedEyeItem(IS.of(Rarity.UNCOMMON));
	public static final Item CRESCENT_CLOCK = new ItemWithTooltip(IS.of(1), "item.spectrum.crescent_clock.tooltip");
	public static final Item PRIMORDIAL_LIGHTER = new PrimordialLighterItem(IS.of(1));
	
	public static final Item NIGHT_SALTS = new NightSaltsItem(IS.of(16));
	public static final Item SOOTHING_BOUQUET = new SoothingBouquetItem(IS.of(1, Rarity.RARE));
	public static final Item CONCEALING_OILS = new ConcealingOilsItem(IS.of(1));
	public static final Item BITTER_OILS = new DrinkItem(IS.of(16).food(SpectrumFoodComponents.BITTER_OILS));
	
	public static final Item INCANDESCENT_ESSENCE = new CloakedItem(IS.of().fireproof(), SpectrumCommon.locate("midgame/spectrum_midgame"), Items.ORANGE_DYE);
	public static final Item FROSTBITE_ESSENCE = new CloakedItem(IS.of(), SpectrumCommon.locate("midgame/spectrum_midgame"), Items.LIGHT_BLUE_DYE);
	public static final Item MOONSTONE_CORE = new CloakedItem(IS.of(16, Rarity.RARE), SpectrumCommon.locate("lategame/find_forgotten_city"), Items.WHITE_DYE);
	
	// Misc
	public static final Item MUSIC_DISC_DISCOVERY = new MusicDiscItem(1, SpectrumSoundEvents.MUSIC_DISCOVERY, IS.of(1, Rarity.RARE), 120);
	public static final Item MUSIC_DISC_CREDITS = new MusicDiscItem(2, SpectrumSoundEvents.MUSIC_CREDITS, IS.of(1, Rarity.RARE), 265);
	public static final Item MUSIC_DISC_DIVINITY = new MusicDiscItem(3, SpectrumSoundEvents.MUSIC_DIVINITY, IS.of(1, Rarity.RARE), 289);
	
	public static final Item PHANTOM_FRAME = new PhantomFrameItem(SpectrumEntityTypes.PHANTOM_FRAME, IS.of());
	public static final Item GLOW_PHANTOM_FRAME = new PhantomGlowFrameItem(SpectrumEntityTypes.GLOW_PHANTOM_FRAME, IS.of());
	
	public static final BottomlessBundleItem BOTTOMLESS_BUNDLE = new BottomlessBundleItem(IS.of(1));
	public static final KnowledgeGemItem KNOWLEDGE_GEM = new KnowledgeGemItem(IS.of(1, Rarity.UNCOMMON), 10000);
	public static final Item CELESTIAL_POCKETWATCH = new CelestialPocketWatchItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item ARTISANS_ATLAS = new ArtisansAtlasItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item GILDED_BOOK = new GildedBookItem(IS.of(Rarity.UNCOMMON));
	public static final Item ENCHANTMENT_CANVAS = new EnchantmentCanvasItem(IS.of(16, Rarity.UNCOMMON));
	public static final Item EVERPROMISE_RIBBON = new EverpromiseRibbonItem(IS.of());

	// Lore
	public static final Item MYSTERIOUS_LOCKET = new MysteriousLocketItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item MYSTERIOUS_COMPASS = new MysteriousCompassItem(IS.of(1, Rarity.RARE));
	
	// Trinkets
	public static final Item FANCIFUL_BELT = new Item(IS.of(16, Rarity.UNCOMMON));
	public static final Item FANCIFUL_PENDANT = new Item(IS.of(16, Rarity.UNCOMMON));
	public static final Item FANCIFUL_STONE_RING = new Item(IS.of(16, Rarity.UNCOMMON));
	public static final Item FANCIFUL_CIRCLET = new Item(IS.of(16, Rarity.UNCOMMON));
	public static final Item FANCIFUL_GLOVES = new Item(IS.of(16, Rarity.UNCOMMON));
	public static final Item FANCIFUL_BISMUTH_RING = new Item(IS.of(16, Rarity.UNCOMMON));
	
	public static final Item GLOW_VISION_GOGGLES = new GlowVisionGogglesItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item JEOPARDANT = new AttackRingItem(IS.of(1, Rarity.UNCOMMON));
	public static final SevenLeagueBootsItem SEVEN_LEAGUE_BOOTS = new SevenLeagueBootsItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item COTTON_CLOUD_BOOTS = new CottonCloudBootsItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item RADIANCE_PIN = new RadiancePinItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item TOTEM_PENDANT = new TotemPendantItem(IS.of(1, Rarity.UNCOMMON));
	public static final TakeOffBeltItem TAKE_OFF_BELT = new TakeOffBeltItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item AZURE_DIKE_BELT = new AzureDikeBeltItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item AZURE_DIKE_RING = new AzureDikeRingItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item AZURESQUE_DIKE_CORE = new AzureDikeCoreItem(IS.of(1, Rarity.EPIC));
	public static final InkDrainTrinketItem SHIELDGRASP_AMULET = new AzureDikeAmuletItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem HEARTSINGERS_REWARD = new ExtraHealthRingItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem GLOVES_OF_DAWNS_GRASP = new ExtraReachGlovesItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem RING_OF_PURSUIT = new ExtraMiningSpeedRingItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem RING_OF_DENSER_STEPS = new RingOfDenserStepsItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem RING_OF_AERIAL_GRACE = new RingOfAerialGraceItem(IS.of(1, Rarity.UNCOMMON));
	public static final InkDrainTrinketItem LAURELS_OF_SERENITY = new LaurelsOfSerenityItem(IS.of(1, Rarity.UNCOMMON));

	public static final InkFlaskItem INK_FLASK = new InkFlaskItem(IS.of(1), 64 * 64 * 100); // 64 stacks of pigments (1 pigment => 100 energy)
	public static final InkAssortmentItem INK_ASSORTMENT = new InkAssortmentItem(IS.of(1), 64 * 100);
	public static final PigmentPaletteItem PIGMENT_PALETTE = new PigmentPaletteItem(IS.of(1, Rarity.UNCOMMON), 64 * 64 * 100);
	public static final ArtistsPaletteItem ARTISTS_PALETTE = new ArtistsPaletteItem(IS.of(1, Rarity.UNCOMMON), 64 * 64 * 64 * 64 * 100);
	public static final CreativeInkAssortmentItem CREATIVE_INK_ASSORTMENT = new CreativeInkAssortmentItem(IS.of(1, Rarity.EPIC));
	
	public static final GleamingPinItem GLEAMING_PIN = new GleamingPinItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item LESSER_POTION_PENDANT = new PotionPendantItem(IS.of(1, Rarity.UNCOMMON), 1, SpectrumCommon.CONFIG.MaxLevelForEffectsInLesserPotionPendant - 1, SpectrumAdvancements.UNLOCK_LESSER_POTION_PENDANT);
	public static final Item GREATER_POTION_PENDANT = new PotionPendantItem(IS.of(1, Rarity.UNCOMMON), 3, SpectrumCommon.CONFIG.MaxLevelForEffectsInGreaterPotionPendant - 1, SpectrumAdvancements.UNLOCK_GREATER_POTION_PENDANT);
	public static final Item ASHEN_CIRCLET = new AshenCircletItem(IS.of(1, Rarity.UNCOMMON).fireproof());
	public static final Item WEEPING_CIRCLET = new WeepingCircletItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item PUFF_CIRCLET = new PuffCircletItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item WHISPY_CIRCLET = new WhispyCircletItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item CIRCLET_OF_ARROGANCE = new CircletOfArroganceItem(IS.of(1, Rarity.UNCOMMON));
	public static final Item NEAT_RING = new NeatRingItem(IS.of(1, Rarity.EPIC));

	public static final Item AETHER_GRACED_NECTAR_GLOVES = new AetherGracedNectarGlovesItem(IS.of(1, Rarity.EPIC));
	
	// Pure Clusters
	public static final Item PURE_EMERALD = new Item(IS.of());
	public static final Item PURE_PRISMARINE = new Item(IS.of());
	public static final Item PURE_COAL = new Item(IS.of());
	public static final Item PURE_REDSTONE = new Item(IS.of());
	public static final Item PURE_GLOWSTONE = new Item(IS.of());
	public static final Item PURE_LAPIS = new Item(IS.of());
	public static final Item PURE_COPPER = new Item(IS.of());
	public static final Item PURE_QUARTZ = new Item(IS.of());
	public static final Item PURE_GOLD = new Item(IS.of());
	public static final Item PURE_DIAMOND = new Item(IS.of());
	public static final Item PURE_IRON = new Item(IS.of());
	public static final Item PURE_NETHERITE_SCRAP = new Item(IS.of().fireproof());
	public static final Item PURE_ECHO = new Item(IS.of());

	//Technical Items
	public static final Item CONNECTION_NODE_CRYSTAL = new Item(IS.of());
	public static final Item PROVIDER_NODE_CRYSTAL = new Item(IS.of());
	public static final Item SENDER_NODE_CRYSTAL = new Item(IS.of());
	public static final Item STORAGE_NODE_CRYSTAL = new Item(IS.of());
	public static final Item BUFFER_NODE_CRYSTAL = new Item(IS.of());
	public static final Item GATHER_NODE_CRYSTAL = new Item(IS.of());


	public static void register(String name, Item item, DyeColor dyeColor) {
		Registry.register(Registries.ITEM, SpectrumCommon.locate(name), item);
		ItemColors.ITEM_COLORS.registerColorMapping(item, dyeColor);
	}
	
	public static void register() {
		register("guidebook", GUIDEBOOK, DyeColor.WHITE);
		register("paintbrush", PAINTBRUSH, DyeColor.WHITE);
		register("tuning_stamp", TUNING_STAMP, DyeColor.WHITE);
		
		registerGemstoneItems();
		registerPigments();
		registerResources();
		registerDecayBottles();
		registerPreEnchantedTools();
		registerConsumables();
		registerInkStorage();
		registerTrinkets();
		registerMagicalTools();
		registerFluidContainers();
		registerBannerPatterns();
		registerPureClusters();
		registerStructurePlacers();
		registerSpawningStuff();
		registerMusicDisks();
		registerTechnicalItems();
	}
	
	public static void registerMusicDisks() {
		register("music_disc_discovery", MUSIC_DISC_DISCOVERY, DyeColor.GREEN);
		register("music_disc_credits", MUSIC_DISC_CREDITS, DyeColor.GREEN);
		register("music_disc_divinity", MUSIC_DISC_DIVINITY, DyeColor.GREEN);
	}
	
	public static void registerSpawningStuff() {
		register("bucket_of_eraser", BUCKET_OF_ERASER, DyeColor.PINK);

		register("egg_laying_wooly_pig_spawn_egg", EGG_LAYING_WOOLY_PIG_SPAWN_EGG, DyeColor.WHITE);
		register("preservation_turret_spawn_egg", PRESERVATION_TURRET_SPAWN_EGG, DyeColor.WHITE);
		register("kindling_spawn_egg", KINDLING_SPAWN_EGG, DyeColor.WHITE);
		register("lizard_spawn_egg", LIZARD_SPAWN_EGG, DyeColor.WHITE);
		register("eraser_spawn_egg", ERASER_SPAWN_EGG, DyeColor.WHITE);
	}
	
	public static void registerPureClusters() {
		register("pure_coal", PURE_COAL, DyeColor.BROWN);
		register("pure_iron", PURE_IRON, DyeColor.BROWN);
		register("pure_gold", PURE_GOLD, DyeColor.BROWN);
		register("pure_diamond", PURE_DIAMOND, DyeColor.CYAN);
		register("pure_emerald", PURE_EMERALD, DyeColor.CYAN);
		register("pure_redstone", PURE_REDSTONE, DyeColor.RED);
		register("pure_lapis", PURE_LAPIS, DyeColor.PURPLE);
		register("pure_copper", PURE_COPPER, DyeColor.BROWN);
		register("pure_quartz", PURE_QUARTZ, DyeColor.BROWN);
		register("pure_glowstone", PURE_GLOWSTONE, DyeColor.YELLOW);
		register("pure_prismarine", PURE_PRISMARINE, DyeColor.CYAN);
		register("pure_netherite_scrap", PURE_NETHERITE_SCRAP, DyeColor.BROWN);
		register("pure_echo", PURE_ECHO, DyeColor.BROWN);
	}
	
	public static void registerStructurePlacers() {
		register("pedestal_tier_1_structure_placer", PEDESTAL_TIER_1_STRUCTURE_PLACER, DyeColor.WHITE);
		register("pedestal_tier_2_structure_placer", PEDESTAL_TIER_2_STRUCTURE_PLACER, DyeColor.WHITE);
		register("pedestal_tier_3_structure_placer", PEDESTAL_TIER_3_STRUCTURE_PLACER, DyeColor.WHITE);
		register("fusion_shrine_structure_placer", FUSION_SHRINE_STRUCTURE_PLACER, DyeColor.WHITE);
		register("enchanter_structure_placer", ENCHANTER_STRUCTURE_PLACER, DyeColor.WHITE);
		register("spirit_instiller_structure_placer", SPIRIT_INSTILLER_STRUCTURE_PLACER, DyeColor.WHITE);
		register("cinderhearth_structure_placer", CINDERHEARTH_STRUCTURE_PLACER, DyeColor.WHITE);
	}
	
	public static void registerBannerPatterns() {
		register("logo_banner_pattern", LOGO_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("amethyst_shard_banner_pattern", AMETHYST_SHARD_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("amethyst_cluster_banner_pattern", AMETHYST_CLUSTER_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("astrologer_banner_pattern", ASTROLOGER_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("velvet_astrologer_banner_pattern", VELVET_ASTROLOGER_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("poisonbloom_banner_pattern", POISONBLOOM_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
		register("deep_light_banner_pattern", DEEP_LIGHT_BANNER_PATTERN, DyeColor.LIGHT_BLUE);
	}
	
	public static void registerGemstoneItems() {
		register("topaz_shard", TOPAZ_SHARD, DyeColor.CYAN);
		register("citrine_shard", CITRINE_SHARD, DyeColor.YELLOW);
		register("onyx_shard", ONYX_SHARD, DyeColor.BLACK);
		register("moonstone_shard", MOONSTONE_SHARD, DyeColor.WHITE);
		register("spectral_shard", SPECTRAL_SHARD, DyeColor.WHITE);
		
		register("topaz_powder", TOPAZ_POWDER, DyeColor.CYAN);
		register("amethyst_powder", AMETHYST_POWDER, DyeColor.MAGENTA);
		register("citrine_powder", CITRINE_POWDER, DyeColor.YELLOW);
		register("onyx_powder", ONYX_POWDER, DyeColor.BLACK);
		register("moonstone_powder", MOONSTONE_POWDER, DyeColor.WHITE);
	}
	
	public static void registerPigments() {
		register("white_pigment", WHITE_PIGMENT, DyeColor.WHITE);
		register("orange_pigment", ORANGE_PIGMENT, DyeColor.ORANGE);
		register("magenta_pigment", MAGENTA_PIGMENT, DyeColor.MAGENTA);
		register("light_blue_pigment", LIGHT_BLUE_PIGMENT, DyeColor.LIGHT_BLUE);
		register("yellow_pigment", YELLOW_PIGMENT, DyeColor.YELLOW);
		register("lime_pigment", LIME_PIGMENT, DyeColor.LIME);
		register("pink_pigment", PINK_PIGMENT, DyeColor.PINK);
		register("gray_pigment", GRAY_PIGMENT, DyeColor.GRAY);
		register("light_gray_pigment", LIGHT_GRAY_PIGMENT, DyeColor.LIGHT_GRAY);
		register("cyan_pigment", CYAN_PIGMENT, DyeColor.CYAN);
		register("purple_pigment", PURPLE_PIGMENT, DyeColor.PURPLE);
		register("blue_pigment", BLUE_PIGMENT, DyeColor.BLUE);
		register("brown_pigment", BROWN_PIGMENT, DyeColor.BROWN);
		register("green_pigment", GREEN_PIGMENT, DyeColor.GREEN);
		register("red_pigment", RED_PIGMENT, DyeColor.RED);
		register("black_pigment", BLACK_PIGMENT, DyeColor.BLACK);
	}
	
	public static void registerResources() {
		register("shimmerstone_gem", SHIMMERSTONE_GEM, DyeColor.YELLOW);
		register("raw_azurite", RAW_AZURITE, DyeColor.BLUE);
		register("refined_azurite", REFINED_AZURITE, DyeColor.BLUE);
		register("paltaeria_fragments", PALTAERIA_FRAGMENTS, DyeColor.LIGHT_BLUE);
		register("paltaeria_gem", PALTAERIA_GEM, DyeColor.LIGHT_BLUE);
		register("stratine_fragments", STRATINE_FRAGMENTS, DyeColor.RED);
		register("stratine_gem", STRATINE_GEM, DyeColor.RED);
		register("pyrite_chunk", PYRITE_CHUNK, DyeColor.PURPLE);
		register("dragonbone_chunk", DRAGONBONE_CHUNK, DyeColor.GRAY);
		register("bone_ash", BONE_ASH, DyeColor.GRAY);
		register("effulgent_feather", EFFULGENT_FEATHER, DyeColor.YELLOW);
		register("raw_bloodstone", RAW_BLOODSTONE, DyeColor.RED);
		register("refined_bloodstone", REFINED_BLOODSTONE, DyeColor.RED);
		register("downstone_fragments", DOWNSTONE_FRAGMENTS, DyeColor.LIGHT_GRAY);
		register("resonance_shard", RESONANCE_SHARD, DyeColor.WHITE);
		register("aether_vestiges", AETHER_VESTIGES, DyeColor.WHITE);

		register("ash_flakes", ASH_FLAKES, DyeColor.LIGHT_GRAY);

		register("quitoxic_powder", QUITOXIC_POWDER, DyeColor.PURPLE);
		register("mermaids_gem", MERMAIDS_GEM, DyeColor.LIGHT_BLUE);
		register("storm_stone", STORM_STONE, DyeColor.YELLOW);
		register("star_fragment", STAR_FRAGMENT, DyeColor.PURPLE);
		register("stardust", STARDUST, DyeColor.PURPLE);
		register("blood_orchid_petal", BLOOD_ORCHID_PETAL, DyeColor.RED);
		
		register("hibernating_jade_vine_bulb", HIBERNATING_JADE_VINE_BULB, DyeColor.GRAY);
		register("germinated_jade_vine_bulb", GERMINATED_JADE_VINE_BULB, DyeColor.LIME);
		register("jade_vine_petals", JADE_VINE_PETALS, DyeColor.LIME);
		register("jadeite_petals", JADEITE_PETALS, DyeColor.BROWN);
		
		register("glistering_melon_seeds", GLISTERING_MELON_SEEDS, DyeColor.LIME);
		register("amaranth_grains", AMARANTH_GRAINS, DyeColor.LIME);
		
		register("vegetal", VEGETAL, DyeColor.LIME);
		register("neolith", NEOLITH, DyeColor.PINK);
		register("bedrock_dust", BEDROCK_DUST, DyeColor.BLACK);
		register("midnight_aberration", MIDNIGHT_ABERRATION, DyeColor.GRAY);
		register("midnight_chip", MIDNIGHT_CHIP, DyeColor.GRAY);
		
		register("bismuth_flake", BISMUTH_FLAKE, DyeColor.CYAN);
		register("bismuth_crystal", BISMUTH_CRYSTAL, DyeColor.CYAN);
		
		register("raw_malachite", RAW_MALACHITE, DyeColor.GREEN);
		register("refined_malachite", REFINED_MALACHITE, DyeColor.GREEN);
		
		register("incandescent_essence", INCANDESCENT_ESSENCE, DyeColor.ORANGE);
		register("frostbite_essence", FROSTBITE_ESSENCE, DyeColor.LIGHT_BLUE);
		
		register("moonstone_core", MOONSTONE_CORE, DyeColor.WHITE);
	}
	
	public static void registerDecayBottles() {
		register("bottle_of_fading", BOTTLE_OF_FADING, DyeColor.GRAY);
		register("bottle_of_failing", BOTTLE_OF_FAILING, DyeColor.GRAY);
		register("bottle_of_ruin", BOTTLE_OF_RUIN, DyeColor.GRAY);
		register("bottle_of_forfeiture", BOTTLE_OF_FORFEITURE, DyeColor.GRAY);
		register("bottle_of_decay_away", BOTTLE_OF_DECAY_AWAY, DyeColor.PINK);
	}
	
	public static void registerPreEnchantedTools() {
		register("multitool", MULTITOOL, DyeColor.BROWN);
		register("tender_pickaxe", TENDER_PICKAXE, DyeColor.BLUE);
		register("lucky_pickaxe", LUCKY_PICKAXE, DyeColor.LIGHT_BLUE);
		register("razor_falchion", RAZOR_FALCHION, DyeColor.RED);
		register("oblivion_pickaxe", OBLIVION_PICKAXE, DyeColor.GRAY);
		register("resonant_pickaxe", RESONANT_PICKAXE, DyeColor.WHITE);
		register("dragonrending_pickaxe", DRAGONRENDING_PICKAXE, DyeColor.WHITE);
		register("lagoon_rod", LAGOON_ROD, DyeColor.LIGHT_BLUE);
		register("molten_rod", MOLTEN_ROD, DyeColor.ORANGE);
		
		register("fetchling_helmet", FETCHLING_HELMET, DyeColor.BLUE);
		register("ferocious_chestplate", FEROCIOUS_CHESTPLATE, DyeColor.BLUE);
		register("sylph_leggings", SYLPH_LEGGINGS, DyeColor.BLUE);
		register("oread_boots", OREAD_BOOTS, DyeColor.BLUE);
		
		register("bedrock_pickaxe", BEDROCK_PICKAXE, DyeColor.BLACK);
		register("bedrock_axe", BEDROCK_AXE, DyeColor.BLACK);
		register("bedrock_shovel", BEDROCK_SHOVEL, DyeColor.BLACK);
		register("bedrock_sword", BEDROCK_SWORD, DyeColor.BLACK);
		register("bedrock_hoe", BEDROCK_HOE, DyeColor.BLACK);
		register("bedrock_bow", BEDROCK_BOW, DyeColor.BLACK);
		register("bedrock_crossbow", BEDROCK_CROSSBOW, DyeColor.BLACK);
		register("bedrock_shears", BEDROCK_SHEARS, DyeColor.BLACK);
		register("bedrock_fishing_rod", BEDROCK_FISHING_ROD, DyeColor.BLACK);

		register("bedrock_helmet", BEDROCK_HELMET, DyeColor.BLACK);
		register("bedrock_chestplate", BEDROCK_CHESTPLATE, DyeColor.BLACK);
		register("bedrock_leggings", BEDROCK_LEGGINGS, DyeColor.BLACK);
		register("bedrock_boots", BEDROCK_BOOTS, DyeColor.BLACK);

		register("malachite_workstaff", MALACHITE_WORKSTAFF, DyeColor.GREEN);
		register("malachite_ultra_greatsword", MALACHITE_ULTRA_GREATSWORD, DyeColor.GREEN);
		register("malachite_crossbow", MALACHITE_CROSSBOW, DyeColor.GREEN);
		register("malachite_bident", MALACHITE_BIDENT, DyeColor.GREEN);

		register("glass_crest_workstaff", GLASS_CREST_WORKSTAFF, DyeColor.WHITE);
		register("glass_crest_ultra_greatsword", GLASS_CREST_ULTRA_GREATSWORD, DyeColor.WHITE);
		register("ferocious_glass_crest_bident", FEROCIOUS_GLASS_CREST_BIDENT, DyeColor.WHITE);
		register("fractal_glass_crest_bident", FRACTAL_GLASS_CREST_BIDENT, DyeColor.WHITE);
		register("glass_crest_crossbow", GLASS_CREST_CROSSBOW, DyeColor.WHITE);
		
		register("malachite_glass_arrow", MALACHITE_GLASS_ARROW, DyeColor.GREEN);
		register("topaz_glass_arrow", TOPAZ_GLASS_ARROW, DyeColor.CYAN);
		register("amethyst_glass_arrow", AMETHYST_GLASS_ARROW, DyeColor.MAGENTA);
		register("citrine_glass_arrow", CITRINE_GLASS_ARROW, DyeColor.YELLOW);
		register("onyx_glass_arrow", ONYX_GLASS_ARROW, DyeColor.BLACK);
		register("moonstone_glass_arrow", MOONSTONE_GLASS_ARROW, DyeColor.WHITE);

		register("omni_accelerator", OMNI_ACCELERATOR, DyeColor.YELLOW);

		register("azurite_glass_ampoule", AZURITE_GLASS_AMPOULE, DyeColor.BLUE);
		register("bloodstone_glass_ampoule", BLOODSTONE_GLASS_AMPOULE, DyeColor.RED);
		register("malachite_glass_ampoule", MALACHITE_GLASS_AMPOULE, DyeColor.GREEN);
		//register("crystallized_dragon_fang", CRYSTALLIZED_DRAGON_FANG, DyeColor.WHITE);
		
		register("dreamflayer", DREAMFLAYER, DyeColor.RED);
		register("nightfalls_blade", NIGHTFALLS_BLADE, DyeColor.GRAY);
		register("draconic_twinsword", DRACONIC_TWINSWORD, DyeColor.YELLOW);
		register("dragon_talon", DRAGON_TALON, DyeColor.YELLOW);
		register("knotted_sword", KNOTTED_SWORD, DyeColor.GREEN);
		register("nectar_lance", NECTAR_LANCE, DyeColor.PURPLE);
	}
	
	public static void registerMagicalTools() {
		register("crafting_tablet", CRAFTING_TABLET, DyeColor.LIGHT_GRAY);
		register("bottomless_bundle", BOTTOMLESS_BUNDLE, DyeColor.LIGHT_GRAY);
		register("phantom_frame", PHANTOM_FRAME, DyeColor.YELLOW);
		register("glow_phantom_frame", GLOW_PHANTOM_FRAME, DyeColor.YELLOW);
		register("knowledge_gem", KNOWLEDGE_GEM, DyeColor.PURPLE);
		register("celestial_pocketwatch", CELESTIAL_POCKETWATCH, DyeColor.MAGENTA);
		register("artisans_atlas", ARTISANS_ATLAS, DyeColor.YELLOW);
		register("gilded_book", GILDED_BOOK, DyeColor.PURPLE);
		register("enchantment_canvas", ENCHANTMENT_CANVAS, DyeColor.PURPLE);
		register("everpromise_ribbon", EVERPROMISE_RIBBON, DyeColor.PINK);
		register("bag_of_holding", BAG_OF_HOLDING, DyeColor.PURPLE);
		register("radiance_staff", RADIANCE_STAFF, DyeColor.YELLOW);
		register("natures_staff", NATURES_STAFF, DyeColor.LIME);
		register("staff_of_remembrance", STAFF_OF_REMEMBRANCE, DyeColor.LIME);
		register("constructors_staff", CONSTRUCTORS_STAFF, DyeColor.LIGHT_GRAY);
		register("exchanging_staff", EXCHANGING_STAFF, DyeColor.LIGHT_GRAY);
		register("block_flooder", BLOCK_FLOODER, DyeColor.LIGHT_GRAY);
		register("pipe_bomb", PIPE_BOMB, DyeColor.ORANGE);
		register("ender_splice", ENDER_SPLICE, DyeColor.PURPLE);
		register("perturbed_eye", PERTURBED_EYE, DyeColor.RED);
		register("crescent_clock", CRESCENT_CLOCK, DyeColor.MAGENTA);
		register("primordial_lighter", PRIMORDIAL_LIGHTER, DyeColor.ORANGE);

		register("night_salts", NIGHT_SALTS, DyeColor.PURPLE);
		register("soothing_bouquet", SOOTHING_BOUQUET, DyeColor.PURPLE);
		register("concealing_oils", CONCEALING_OILS, DyeColor.BLACK);
		register("bitter_oils", BITTER_OILS, DyeColor.BLUE);

		register("mysterious_locket", MYSTERIOUS_LOCKET, DyeColor.GRAY);
		register("mysterious_compass", MYSTERIOUS_COMPASS, DyeColor.GRAY);
	}
	
	public static void registerConsumables() {
		register("rock_candy", ROCK_CANDY, DyeColor.PINK);
		register("topaz_rock_candy", TOPAZ_ROCK_CANDY, DyeColor.CYAN);
		register("amethyst_rock_candy", AMETHYST_ROCK_CANDY, DyeColor.MAGENTA);
		register("citrine_rock_candy", CITRINE_ROCK_CANDY, DyeColor.YELLOW);
		register("onyx_rock_candy", ONYX_ROCK_CANDY, DyeColor.BLACK);
		register("moonstone_rock_candy", MOONSTONE_ROCK_CANDY, DyeColor.WHITE);

		register("cheong", CHEONG, DyeColor.PINK);
		register("mermaids_jam", MERMAIDS_JAM, DyeColor.PINK);
		register("mermaids_popcorn", MERMAIDS_POPCORN, DyeColor.PINK);
		register("le_fishe_au_chocolat", LE_FISHE_AU_CHOCOLAT, DyeColor.PINK);
		//register("stuffed_petals", STUFFED_PETALS, DyeColor.PINK);
		//register("pastiche", PASTICHE, DyeColor.PINK);
		//register("vittorias_roast", VITTORIAS_ROAST, DyeColor.PINK);

		register("triple_meat_pot_pie", TRIPLE_MEAT_POT_PIE, DyeColor.PINK);
		register("kimchi", KIMCHI, DyeColor.PINK);
		
		register("clotted_cream", CLOTTED_CREAM, DyeColor.PINK);
		register("fresh_chocolate", FRESH_CHOCOLATE, DyeColor.PINK);
		register("bodacious_berry_bar", BODACIOUS_BERRY_BAR, DyeColor.PINK);

		register("hot_chocolate", HOT_CHOCOLATE, DyeColor.PINK);
		register("karak_chai", KARAK_CHAI, DyeColor.PINK);
		register("restoration_tea", RESTORATION_TEA, DyeColor.PINK);
		register("glistering_jelly_tea", GLISTERING_JELLY_TEA, DyeColor.PINK);
		register("azalea_tea", AZALEA_TEA, DyeColor.PURPLE);
		register("demon_tea", DEMON_TEA, DyeColor.RED);

		register("enchanted_golden_carrot", ENCHANTED_GOLDEN_CARROT, DyeColor.PINK);
		register("jade_jelly", JADE_JELLY, DyeColor.LIME);
		register("jaramel", JARAMEL, DyeColor.PINK);
		register("moonstruck_nectar", MOONSTRUCK_NECTAR, DyeColor.LIME);
		register("glass_peach", GLASS_PEACH, DyeColor.PINK);
		register("fissure_plum", FISSURE_PLUM, DyeColor.BROWN);
		register("nightdew_sprout", NIGHTDEW_SPROUT, DyeColor.PURPLE);
		register("nectardew_burgeon", NECTARDEW_BURGEON, DyeColor.PURPLE);
		register("bloodboil_syrup", BLOODBOIL_SYRUP, DyeColor.RED);
		register("milky_resin", MILKY_RESIN, DyeColor.LIGHT_GRAY);
		
		register("scone", SCONE, DyeColor.PINK);
		register("star_candy", STAR_CANDY, DyeColor.PINK);
		register("purple_star_candy", PURPLE_STAR_CANDY, DyeColor.PINK);
		register("lucky_roll", LUCKY_ROLL, DyeColor.PINK);
		register("honey_pastry", HONEY_PASTRY, DyeColor.PINK);
		
		register("jaramel_tart", JARAMEL_TART, DyeColor.PINK);
		register("salted_jaramel_tart", SALTED_JARAMEL_TART, DyeColor.PINK);
		register("ashen_tart", ASHEN_TART, DyeColor.PINK);
		register("weeping_tart", WEEPING_TART, DyeColor.PINK);
		register("whispy_tart", WHISPY_TART, DyeColor.PINK);
		register("puff_tart", PUFF_TART, DyeColor.PINK);

		register("jaramel_trifle", JARAMEL_TRIFLE, DyeColor.PINK);
		register("salted_jaramel_trifle", SALTED_JARAMEL_TRIFLE, DyeColor.PINK);
		register("monster_trifle", MONSTER_TRIFLE, DyeColor.PINK);
		register("demon_trifle", DEMON_TRIFLE, DyeColor.PINK);

		register("myceylon", MYCEYLON, DyeColor.PINK);
		register("myceylon_apple_pie", MYCEYLON_APPLE_PIE, DyeColor.PINK);
		register("myceylon_pumpkin_pie", MYCEYLON_PUMPKIN_PIE, DyeColor.PINK);
		register("myceylon_cookie", MYCEYLON_COOKIE, DyeColor.PINK);
		register("aloe_leaf", ALOE_LEAF, DyeColor.PINK);
		register("sawblade_holly_berry", SAWBLADE_HOLLY_BERRY, DyeColor.PINK);
		register("prickly_bayleaf", PRICKLY_BAYLEAF, DyeColor.PINK);
		register("triple_meat_pot_stew", TRIPLE_MEAT_POT_STEW, DyeColor.PINK);
		register("dragonbone_broth", DRAGONBONE_BROTH, DyeColor.GRAY);
		register("doombloom_seed", DOOMBLOOM_SEED, DyeColor.BLACK);
		
		register("infused_beverage", INFUSED_BEVERAGE, DyeColor.PINK);
		register("pure_alcohol", PURE_ALCOHOL, DyeColor.WHITE);
		register("reprise", REPRISE, DyeColor.PINK);
		register("suspicious_brew", SUSPICIOUS_BREW, DyeColor.LIME);
		register("jade_wine", JADE_WINE, DyeColor.LIME);
		register("chrysocolla", CHRYSOCOLLA, DyeColor.LIME);
		register("evernectar", EVERNECTAR, DyeColor.LIME);
		register("freigeist", FREIGEIST, DyeColor.RED);
		register("divination_heart", DIVINATION_HEART, DyeColor.RED);
		
		register("imbrifer_cookbook", IMBRIFER_COOKBOOK, DyeColor.PURPLE);
		register("imperial_cookbook", IMPERIAL_COOKBOOK, DyeColor.PURPLE);
		register("melochites_cookbook_vol_1", MELOCHITES_COOKBOOK_VOL_1, DyeColor.PURPLE);
		register("melochites_cookbook_vol_2", MELOCHITES_COOKBOOK_VOL_2, DyeColor.PURPLE);
		register("brewers_handbook", BREWERS_HANDBOOK, DyeColor.PURPLE);
		//register("varia_cookbook", VARIA_COOKBOOK, DyeColor.PURPLE);
		register("poisoners_handbook", POISONERS_HANDBOOK, DyeColor.PURPLE);

		register("aqua_regia", AQUA_REGIA, DyeColor.PINK);
		register("bagnun", BAGNUN, DyeColor.PINK);
		register("banyash", BANYASH, DyeColor.PINK);
		register("berliner", BERLINER, DyeColor.PINK);
		register("bristle_mead", BRISTLE_MEAD, DyeColor.PINK);
		register("chauve_souris_au_vin", CHAUVE_SOURIS_AU_VIN, DyeColor.PINK);
		register("crawfish", CRAWFISH, DyeColor.PINK);
		register("crawfish_cocktail", CRAWFISH_COCKTAIL, DyeColor.PINK);
		register("cream_pastry", CREAM_PASTRY, DyeColor.PINK);
		register("faded_koi", FADED_KOI, DyeColor.PINK);
		register("fishcake", FISHCAKE, DyeColor.PINK);
		register("lizard_meat", LIZARD_MEAT, DyeColor.PINK);
		register("cooked_lizard_meat", COOKED_LIZARD_MEAT, DyeColor.PINK);
		register("golden_bristle_tea", GOLDEN_BRISTLE_TEA, DyeColor.PINK);
		register("hare_roast", HARE_ROAST, DyeColor.PINK);
		register("junket", JUNKET, DyeColor.PINK);
		register("koi", KOI, DyeColor.PINK);
		register("meatloaf", MEATLOAF, DyeColor.PINK);
		register("meatloaf_sandwich", MEATLOAF_SANDWICH, DyeColor.PINK);
		register("mellow_shallot_soup", MELLOW_SHALLOT_SOUP, DyeColor.PINK);
		register("morchella", MORCHELLA, DyeColor.PINK);
		register("nectered_viognier", NECTERED_VIOGNIER, DyeColor.PINK);
		register("peaches_flambe", PEACHES_FLAMBE, DyeColor.PINK);
		register("peach_cream", PEACH_CREAM, DyeColor.PINK);
		register("peach_jam", PEACH_JAM, DyeColor.PINK);
		register("rabbit_cream_pie", RABBIT_CREAM_PIE, DyeColor.PINK);
		register("sedatives", SEDATIVES, DyeColor.PINK);
		register("slushslide", SLUSHSLIDE, DyeColor.PINK);
		register("surstromming", SURSTROMMING, DyeColor.PINK);
	}
	
	public static void registerInkStorage() {
		register("ink_flask", INK_FLASK, DyeColor.WHITE);
		register("ink_assortment", INK_ASSORTMENT, DyeColor.WHITE);
		register("pigment_palette", PIGMENT_PALETTE, DyeColor.WHITE);
		register("artists_palette", ARTISTS_PALETTE, DyeColor.WHITE);
		register("creative_ink_assortment", CREATIVE_INK_ASSORTMENT, DyeColor.WHITE);
	}
	
	public static void registerTrinkets() {
		register("fanciful_stone_ring", FANCIFUL_STONE_RING, DyeColor.GREEN);
		register("fanciful_belt", FANCIFUL_BELT, DyeColor.GREEN);
		register("fanciful_pendant", FANCIFUL_PENDANT, DyeColor.GREEN);
		register("fanciful_circlet", FANCIFUL_CIRCLET, DyeColor.GREEN);
		register("fanciful_gloves", FANCIFUL_GLOVES, DyeColor.GREEN);
		register("fanciful_bismuth_ring", FANCIFUL_BISMUTH_RING, DyeColor.GREEN);
		
		register("glow_vision_goggles", GLOW_VISION_GOGGLES, DyeColor.WHITE);
		register("jeopardant", JEOPARDANT, DyeColor.RED);
		register("seven_league_boots", SEVEN_LEAGUE_BOOTS, DyeColor.PURPLE);
		register("cotton_cloud_boots", COTTON_CLOUD_BOOTS, DyeColor.PURPLE);
		register("radiance_pin", RADIANCE_PIN, DyeColor.BLUE);
		register("totem_pendant", TOTEM_PENDANT, DyeColor.BLUE);
		register("take_off_belt", TAKE_OFF_BELT, DyeColor.YELLOW);
		register("azure_dike_belt", AZURE_DIKE_BELT, DyeColor.BLUE);
		register("azure_dike_ring", AZURE_DIKE_RING, DyeColor.BLUE);
		register("azuresque_dike_core", AZURESQUE_DIKE_CORE, DyeColor.WHITE);
		register("shieldgrasp_amulet", SHIELDGRASP_AMULET, DyeColor.BLUE);
		register("heartsingers_reward", HEARTSINGERS_REWARD, DyeColor.PINK);
		register("gloves_of_dawns_grasp", GLOVES_OF_DAWNS_GRASP, DyeColor.YELLOW);
		register("ring_of_pursuit", RING_OF_PURSUIT, DyeColor.MAGENTA);
		register("ring_of_denser_steps", RING_OF_DENSER_STEPS, DyeColor.BROWN);
		register("ring_of_aerial_grace", RING_OF_AERIAL_GRACE, DyeColor.WHITE);
		register("laurels_of_serenity", LAURELS_OF_SERENITY, DyeColor.PURPLE);
		register("gleaming_pin", GLEAMING_PIN, DyeColor.YELLOW);
		register("lesser_potion_pendant", LESSER_POTION_PENDANT, DyeColor.PINK);
		register("greater_potion_pendant", GREATER_POTION_PENDANT, DyeColor.PINK);
		register("ashen_circlet", ASHEN_CIRCLET, DyeColor.ORANGE);
		register("weeping_circlet", WEEPING_CIRCLET, DyeColor.LIGHT_BLUE);
		register("puff_circlet", PUFF_CIRCLET, DyeColor.WHITE);
		register("whispy_circlet", WHISPY_CIRCLET, DyeColor.BROWN);
		register("circlet_of_arrogance", CIRCLET_OF_ARROGANCE, DyeColor.RED);
		register("neat_ring", NEAT_RING, DyeColor.GREEN);
		register("aether_graced_nectar_gloves", AETHER_GRACED_NECTAR_GLOVES, DyeColor.PURPLE);
	}
	
	public static void registerFluidContainers() {
		register("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET, DyeColor.LIGHT_GRAY);
		register("mud_bucket", MUD_BUCKET, DyeColor.BROWN);
		register("midnight_solution_bucket", MIDNIGHT_SOLUTION_BUCKET, DyeColor.GRAY);
		register("dragonrot_bucket", DRAGONROT_BUCKET, DyeColor.LIGHT_GRAY);
		FluidStorage.combinedItemApiProvider(SpectrumItems.MERMAIDS_GEM).register(context ->
				new RemainderlessItemFluidStorage(context, FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET));
	}

	public static void registerTechnicalItems() {
		register("connection_node_crystal", CONNECTION_NODE_CRYSTAL, DyeColor.LIGHT_GRAY);
		register("provider_node_crystal", PROVIDER_NODE_CRYSTAL, DyeColor.MAGENTA);
		register("sender_node_crystal", SENDER_NODE_CRYSTAL, DyeColor.YELLOW);
		register("storage_node_crystal", STORAGE_NODE_CRYSTAL, DyeColor.CYAN);
		register("buffer_node_crystal", BUFFER_NODE_CRYSTAL, DyeColor.GREEN);
		register("gather_node_crystal", GATHER_NODE_CRYSTAL, DyeColor.BLACK);
	}
	
	public static void registerFuelRegistry() {
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), 12800);

		FuelRegistry.INSTANCE.add(SpectrumBlocks.LIGHT_LEVEL_DETECTOR.asItem(), 300);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEATHER_DETECTOR.asItem(), 300);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.ITEM_DETECTOR.asItem(), 300);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.PLAYER_DETECTOR.asItem(), 300);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.ENTITY_DETECTOR.asItem(), 300);

		FuelRegistry.INSTANCE.add(SpectrumItems.PURE_COAL, 3200);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.PURE_COAL_BLOCK, 32000);
		
		FuelRegistry.INSTANCE.add(SpectrumItems.VEGETAL, 800);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.VEGETAL_BLOCK, 8000);
		
		FuelRegistry.INSTANCE.add(SpectrumItems.PURE_ALCOHOL, 16000);
		FuelRegistry.INSTANCE.add(SpectrumItems.CHRYSOCOLLA, 16000);

		FuelRegistry.INSTANCE.add(SpectrumItems.INCANDESCENT_ESSENCE, 2400);

		FuelRegistry.INSTANCE.add(SpectrumItemTags.COLORED_FENCES, 300);
		FuelRegistry.INSTANCE.add(SpectrumItemTags.COLORED_FENCE_GATES, 300);

		// gala wood burns twice as long as normal
		FuelRegistry.INSTANCE.add(SpectrumItemTags.WEEPING_GALA_LOGS, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_PLANKS, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_STAIRS, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_DOOR, 400);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_FENCE, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_PRESSURE_PLATE, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_TRAPDOOR, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_FENCE_GATE, 600);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_BUTTON, 200);
		FuelRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_SLAB, 300);
	}
	
}
