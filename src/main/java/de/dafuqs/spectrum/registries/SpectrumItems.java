package de.dafuqs.spectrum.registries;

import com.klikli_dev.modonomicon.registry.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.items.ink.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.items.item_frame.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.magic_items.ampoules.*;
import de.dafuqs.spectrum.items.map.*;
import de.dafuqs.spectrum.items.misc.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.food.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;
import static net.minecraft.world.item.Items.*;

//TODO: Migrate tools to use tool components
@SuppressWarnings("unused")
public class SpectrumItems {
	
	public static final DeferredRegister.Items REGISTRAR = DeferredRegister.createItems(SpectrumCommon.MOD_ID);
	
	// Main items
	public static final DeferredItem<GuidebookItem> GUIDEBOOK = register("guidebook", () -> new GuidebookItem(IS.of(1).component(DataComponentRegistry.BOOK_ID, GuidebookItem.GUIDEBOOK_ID)));
	public static final DeferredItem<Item> PAINTBRUSH = register("paintbrush", () -> new PaintbrushItem(IS.of(1)));
	public static final DeferredItem<Item> CRAFTING_TABLET = register("crafting_tablet", () -> new CraftingTabletItem(IS.of(1)));
	
	// Structure placers
	public static final DeferredItem<Item> PEDESTAL_TIER_1_STRUCTURE_PLACER = register("pedestal_tier_1_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_SIMPLE));
	public static final DeferredItem<Item> PEDESTAL_TIER_2_STRUCTURE_PLACER = register("pedestal_tier_2_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_ADVANCED));
	public static final DeferredItem<Item> PEDESTAL_TIER_3_STRUCTURE_PLACER = register("pedestal_tier_3_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.PEDESTAL_COMPLEX));
	public static final DeferredItem<Item> FUSION_SHRINE_STRUCTURE_PLACER = register("fusion_shrine_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.FUSION_SHRINE));
	public static final DeferredItem<Item> ENCHANTER_STRUCTURE_PLACER = register("enchanter_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.ENCHANTER));
	public static final DeferredItem<Item> SPIRIT_INSTILLER_STRUCTURE_PLACER = register("spirit_instiller_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.SPIRIT_INSTILLER));
	public static final DeferredItem<Item> CINDERHEARTH_STRUCTURE_PLACER = register("cinderhearth_structure_placer", () -> new StructurePlacerItem(IS.of(1), SpectrumMultiblocks.CINDERHEARTH));
	
	// Gem shards and powders
	public static final DeferredItem<Item> TOPAZ_SHARD = register("topaz_shard", () -> new Item(IS.of()));
	public static final DeferredItem<Item> CITRINE_SHARD = register("citrine_shard", () -> new Item(IS.of()));
	public static final DeferredItem<Item> ONYX_SHARD = register("onyx_shard", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_ALL_BASIC_PIGMENTS_BESIDES_BROWN, BLACK_DYE));
	public static final DeferredItem<Item> MOONSTONE_SHARD = register("moonstone_shard", () -> new CloakedItem(IS.of(), SpectrumAdvancements.BREAK_DECAYED_BEDROCK, WHITE_DYE));
	public static final DeferredItem<Item> SPECTRAL_SHARD = register("spectral_shard", () -> new Item(IS.of(Rarity.RARE)));
	
	public static final DeferredItem<Item> TOPAZ_POWDER = register("topaz_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_TOPAZ, CYAN_DYE));
	public static final DeferredItem<Item> AMETHYST_POWDER = register("amethyst_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_AMETHYST, MAGENTA_DYE));
	public static final DeferredItem<Item> CITRINE_POWDER = register("citrine_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_CITRINE, YELLOW_DYE));
	public static final DeferredItem<Item> ONYX_POWDER = register("onyx_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.CREATE_ONYX, BLACK_DYE));
	public static final DeferredItem<Item> MOONSTONE_POWDER = register("moonstone_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_MOONSTONE, WHITE_DYE));
	
	// Pigment
	public static final DeferredItem<Item> WHITE_PIGMENT = register("white_pigment", () -> new PigmentItem(IS.of(), InkColors.WHITE, WHITE_DYE));
	public static final DeferredItem<Item> ORANGE_PIGMENT = register("orange_pigment", () -> new PigmentItem(IS.of(), InkColors.ORANGE, ORANGE_DYE));
	public static final DeferredItem<Item> MAGENTA_PIGMENT = register("magenta_pigment", () -> new PigmentItem(IS.of(), InkColors.MAGENTA, MAGENTA_DYE));
	public static final DeferredItem<Item> LIGHT_BLUE_PIGMENT = register("light_blue_pigment", () -> new PigmentItem(IS.of(), InkColors.LIGHT_BLUE, LIGHT_BLUE_DYE));
	public static final DeferredItem<Item> YELLOW_PIGMENT = register("yellow_pigment", () -> new PigmentItem(IS.of(), InkColors.YELLOW, YELLOW_DYE));
	public static final DeferredItem<Item> LIME_PIGMENT = register("lime_pigment", () -> new PigmentItem(IS.of(), InkColors.LIME, LIME_DYE));
	public static final DeferredItem<Item> PINK_PIGMENT = register("pink_pigment", () -> new PigmentItem(IS.of(), InkColors.PINK, PINK_DYE));
	public static final DeferredItem<Item> GRAY_PIGMENT = register("gray_pigment", () -> new PigmentItem(IS.of(), InkColors.GRAY, GRAY_DYE));
	public static final DeferredItem<Item> LIGHT_GRAY_PIGMENT = register("light_gray_pigment", () -> new PigmentItem(IS.of(), InkColors.LIGHT_GRAY, LIGHT_GRAY_DYE));
	public static final DeferredItem<Item> CYAN_PIGMENT = register("cyan_pigment", () -> new PigmentItem(IS.of(), InkColors.CYAN, CYAN_DYE));
	public static final DeferredItem<Item> PURPLE_PIGMENT = register("purple_pigment", () -> new PigmentItem(IS.of(), InkColors.PURPLE, PURPLE_DYE));
	public static final DeferredItem<Item> BLUE_PIGMENT = register("blue_pigment", () -> new PigmentItem(IS.of(), InkColors.BLUE, BLUE_DYE));
	public static final DeferredItem<Item> BROWN_PIGMENT = register("brown_pigment", () -> new PigmentItem(IS.of(), InkColors.BROWN, BROWN_DYE));
	public static final DeferredItem<Item> GREEN_PIGMENT = register("green_pigment", () -> new PigmentItem(IS.of(), InkColors.GREEN, GREEN_DYE));
	public static final DeferredItem<Item> RED_PIGMENT = register("red_pigment", () -> new PigmentItem(IS.of(), InkColors.RED, RED_DYE));
	public static final DeferredItem<Item> BLACK_PIGMENT = register("black_pigment", () -> new PigmentItem(IS.of(), InkColors.BLACK, BLACK_DYE));
	
	// Preenchanted tools
	public static final DeferredItem<PreenchantedMultiToolItem> MULTITOOL = register("multitool", () -> new PreenchantedMultiToolItem(Tiers.IRON, 2, -2.4F, IS.of(Rarity.UNCOMMON).durability(Tiers.IRON.getUses())));
	public static final DeferredItem<GlintlessPickaxe> TENDER_PICKAXE = register("tender_pickaxe", () -> new GlintlessPickaxe(SpectrumToolTiers.GEMSTONE, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.GEMSTONE.getUses()).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.GEMSTONE, 1, -2.8F))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.SILK_TOUCH, 1);
		}
	});
	public static final DeferredItem<GlintlessPickaxe> LUCKY_PICKAXE = register("lucky_pickaxe", () -> new GlintlessPickaxe(SpectrumToolTiers.GEMSTONE, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.GEMSTONE.getUses()).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.GEMSTONE, 1, -2.8F))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FORTUNE, 3);
		}
	});
	public static final DeferredItem<RazorFalchionItem> RAZOR_FALCHION = register("razor_falchion", () -> new RazorFalchionItem(SpectrumToolTiers.GEMSTONE, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.GEMSTONE.getUses()).attributes(SwordItem.createAttributes(SpectrumToolTiers.GEMSTONE, 4, -2.2F))));
	public static final DeferredItem<OblivionPickaxeItem> OBLIVION_PICKAXE = register("oblivion_pickaxe", () -> new OblivionPickaxeItem(SpectrumToolTiers.OBLIVION, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.OBLIVION.getUses()).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.OBLIVION, 1, -2.8F))));
	public static final DeferredItem<GlintlessPickaxe> RESONANT_PICKAXE = register("resonant_pickaxe", () -> new GlintlessPickaxe(SpectrumToolTiers.GEMSTONE_MINING_LEVEL_4, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.GEMSTONE.getUses()).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.GEMSTONE_MINING_LEVEL_4, 1, -2.8F))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(SpectrumEnchantmentKeys.RESONANCE, 1);
		}
	});
	public static final DeferredItem<GlintlessPickaxe> DRAGONRENDING_PICKAXE = register("dragonrending_pickaxe", () -> new GlintlessPickaxe(SpectrumToolTiers.DRACONIC, IS.of(Rarity.UNCOMMON).durability(SpectrumToolTiers.DRACONIC.getUses()).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.DRACONIC, 1, -2.8F))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(SpectrumEnchantmentKeys.RAZING, 3);
		}
	});
	public static final DeferredItem<LagoonRodItem> LAGOON_ROD = register("lagoon_rod", () -> new LagoonRodItem(IS.of().durability(256)));
	public static final DeferredItem<MoltenRodItem> MOLTEN_ROD = register("molten_rod", () -> new MoltenRodItem(IS.of().durability(256)));
	
	// Bedrock Tools
	public static final DeferredItem<SpectrumPickaxeItem> BEDROCK_PICKAXE = register("bedrock_pickaxe", () -> new SpectrumPickaxeItem(SpectrumToolTiers.BEDROCK, IS.of(Rarity.UNCOMMON).attributes(PickaxeItem.createAttributes(SpectrumToolTiers.BEDROCK, 1, -2.8F)).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.SILK_TOUCH, 1);
		}
	});
	public static final DeferredItem<BedrockAxeItem> BEDROCK_AXE = register("bedrock_axe", () -> new BedrockAxeItem(SpectrumToolTiers.BEDROCK, IS.of(Rarity.UNCOMMON).attributes(AxeItem.createAttributes(SpectrumToolTiers.BEDROCK, 5, -3.0F)).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockShovelItem> BEDROCK_SHOVEL = register("bedrock_shovel", () -> new BedrockShovelItem(SpectrumToolTiers.BEDROCK, IS.of(Rarity.UNCOMMON).attributes(ShovelItem.createAttributes(SpectrumToolTiers.BEDROCK, 1, -3.0F)).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockSwordItem> BEDROCK_SWORD = register("bedrock_sword", () -> new BedrockSwordItem(SpectrumToolTiers.BEDROCK, IS.of(Rarity.UNCOMMON).attributes(SwordItem.createAttributes(SpectrumToolTiers.BEDROCK, 4, -2.4F)).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockHoeItem> BEDROCK_HOE = register("bedrock_hoe", () -> new BedrockHoeItem(SpectrumToolTiers.BEDROCK, IS.of(Rarity.UNCOMMON).attributes(HoeItem.createAttributes(SpectrumToolTiers.BEDROCK, 2, -0.0F)).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockBowItem> BEDROCK_BOW = register("bedrock_bow", () -> new BedrockBowItem(IS.of(Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockCrossbowItem> BEDROCK_CROSSBOW = register("bedrock_crossbow", () -> new BedrockCrossbowItem(IS.of(Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	public static final DeferredItem<BedrockShearsItem> BEDROCK_SHEARS = register("bedrock_shears", () -> new BedrockShearsItem(IS.of(Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false)).component(DataComponents.TOOL, ShearsItem.createToolProperties())));
	public static final DeferredItem<BedrockFishingRodItem> BEDROCK_FISHING_ROD = register("bedrock_fishing_rod", () -> new BedrockFishingRodItem(IS.of(Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.BEDROCK.getUses()).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));
	
	public static final DeferredItem<WorkstaffItem> MALACHITE_WORKSTAFF = register("malachite_workstaff", () -> new WorkstaffItem(SpectrumToolTiers.MALACHITE, 1, -3.2F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<GreatswordItem> MALACHITE_ULTRA_GREATSWORD = register("malachite_ultra_greatsword", () -> new GreatswordItem(SpectrumToolTiers.MALACHITE, 7, -2.8F, 1.0F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<MalachiteCrossbowItem> MALACHITE_CROSSBOW = register("malachite_crossbow", () -> new MalachiteCrossbowItem(IS.of(1, Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.MALACHITE.getUses())));
	public static final DeferredItem<MalachiteBidentItem> MALACHITE_BIDENT = register("malachite_bident", () -> new MalachiteBidentItem(IS.of(1, Rarity.UNCOMMON).durability(SpectrumToolTiers.MALACHITE.getUses()), -2.4, 9, 0.25F, 0F, false));
	
	// variants by socketing a moonstone core
	public static final DeferredItem<GlassCrestWorkstaffItem> GLASS_CREST_WORKSTAFF = register("glass_crest_workstaff", () -> new GlassCrestWorkstaffItem(SpectrumToolTiers.GLASS_CREST, 1, -2.8F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<GlassCrestGreatswordItem> GLASS_CREST_ULTRA_GREATSWORD = register("glass_crest_ultra_greatsword", () -> new GlassCrestGreatswordItem(SpectrumToolTiers.GLASS_CREST, 5, -2.8F, 1.0F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<GlassCrestCrossbowItem> GLASS_CREST_CROSSBOW = register("glass_crest_crossbow", () -> new GlassCrestCrossbowItem(IS.of(1, Rarity.UNCOMMON).fireResistant().durability(SpectrumToolTiers.GLASS_CREST.getUses())));
	public static final DeferredItem<FerociousBidentItem> FEROCIOUS_GLASS_CREST_BIDENT = register("ferocious_glass_crest_bident", () -> new FerociousBidentItem(IS.of(1, Rarity.UNCOMMON).durability(SpectrumToolTiers.GLASS_CREST.getUses()).component(SpectrumDataComponentTypes.ACTIVATED.get(), Unit.INSTANCE), -2.2, 13, 0.33F, 0.33F));
	public static final DeferredItem<FractalBidentItem> FRACTAL_GLASS_CREST_BIDENT = register("fractal_glass_crest_bident", () -> new FractalBidentItem(IS.of(1, Rarity.UNCOMMON).durability(SpectrumToolTiers.GLASS_CREST.getUses()).component(SpectrumDataComponentTypes.ACTIVATED.get(), Unit.INSTANCE), -2.4, 6.5, 0.25F, 0.25F));
	
	public static final DeferredItem<Item> MALACHITE_GLASS_ARROW = register("malachite_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.MALACHITE, ColoredCraftingParticleEffect.LIME));
	public static final DeferredItem<Item> TOPAZ_GLASS_ARROW = register("topaz_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.TOPAZ, ColoredCraftingParticleEffect.CYAN));
	public static final DeferredItem<Item> AMETHYST_GLASS_ARROW = register("amethyst_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.AMETHYST, ColoredCraftingParticleEffect.MAGENTA));
	public static final DeferredItem<Item> CITRINE_GLASS_ARROW = register("citrine_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.CITRINE, ColoredCraftingParticleEffect.YELLOW));
	public static final DeferredItem<Item> ONYX_GLASS_ARROW = register("onyx_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.ONYX, ColoredCraftingParticleEffect.BLACK));
	public static final DeferredItem<Item> MOONSTONE_GLASS_ARROW = register("moonstone_glass_arrow", () -> new GlassArrowItem(IS.of(Rarity.UNCOMMON), GlassArrowVariant.MOONSTONE, ColoredCraftingParticleEffect.WHITE));
	
	public static final DeferredItem<Item> OMNI_ACCELERATOR = register("omni_accelerator", () -> new OmniAcceleratorItem(IS.of(1, Rarity.UNCOMMON).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));
	
	public static final DeferredItem<Item> AZURITE_GLASS_AMPOULE = register("azurite_glass_ampoule", () -> new AzuriteGlassAmpouleItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> BLOODSTONE_GLASS_AMPOULE = register("bloodstone_glass_ampoule", () -> new BloodstoneGlassAmpouleItem(IS.of(Rarity.UNCOMMON).attributes(BloodstoneGlassAmpouleItem.createAttributeModifiers())));
	public static final DeferredItem<Item> MALACHITE_GLASS_AMPOULE = register("malachite_glass_ampoule", () -> new MalachiteGlassAmpouleItem(IS.of(Rarity.UNCOMMON)));
	
	// Special tools
	// TODO: set attribute modifiers similarly to how vanilla swords do it
	public static final DeferredItem<DreamflayerItem> DREAMFLAYER = register("dreamflayer", () -> new DreamflayerItem(SpectrumToolTiers.DREAMFLAYER, 3, -1.8F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<NightfallsBladeItem> NIGHTFALLS_BLADE = register("nightfalls_blade", () -> new NightfallsBladeItem(SpectrumToolTiers.NIGHTFALLS_BLADE, 3, -2.4F, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<DraconicTwinswordItem> DRACONIC_TWINSWORD = register("draconic_twinsword", () -> new DraconicTwinswordItem(SpectrumToolTiers.DRACONIC, IS.of(1, Rarity.RARE).attributes(DraconicTwinswordItem.createAttributes())));
	public static final DeferredItem<DragonTalonItem> DRAGON_TALON = register("dragon_talon", () -> new DragonTalonItem(SpectrumToolTiers.DRACONIC, -3.0, -1.0, IS.of(1, Rarity.RARE).durability(SpectrumToolTiers.DRACONIC.getUses())));
	public static final DeferredItem<LightGreatswordItem> KNOTTED_SWORD = register("knotted_sword", () -> new LightGreatswordItem(SpectrumToolTiers.KNOTTED_SWORD, 3, -2.4F, 0.25F, 0.5F, 0xFFd4d6ff, IS.of(1, Rarity.UNCOMMON).durability(SpectrumToolTiers.KNOTTED_SWORD.getUses())));
	public static final DeferredItem<NectarLanceItem> NECTAR_LANCE = register("nectar_lance", () -> new NectarLanceItem(SpectrumToolTiers.NECTAR_LANCE, 0, -2.4F, 0.5F, 1.5F, 0xFFf8e8ff, IS.of(1, Rarity.EPIC).durability(SpectrumToolTiers.NECTAR_LANCE.getUses())));
	
	// Bedrock Armor
	public static final DeferredItem<BedrockArmorItem> BEDROCK_HELMET = register("bedrock_helmet", () -> new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.HELMET, IS.of(Rarity.UNCOMMON).fireResistant().durability(70 * 13).component(DataComponents.UNBREAKABLE, new Unbreakable(false))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROJECTILE_PROTECTION, 5);
		}
	});
	public static final DeferredItem<BedrockArmorItem> BEDROCK_CHESTPLATE = register("bedrock_chestplate", () -> new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.CHESTPLATE, IS.of(Rarity.UNCOMMON).fireResistant().durability(70 * 15).component(DataComponents.UNBREAKABLE, new Unbreakable(false))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.PROTECTION, 5);
		}
	});
	public static final DeferredItem<BedrockArmorItem> BEDROCK_LEGGINGS = register("bedrock_leggings", () -> new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.LEGGINGS, IS.of(Rarity.UNCOMMON).fireResistant().durability(70 * 16).component(DataComponents.UNBREAKABLE, new Unbreakable(false))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.BLAST_PROTECTION, 5);
		}
	});
	public static final DeferredItem<BedrockArmorItem> BEDROCK_BOOTS = register("bedrock_boots", () -> new BedrockArmorItem(SpectrumArmorMaterials.BEDROCK, ArmorItem.Type.BOOTS, IS.of(Rarity.UNCOMMON).fireResistant().durability(70 * 11).component(DataComponents.UNBREAKABLE, new Unbreakable(false))) {
		@Override
		public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
			return Map.of(Enchantments.FIRE_PROTECTION, 5);
		}
	});
	
	// Armor
	public static final DeferredItem<GemstoneArmorItem> FETCHLING_HELMET = register("fetchling_helmet", () -> new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.HELMET, IS.of(Rarity.UNCOMMON).durability(9 * 13)));
	public static final DeferredItem<GemstoneArmorItem> FEROCIOUS_CHESTPLATE = register("ferocious_chestplate", () -> new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.CHESTPLATE, IS.of(Rarity.UNCOMMON).durability(9 * 15)));
	public static final DeferredItem<GemstoneArmorItem> SYLPH_LEGGINGS = register("sylph_leggings", () -> new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.LEGGINGS, IS.of(Rarity.UNCOMMON).durability(9 * 16)));
	public static final DeferredItem<GemstoneArmorItem> OREAD_BOOTS = register("oread_boots", () -> new GemstoneArmorItem(SpectrumArmorMaterials.GEMSTONE, ArmorItem.Type.BOOTS, IS.of(Rarity.UNCOMMON).durability(9 * 11)));
	
	// Decay drops
	public static final DeferredItem<Item> VEGETAL = register("vegetal", () -> new CloakedItemWithLoomPattern(IS.of(), SpectrumAdvancements.CRAFT_BOTTLE_OF_FADING, GUNPOWDER, SpectrumBannerPatternKeys.VEGETAL));
	public static final DeferredItem<Item> NEOLITH = register("neolith", () -> new CloakedItemWithLoomPattern(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CRAFT_BOTTLE_OF_FAILING, GUNPOWDER, SpectrumBannerPatternKeys.NEOLITH));
	public static final DeferredItem<Item> BEDROCK_DUST = register("bedrock_dust", () -> new CloakedItemWithLoomPattern(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.BREAK_DECAYED_BEDROCK, GUNPOWDER, SpectrumBannerPatternKeys.BEDROCK_DUST));
	
	public static final DeferredItem<MidnightAberrationItem> MIDNIGHT_ABERRATION = register("midnight_aberration", () -> new MidnightAberrationItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CREATE_MIDNIGHT_ABERRATION, SpectrumItems.SPECTRAL_SHARD.get()));
	public static final DeferredItem<Item> MIDNIGHT_CHIP = register("midnight_chip", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.CREATE_MIDNIGHT_ABERRATION, GRAY_DYE));
	
	public static final DeferredItem<Item> BISMUTH_FLAKE = register("bismuth_flake", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.ENTER_DIMENSION, CYAN_DYE));
	public static final DeferredItem<Item> BISMUTH_CRYSTAL = register("bismuth_crystal", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.ENTER_DIMENSION, CYAN_DYE));
	public static final DeferredItem<Item> RAW_MALACHITE = register("raw_malachite", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.REVEAL_MALACHITE, GREEN_DYE));
	public static final DeferredItem<Item> PURE_MALACHITE = register("pure_malachite", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.REVEAL_MALACHITE, GREEN_DYE));
	
	// Fluid Buckets
	public static final DeferredItem<Item> LIQUID_CRYSTAL_BUCKET = register("liquid_crystal_bucket", () -> new BucketItem(SpectrumFluids.LIQUID_CRYSTAL.get(), IS.of(1).craftRemainder(BUCKET)));
	public static final DeferredItem<Item> MIDNIGHT_SOLUTION_BUCKET = register("midnight_solution_bucket", () -> new BucketItem(SpectrumFluids.MIDNIGHT_SOLUTION.get(), IS.of(1).craftRemainder(BUCKET)));
	public static final DeferredItem<Item> DRAGONROT_BUCKET = register("dragonrot_bucket", () -> new BucketItem(SpectrumFluids.DRAGONROT.get(), IS.of(1).craftRemainder(BUCKET)));
	
	// Decay bottles
	public static final DeferredItem<Item> BOTTLE_OF_FADING = register("bottle_of_fading", () -> new DecayPlacerItem(SpectrumBlocks.FADING.get(), IS.of(), List.of(Component.translatable("item.spectrum.bottle_of_fading.tooltip"))));
	public static final DeferredItem<Item> BOTTLE_OF_FAILING = register("bottle_of_failing", () -> new DecayPlacerItem(SpectrumBlocks.FAILING.get(), IS.of(), List.of(Component.translatable("item.spectrum.bottle_of_failing.tooltip"))));
	public static final DeferredItem<Item> BOTTLE_OF_RUIN = register("bottle_of_ruin", () -> new DecayPlacerItem(SpectrumBlocks.RUIN.get(), IS.of(), List.of(Component.translatable("item.spectrum.bottle_of_ruin.tooltip"))));
	public static final DeferredItem<Item> BOTTLE_OF_FORFEITURE = register("bottle_of_forfeiture", () -> new DecayPlacerItem(SpectrumBlocks.FORFEITURE.get(), IS.of(), List.of(CreativeOnlyItem.DESCRIPTION, Component.translatable("item.spectrum.bottle_of_forfeiture.tooltip"))));
	public static final DeferredItem<Item> BOTTLE_OF_DECAY_AWAY = register("bottle_of_decay_away", () -> new DecayPlacerItem(SpectrumBlocks.DECAY_AWAY.get(), IS.of(), List.of(Component.translatable("item.spectrum.bottle_of_decay_away.tooltip"))));
	
	// Resources
	public static final DeferredItem<Item> SHIMMERSTONE_GEM = register("shimmerstone_gem", () -> new CloakedItemWithLoomPattern(IS.of(), (SpectrumBlocks.SHIMMERSTONE_ORE.get()).getCloakAdvancementIdentifier(), YELLOW_DYE, SpectrumBannerPatternKeys.SHIMMERSTONE));
	public static final DeferredItem<Item> RAW_AZURITE = register("raw_azurite", () -> new CloakedItemWithLoomPattern(IS.of(), SpectrumBlocks.AZURITE_ORE.get().getCloakAdvancementIdentifier(), BLUE_DYE, SpectrumBannerPatternKeys.RAW_AZURITE));
	public static final DeferredItem<Item> PURE_AZURITE = register("pure_azurite", () -> new CloakedItem(IS.of(), SpectrumBlocks.AZURITE_ORE.get().getCloakAdvancementIdentifier(), BLUE_DYE));
	public static final DeferredItem<CloakedItem> PALTAERIA_FRAGMENTS = register("paltaeria_fragments", () -> new CloakedItem(IS.of().component(SpectrumDataComponentTypes.GRAVITABLE, 0.00125F), SpectrumBlocks.PALTAERIA_ORE.get().getCloakAdvancementIdentifier(), CYAN_DYE));
	public static final DeferredItem<CloakedItem> PALTAERIA_GEM = register("paltaeria_gem", () -> new CloakedItem(IS.of(16).component(SpectrumDataComponentTypes.GRAVITABLE, 0.01F), SpectrumBlocks.PALTAERIA_ORE.get().getCloakAdvancementIdentifier(), CYAN_DYE));
	public static final DeferredItem<CloakedItem> STRATINE_FRAGMENTS = register("stratine_fragments", () -> new CloakedItem(IS.of().fireResistant().component(SpectrumDataComponentTypes.GRAVITABLE, -0.00125F), SpectrumBlocks.STRATINE_ORE.get().getCloakAdvancementIdentifier(), RED_DYE));
	public static final DeferredItem<CloakedItem> STRATINE_GEM = register("stratine_gem", () -> new CloakedItem(IS.of(16).fireResistant().component(SpectrumDataComponentTypes.GRAVITABLE, -0.01F), SpectrumBlocks.STRATINE_ORE.get().getCloakAdvancementIdentifier(), RED_DYE));
	public static final DeferredItem<Item> PYRITE_CHUNK = register("pyrite_chunk", () -> new Item(IS.of()));
	public static final DeferredItem<Item> DRAGONBONE_CHUNK = register("dragonbone_chunk", () -> new CloakedItem(IS.of(Rarity.UNCOMMON).component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_EXPLOSION)), SpectrumAdvancements.BREAK_CRACKED_DRAGONBONE, GRAY_DYE));
	public static final DeferredItem<Item> BONE_ASH = register("bone_ash", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.BREAK_CRACKED_DRAGONBONE, GRAY_DYE));
	public static final DeferredItem<Item> RESPLENDENT_FEATHER = register("resplendent_feather", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_RESPLENDENT_FEATHER, RED_DYE));
	public static final DeferredItem<Item> RAW_BLOODSTONE = register("raw_bloodstone", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_RESPLENDENT_FEATHER, RED_DYE));
	public static final DeferredItem<Item> PURE_BLOODSTONE = register("pure_bloodstone", () -> new CloakedItem(IS.of(Rarity.UNCOMMON), SpectrumAdvancements.PLUCK_RESPLENDENT_FEATHER, RED_DYE));
	public static final DeferredItem<Item> DOWNSTONE_FRAGMENTS = register("downstone_fragments", () -> new CloakedItem(IS.of(16, Rarity.UNCOMMON), SpectrumAdvancements.FIND_EXCAVATION_SITE, LIGHT_GRAY_DYE));
	public static final DeferredItem<Item> RESONANCE_SHARD = register("resonance_shard", () -> new CloakedItem(IS.of(16, Rarity.UNCOMMON), SpectrumAdvancements.STRIKE_UP_HUMMINGSTONE_HYMN, LIGHT_BLUE_DYE));
	public static final DeferredItem<Item> AETHER_VESTIGES = register("aether_vestiges", () -> new AetherVestigesItem(IS.of(1, Rarity.EPIC).fireResistant(), "item.spectrum.aether_vestiges.tooltip"));
	
	public static final DeferredItem<Item> QUITOXIC_POWDER = register("quitoxic_powder", () -> new CloakedItem(IS.of(), SpectrumAdvancements.REVEAL_QUITOXIC_REEDS, PURPLE_DYE));
	public static final DeferredItem<Item> STORM_STONE = register("storm_stone", () -> new StormStoneItem(IS.of(), SpectrumAdvancements.REVEAL_STORM_STONES, YELLOW_DYE));
	public static final DeferredItem<Item> MERMAIDS_GEM = register("mermaids_gem", () -> new MermaidsGemItem(SpectrumBlocks.MERMAIDS_BRUSH.get(), IS.of().component(SpectrumDataComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME)))));
	public static final DeferredItem<CloakedItem> STAR_FRAGMENT = register("star_fragment", () -> new CloakedItem(IS.of(16), SpectrumAdvancements.UNLOCK_SHOOTING_STARS, PURPLE_DYE));
	public static final DeferredItem<Item> STARDUST = register("stardust", () -> new CloakedItemWithLoomPattern(IS.of(), SpectrumAdvancements.UNLOCK_SHOOTING_STARS, PURPLE_DYE, SpectrumBannerPatternKeys.SHIMMER));
	public static final DeferredItem<Item> ASH_FLAKES = register("ash_flakes", () -> new AshItem(IS.of()));
	
	public static final DeferredItem<Item> HIBERNATING_JADE_VINE_BULB = register("hibernating_jade_vine_bulb", () -> new ItemWithTooltip(IS.of(), "item.spectrum.hibernating_jade_vine_bulb.tooltip"));
	public static final DeferredItem<Item> GERMINATED_JADE_VINE_BULB = register("germinated_jade_vine_bulb", () -> new GerminatedJadeVineBulbItem(IS.of(), SpectrumAdvancements.COLLECT_HIBERNATING_JADE_VINE_BULB, LIME_DYE));
	public static final DeferredItem<Item> JADE_VINE_PETALS = register("jade_vine_petals", () -> new CloakedItemWithLoomPattern(IS.of(), SpectrumAdvancements.COLLECT_JADE_VINE_PETALS, LIME_DYE, SpectrumBannerPatternKeys.JADE_VINE));
	public static final DeferredItem<Item> JADEITE_PETALS = register("jadeite_petals", () -> new Item(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> BLOOD_ORCHID_PETAL = register("blood_orchid_petal", () -> new CloakedItem(IS.of(), SpectrumAdvancements.REVEAL_BLOOD_ORCHID_PETALS, RED_DYE));
	
	public static final DeferredItem<Item> SUGAR_STICK = register("sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.SUGAR, false));
	public static final DeferredItem<Item> TOPAZ_SUGAR_STICK = register("topaz_sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.TOPAZ, false));
	public static final DeferredItem<Item> AMETHYST_SUGAR_STICK = register("amethyst_sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.AMETHYST, false));
	public static final DeferredItem<Item> CITRINE_SUGAR_STICK = register("citrine_sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.CITRINE, false));
	public static final DeferredItem<Item> ONYX_SUGAR_STICK = register("onyx_sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.ONYX, false));
	public static final DeferredItem<Item> MOONSTONE_SUGAR_STICK = register("moonstone_sugar_stick", () -> new RockCandyItem(IS.of(), RockCandyItem.RockCandyVariant.MOONSTONE, false));
	
	public static final DeferredItem<Item> ROCK_CANDY = register("rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.ROCK_CANDY), RockCandyItem.RockCandyVariant.SUGAR, true));
	public static final DeferredItem<Item> TOPAZ_ROCK_CANDY = register("topaz_rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.TOPAZ_ROCK_CANDY), RockCandyItem.RockCandyVariant.TOPAZ, true));
	public static final DeferredItem<Item> AMETHYST_ROCK_CANDY = register("amethyst_rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.AMETHYST_ROCK_CANDY), RockCandyItem.RockCandyVariant.AMETHYST, true));
	public static final DeferredItem<Item> CITRINE_ROCK_CANDY = register("citrine_rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.CITRINE_ROCK_CANDY), RockCandyItem.RockCandyVariant.CITRINE, true));
	public static final DeferredItem<Item> ONYX_ROCK_CANDY = register("onyx_rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.ONYX_ROCK_CANDY), RockCandyItem.RockCandyVariant.ONYX, true));
	public static final DeferredItem<Item> MOONSTONE_ROCK_CANDY = register("moonstone_rock_candy", () -> new RockCandyItem(IS.of().food(SpectrumFoodComponents.MOONSTONE_ROCK_CANDY), RockCandyItem.RockCandyVariant.MOONSTONE, true));
	
	public static final DeferredItem<Item> BLOODBOIL_SYRUP = register("bloodboil_syrup", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.BLOODBOIL_SYRUP).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> MILKY_RESIN = register("milky_resin", () -> new Item(IS.of(Rarity.UNCOMMON)));
	
	// Food & drinks
	public static final DeferredItem<Item> SCONE = register("scone", () -> new Item(IS.of().food(SpectrumFoodComponents.SCONE)));
	public static final DeferredItem<Item> MOONSTRUCK_NECTAR = register("moonstruck_nectar", () -> new DrinkItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.MOONSTRUCK_NECTAR).craftRemainder(GLASS_BOTTLE), Component.translatable("item.spectrum.moonstruck_nectar.tooltip").append("\n").append(Component.translatable("item.spectrum.moonstruck_nectar.tooltip2"))));
	public static final DeferredItem<Item> JADE_JELLY = register("jade_jelly", () -> new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.JADE_JELLY), "item.spectrum.jade_jelly.tooltip"));
	public static final DeferredItem<Item> GLASS_PEACH = register("glass_peach", () -> new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.GLASS_PEACH), "item.spectrum.glass_peach.tooltip"));
	public static final DeferredItem<Item> FISSURE_PLUM = register("fissure_plum", () -> new AliasedTooltipItem(SpectrumBlocks.ABYSSAL_VINES.get(), IS.of().food(SpectrumFoodComponents.FISSURE_PLUM), "item.spectrum.fissure_plum.tooltip"));
	public static final DeferredItem<Item> NIGHTDEW_SPROUT = register("nightdew_sprout", () -> new AliasedTooltipItem(SpectrumBlocks.NIGHTDEW.get(), IS.of().food(SpectrumFoodComponents.NIGHTDEW_SPROUT), "item.spectrum.nightdew_sprout.tooltip"));
	public static final DeferredItem<Item> NECTARDEW_BURGEON = register("nectardew_burgeon", () -> new NectardewBurgeonItem(IS.of().food(SpectrumFoodComponents.NECTARDEW_BURGEON), "item.spectrum.nectardew_burgeon.tooltip", SpectrumAdvancements.COLLECT_NECTARDEW, SpectrumItems.NIGHTDEW_SPROUT.get()));
	public static final DeferredItem<Item> RESTORATION_TEA = register("restoration_tea", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.RESTORATION_TEA).craftRemainder(GLASS_BOTTLE).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.RESTORATION_TEA_SCONE_BONUS)), Component.translatable("item.spectrum.restoration_tea.tooltip").append("\n").append(Component.translatable("item.spectrum.restoration_tea.tooltip2"))));
	public static final DeferredItem<Item> KIMCHI = register("kimchi", () -> new Item(IS.of().food(SpectrumFoodComponents.KIMCHI)));
	public static final DeferredItem<Item> CLOTTED_CREAM = register("clotted_cream", () -> new ClottedCreamItem(IS.of().food(SpectrumFoodComponents.CLOTTED_CREAM), new String[]{"item.spectrum.clotted_cream.tooltip", "item.spectrum.clotted_cream.tooltip2"}));
	public static final DeferredItem<Item> FRESH_CHOCOLATE = register("fresh_chocolate", () -> new Item(IS.of().food(SpectrumFoodComponents.FRESH_CHOCOLATE)));
	public static final DeferredItem<Item> HOT_CHOCOLATE = register("hot_chocolate", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.HOT_CHOCOLATE).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.HOT_CHOCOLATE_SCONE_BONUS))));
	public static final DeferredItem<Item> KARAK_CHAI = register("karak_chai", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.KARAK_CHAI).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.KARAK_CHAI_SCONE_BONUS))));
	public static final DeferredItem<Item> AZALEA_TEA = register("azalea_tea", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.AZALEA_TEA).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.AZALEA_TEA_SCONE_BONUS)), "item.spectrum.azalea_tea.tooltip"));
	public static final DeferredItem<Item> BODACIOUS_BERRY_BAR = register("bodacious_berry_bar", () -> new Item(IS.of().food(SpectrumFoodComponents.BODACIOUS_BERRY_BAR)));
	public static final DeferredItem<Item> DEMON_TEA = register("demon_tea", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.DEMON_TEA).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.DEMON_TEA_SCONE_BONUS))));
	
	public static final DeferredItem<Item> CHEONG = register("cheong", () -> new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.CHEONG), "item.spectrum.cheong.tooltip"));
	public static final DeferredItem<Item> MERMAIDS_JAM = register("mermaids_jam", () -> new Item(IS.of().food(SpectrumFoodComponents.MERMAIDS_JAM)));
	public static final DeferredItem<Item> MERMAIDS_POPCORN = register("mermaids_popcorn", () -> new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.MERMAIDS_POPCORN), "item.spectrum.mermaids_popcorn.tooltip"));
	public static final DeferredItem<Item> LE_FISHE_AU_CHOCOLAT = register("le_fishe_au_chocolat", () -> new Item(IS.of().food(SpectrumFoodComponents.LE_FISHE_AU_CHOCOLAT).jukeboxPlayable(SpectrumJukeboxSongs.LE_FISHE_AU_CHOCOLAT)));
	
	public static final DeferredItem<Item> INFUSED_BEVERAGE = register("infused_beverage", () -> new BeverageItem(IS.of().food(SpectrumFoodComponents.BEVERAGE).craftRemainder(Items.GLASS_BOTTLE).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(SpectrumDataComponentTypes.INFUSED_BEVERAGE, InfusedBeverageComponent.DEFAULT)));
	public static final DeferredItem<Item> SUSPICIOUS_BREW = register("suspicious_brew", () -> new SuspiciousBrewItem(IS.of().food(SpectrumFoodComponents.BEVERAGE).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> REPRISE = register("reprise", () -> new RepriseItem(IS.of().food(SpectrumFoodComponents.BEVERAGE).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> PURE_ALCOHOL = register("pure_alcohol", () -> new DrinkItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.PURE_ALCOHOL).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> JADE_WINE = register("jade_wine", () -> new JadeWineItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.BEVERAGE).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> CHRYSOCOLLA = register("chrysocolla", () -> new DrinkItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.PURE_ALCOHOL).craftRemainder(GLASS_BOTTLE)));
	
	public static final DeferredItem<Item> HONEY_PASTRY = register("honey_pastry", () -> new Item(IS.of().food(SpectrumFoodComponents.HONEY_PASTRY)));
	public static final DeferredItem<Item> LUCKY_ROLL = register("lucky_roll", () -> new Item(IS.of().food(SpectrumFoodComponents.LUCKY_ROLL)));
	public static final DeferredItem<Item> TRIPLE_MEAT_POT_PIE = register("triple_meat_pot_pie", () -> new Item(IS.of().food(SpectrumFoodComponents.TRIPLE_MEAT_POT_PIE)));
	public static final DeferredItem<Item> GLISTERING_JELLY_TEA = register("glistering_jelly_tea", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.GLISTERING_JELLY_TEA).craftRemainder(GLASS_BOTTLE).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.GLISTERING_JELLY_TEA_SCONE_BONUS))));
	public static final DeferredItem<Item> FREIGEIST = register("freigeist", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.FREIGEIST).craftRemainder(GLASS_BOTTLE), "item.spectrum.freigeist.tooltip"));
	public static final DeferredItem<Item> DIVINATION_HEART = register("divination_heart", () -> new Item(IS.of().food(SpectrumFoodComponents.DIVINATION_HEART)));
	
	public static final DeferredItem<Item> SUGARY_STAR_CANDY = register("sugary_star_candy", () -> new StarCandyItem(IS.of(Rarity.COMMON).food(SpectrumFoodComponents.SUGARY_STAR_CANDY), StarCandyItem.Rarity.SUGARY));
	public static final DeferredItem<Item> MELLOW_STAR_CANDY = register("mellow_star_candy", () -> new StarCandyItem(IS.of(Rarity.UNCOMMON).food(SpectrumFoodComponents.MELLOW_STAR_CANDY), StarCandyItem.Rarity.MELLOW));
	public static final DeferredItem<Item> GLEAMING_STAR_CANDY = register("gleaming_star_candy", () -> new StarCandyItem(IS.of(Rarity.RARE).food(SpectrumFoodComponents.GLEAMING_STAR_CANDY), StarCandyItem.Rarity.GLEAMING));
	public static final DeferredItem<Item> ENCHANTED_STAR_CANDY = register("enchanted_star_candy", () -> new StarCandyItem(IS.of(Rarity.EPIC).food(SpectrumFoodComponents.ENCHANTED_STAR_CANDY), StarCandyItem.Rarity.ENCHANTED));
	public static final DeferredItem<Item> MAGNIFICENT_STAR_CANDY = register("magnificent_star_candy", () -> new StarCandyItem(IS.of(Rarity.EPIC).food(SpectrumFoodComponents.MAGNIFICENT_STAR_CANDY), StarCandyItem.Rarity.MAGNIFICENT));

	public static final DeferredItem<Item> ENCHANTED_GOLDEN_CARROT = register("enchanted_golden_carrot", () -> new ItemWithGlint(IS.of(Rarity.EPIC).food(SpectrumFoodComponents.ENCHANTED_GOLDEN_CARROT)));
	public static final DeferredItem<Item> JARAMEL = register("jaramel", () -> new Item(IS.of().food(SpectrumFoodComponents.JARAMEL)));
	
	public static final DeferredItem<Item> JARAMEL_TART = register("jaramel_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.JARAMEL_TART)));
	public static final DeferredItem<Item> SALTED_JARAMEL_TART = register("salted_jaramel_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.SALTED_JARAMEL_TART)));
	public static final DeferredItem<Item> ASHEN_TART = register("ashen_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.ASHEN_TART)));
	public static final DeferredItem<Item> WEEPING_TART = register("weeping_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.WEEPING_TART)));
	public static final DeferredItem<Item> WHISPY_TART = register("whispy_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.WHISPY_TART)));
	public static final DeferredItem<Item> PUFF_TART = register("puff_tart", () -> new Item(IS.of().food(SpectrumFoodComponents.PUFF_TART)));
	
	public static final DeferredItem<Item> JARAMEL_TRIFLE = register("jaramel_trifle", () -> new Item(IS.of().food(SpectrumFoodComponents.JARAMEL_TRIFLE)));
	public static final DeferredItem<Item> SALTED_JARAMEL_TRIFLE = register("salted_jaramel_trifle", () -> new Item(IS.of().food(SpectrumFoodComponents.SALTED_JARAMEL_TRIFLE)));
	public static final DeferredItem<Item> MONSTER_TRIFLE = register("monster_trifle", () -> new Item(IS.of().food(SpectrumFoodComponents.MONSTER_TRIFLE)));
	public static final DeferredItem<Item> DEMON_TRIFLE = register("demon_trifle", () -> new Item(IS.of().food(SpectrumFoodComponents.DEMON_TRIFLE)));
	
	public static final DeferredItem<Item> MYCEYLON = register("myceylon", () -> new CloakedItem(IS.of(), SpectrumAdvancements.COLLECT_MYCEYLON, ORANGE_DYE));
	public static final DeferredItem<Item> MYCEYLON_APPLE_PIE = register("myceylon_apple_pie", () -> new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_APPLE_PIE)));
	public static final DeferredItem<Item> MYCEYLON_PUMPKIN_PIE = register("myceylon_pumpkin_pie", () -> new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_PUMPKIN_PIE)));
	public static final DeferredItem<Item> MYCEYLON_COOKIE = register("myceylon_cookie", () -> new Item(IS.of().food(SpectrumFoodComponents.MYCEYLON_COOKIE)));
	public static final DeferredItem<Item> ALOE_LEAF = register("aloe_leaf", () -> new ItemNameBlockItem(SpectrumBlocks.ALOE.get(), IS.of().food(SpectrumFoodComponents.ALOE_LEAF)));
	public static final DeferredItem<Item> SAWBLADE_HOLLY_BERRY = register("sawblade_holly_berry", () -> new ItemNameBlockItem(SpectrumBlocks.SAWBLADE_HOLLY_BUSH.get(), IS.of().food(Foods.SWEET_BERRIES)));
	public static final DeferredItem<Item> PRICKLY_BAYLEAF = register("prickly_bayleaf", () -> new Item(IS.of().food(SpectrumFoodComponents.PRICKLY_BAYLEAF)));
	public static final DeferredItem<Item> TRIPLE_MEAT_POT_STEW = register("triple_meat_pot_stew", () -> new Item(IS.of(8).food(SpectrumFoodComponents.TRIPLE_MEAT_POT_STEW)));
	public static final DeferredItem<Item> DRAGONBONE_BROTH = register("dragonbone_broth", () -> new Item(IS.of(8).food(SpectrumFoodComponents.DRAGONBONE_BROTH)));
	public static final DeferredItem<Item> DOOMBLOOM_SEED = register("doombloom_seed", () -> new ItemNameBlockItem(SpectrumBlocks.DOOMBLOOM.get(), IS.of().fireResistant().fireResistant().component(SpectrumDataComponentTypes.DAMAGE_IMMUNE, List.of(DamageTypeTags.IS_FIRE, DamageTypeTags.IS_EXPLOSION))));
	
	public static final DeferredItem<Item> GLISTERING_MELON_SEEDS = register("glistering_melon_seeds", () -> new ItemNameBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM.get(), IS.of()));
	public static final DeferredItem<Item> AMARANTH_GRAINS = register("amaranth_grains", () -> new ItemNameBlockItem(SpectrumBlocks.AMARANTH.get(), IS.of()));
	
	// Cookbooks
	public static final DeferredItem<Item> MELOCHITES_COOKBOOK_VOL_1 = register("melochites_cookbook_vol_1", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.UNCOMMON), GuidebookItem.addressOf(GuidebookItem.CUISINE_CATEGORY_ID, locate("cuisine/cookbooks/melochites_cookbook_vol_1"))));
	public static final DeferredItem<Item> MELOCHITES_COOKBOOK_VOL_2 = register("melochites_cookbook_vol_2", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.UNCOMMON), GuidebookItem.addressOf(GuidebookItem.CUISINE_CATEGORY_ID, locate("cuisine/cookbooks/melochites_cookbook_vol_2"))));
	public static final DeferredItem<Item> IMBRIFER_COOKBOOK = register("imbrifer_cookbook", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.UNCOMMON), GuidebookItem.addressOf(GuidebookItem.CUISINE_CATEGORY_ID, locate("cuisine/cookbooks/imbrifer_cookbook"))));
	public static final DeferredItem<Item> IMPERIAL_COOKBOOK = register("imperial_cookbook", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.UNCOMMON), GuidebookItem.addressOf(GuidebookItem.CUISINE_CATEGORY_ID, locate("cuisine/cookbooks/imperial_cookbook"))));
	public static final DeferredItem<Item> BREWERS_HANDBOOK = register("brewers_handbook", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.UNCOMMON), GuidebookItem.addressOf(GuidebookItem.CUISINE_CATEGORY_ID, locate("cuisine/cookbooks/brewers_handbook"))));
	public static final DeferredItem<Item> POISONERS_HANDBOOK = register("poisoners_handbook", () -> new CookbookItem(IS.of().stacksTo(1).rarity(Rarity.EPIC), GuidebookItem.addressOf(GuidebookItem.DIMENSION_CATEGORY_ID, locate("dimension/poisoners_handbook")), SpectrumMobEffects.ETERNAL_SLUMBER_COLOR));
	
	public static final DeferredItem<Item> AQUA_REGIA = register("aqua_regia", () -> new JadeWineItem(IS.of(16).food(SpectrumFoodComponents.AQUA_REGIA).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> BAGNUN = register("bagnun", () -> new Item(IS.of().food(SpectrumFoodComponents.BAGNUN)));
	public static final DeferredItem<Item> BANYASH = register("banyash", () -> new Item(IS.of().food(SpectrumFoodComponents.BANYASH)));
	public static final DeferredItem<Item> BERLINER = register("berliner", () -> new Item(IS.of().food(SpectrumFoodComponents.BERLINER)));
	public static final DeferredItem<Item> BRISTLE_MEAD = register("bristle_mead", () -> new BeverageItem(IS.of(16).food(SpectrumFoodComponents.BEVERAGE).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)));
	public static final DeferredItem<Item> CHAUVE_SOURIS_AU_VIN = register("chauve_souris_au_vin", () -> new Item(IS.of().food(SpectrumFoodComponents.CHAUVE_SOURIS_AU_VIN)));
	public static final DeferredItem<Item> CRAWFISH = register("crawfish", () -> new Item(IS.of().food(SpectrumFoodComponents.CRAWFISH)));
	public static final DeferredItem<Item> CRAWFISH_COCKTAIL = register("crawfish_cocktail", () -> new Item(IS.of().food(SpectrumFoodComponents.CRAWFISH_COCKTAIL)));
	public static final DeferredItem<Item> CREAM_PASTRY = register("cream_pastry", () -> new Item(IS.of().food(SpectrumFoodComponents.CREAM_PASTRY)));
	public static final DeferredItem<Item> FADED_KOI = register("faded_koi", () -> new Item(IS.of().food(SpectrumFoodComponents.FADED_KOI)));
	public static final DeferredItem<Item> FISHCAKE = register("fishcake", () -> new Item(IS.of().food(SpectrumFoodComponents.FISHCAKE)));
	public static final DeferredItem<Item> LIZARD_MEAT = register("lizard_meat", () -> new Item(IS.of().food(SpectrumFoodComponents.LIZARD_MEAT)));
	public static final DeferredItem<Item> COOKED_LIZARD_MEAT = register("cooked_lizard_meat", () -> new Item(IS.of().food(SpectrumFoodComponents.COOKED_LIZARD_MEAT)));
	public static final DeferredItem<Item> GOLDEN_BRISTLE_TEA = register("golden_bristle_tea", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.GOLDEN_BRISTLE_TEA).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.GOLDEN_BRISTLE_TEA_SCONE_BONUS)).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> HARE_ROAST = register("hare_roast", () -> new Item(IS.of().food(SpectrumFoodComponents.HARE_ROAST)));
	public static final DeferredItem<Item> JUNKET = register("junket", () -> new Item(IS.of().food(SpectrumFoodComponents.JUNKET)));
	public static final DeferredItem<Item> KOI = register("koi", () -> new Item(IS.of().food(SpectrumFoodComponents.KOI)));
	public static final DeferredItem<Item> MEATLOAF = register("meatloaf", () -> new Item(IS.of().food(SpectrumFoodComponents.MEATLOAF)));
	public static final DeferredItem<Item> MEATLOAF_SANDWICH = register("meatloaf_sandwich", () -> new Item(IS.of().food(SpectrumFoodComponents.MEATLOAF_SANDWICH)));
	public static final DeferredItem<Item> MELLOW_SHALLOT_SOUP = register("mellow_shallot_soup", () -> new Item(IS.of().food(SpectrumFoodComponents.MELLOW_SHALLOT_SOUP)));
	public static final DeferredItem<Item> MORCHELLA = register("morchella", () -> new BeverageItem(IS.of().food(SpectrumFoodComponents.BEVERAGE).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> NECTERED_VIOGNIER = register("nectered_viognier", () -> new JadeWineItem(IS.of().food(SpectrumFoodComponents.NECTERED_VIOGNIER).craftRemainder(GLASS_BOTTLE)));
	public static final DeferredItem<Item> PEACHES_FLAMBE = register("peaches_flambe", () -> new Item(IS.of().food(SpectrumFoodComponents.PEACHES_FLAMBE)));
	public static final DeferredItem<Item> PEACH_CREAM = register("peach_cream", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.PEACH_CREAM).component(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT, teaSconeBonus(SpectrumFoodComponents.PEACH_CREAM_SCONE_BONUS))));
	public static final DeferredItem<Item> PEACH_JAM = register("peach_jam", () -> new Item(IS.of().food(SpectrumFoodComponents.PEACH_JAM)));
	public static final DeferredItem<Item> RABBIT_CREAM_PIE = register("rabbit_cream_pie", () -> new ItemWithTooltip(IS.of().food(SpectrumFoodComponents.RABBIT_CREAM_PIE), "item.spectrum.rabbit_cream_pie.tooltip"));
	public static final DeferredItem<Item> SEDATIVES = register("sedatives", () -> new SedativesItem(IS.of().food(SpectrumFoodComponents.SEDATIVES), "item.spectrum.sedatives.tooltip"));
	public static final DeferredItem<Item> SLUSHSLIDE = register("slushslide", () -> new DrinkItem(IS.of().food(SpectrumFoodComponents.SLUSHSLIDE), "item.spectrum.slushslide.tooltip"));
	public static final DeferredItem<Item> SURSTROMMING = register("surstromming", () -> new Item(IS.of().food(SpectrumFoodComponents.SURSTROMMING)));
	public static final DeferredItem<Item> EVERNECTAR = register("evernectar", () -> new DrinkItem(IS.of(16, Rarity.EPIC).food(SpectrumFoodComponents.EVERNECTAR).craftRemainder(GLASS_BOTTLE), "item.spectrum.evernectar.tooltip"));
	
	// Banner Patterns
	public static final DeferredItem<Item> LOGO_BANNER_PATTERN = register("logo_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.SPECTRUM_LOGO_TAG, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> AMETHYST_SHARD_BANNER_PATTERN = register("amethyst_shard_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.AMETHYST_SHARD_TAG, IS.of(1)));
	public static final DeferredItem<Item> AMETHYST_CLUSTER_BANNER_PATTERN = register("amethyst_cluster_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.AMETHYST_CLUSTER_TAG, IS.of(1)));
	public static final DeferredItem<Item> ASTROLOGER_BANNER_PATTERN = register("astrologer_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.ASTROLOGER_TAG, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> VELVET_ASTROLOGER_BANNER_PATTERN = register("velvet_astrologer_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.VELVET_ASTROLOGER_TAG, IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> POISONBLOOM_BANNER_PATTERN = register("poisonbloom_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.POISONBLOOM_TAG, IS.of(1, Rarity.RARE)));
	public static final DeferredItem<Item> DEEP_LIGHT_BANNER_PATTERN = register("deep_light_banner_pattern", () -> new BannerPatternItem(SpectrumBannerPatternTags.DEEP_LIGHT_TAG, IS.of(1, Rarity.RARE)));
	
	// Spawning items
	public static final DeferredItem<Item> BUCKET_OF_ERASER = register("bucket_of_eraser", () -> new EmptyFluidEntityBucketItem(SpectrumEntityTypes.ERASER.get(), Fluids.EMPTY, SoundEvents.BUCKET_EMPTY, IS.of(1)));
	public static final DeferredItem<Item> EGG_LAYING_WOOLY_PIG_SPAWN_EGG = register("egg_laying_wooly_pig_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, 0x3a2c38, 0xfff2e0, IS.of()));
	public static final DeferredItem<Item> PRESERVATION_TURRET_SPAWN_EGG = register("preservation_turret_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.PRESERVATION_TURRET, 0xf3f6f8, 0xc8c5be, IS.of()));
	public static final DeferredItem<Item> KINDLING_SPAWN_EGG = register("kindling_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.KINDLING, 0xda4261, 0xffd452, IS.of()));
	public static final DeferredItem<Item> LIZARD_SPAWN_EGG = register("lizard_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.LIZARD, 0x896459, 0x503a40, IS.of()));
	public static final DeferredItem<Item> ERASER_SPAWN_EGG = register("eraser_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.ERASER, 0x200d29, 0xc83e93, IS.of()));
	public static final DeferredItem<Item> MARROW_SPAWN_EGG = register("marrow_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.MARROW, 0x908188, 0xe2762f, IS.of()));
	public static final DeferredItem<Item> SPLINTERSPAWN_SPAWN_EGG = register("splinterspawn_spawn_egg", () -> new DeferredSpawnEggItem(SpectrumEntityTypes.SPLINTERSPAWN, 0x7b6b75, 0xf6db6f, IS.of()));
	
	// Magical Tools
	public static final DeferredItem<Item> BAG_OF_HOLDING = register("bag_of_holding", () -> new BagOfHoldingItem(IS.of(1)));
	public static final DeferredItem<Item> RADIANCE_STAFF = register("radiance_staff", () -> new RadianceStaffItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<NaturesStaffItem> NATURES_STAFF = register("natures_staff", () -> new NaturesStaffItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> STAFF_OF_REMEMBRANCE = register("staff_of_remembrance", () -> new StaffOfRemembranceItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> CONSTRUCTORS_STAFF = register("constructors_staff", () -> new ConstructorsStaffItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> EXCHANGING_STAFF = register("exchanging_staff", () -> new ExchangeStaffItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> BLOCK_FLOODER = register("block_flooder", () -> new BlockFlooderItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> PIPE_BOMB = register("pipe_bomb", () -> new PipeBombItem(IS.of(1)));
	public static final DeferredItem<EnderSpliceItem> ENDER_SPLICE = register("ender_splice", () -> new EnderSpliceItem(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> PERTURBED_EYE = register("perturbed_eye", () -> new PerturbedEyeItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> CRESCENT_CLOCK = register("crescent_clock", () -> new ItemWithTooltip(IS.of(1), "item.spectrum.crescent_clock.tooltip"));
	public static final DeferredItem<Item> PRIMORDIAL_LIGHTER = register("primordial_lighter", () -> new PrimordialLighterItem(IS.of(1)));
	public static final DeferredItem<Item> NIGHT_SALTS = register("night_salts", () -> new NightSaltsItem(IS.of(16).component(SpectrumDataComponentTypes.SLEEP_ALTERING_EFFECTS, List.of(new MobEffectInstance(SpectrumMobEffects.VULNERABILITY, 20 * 30), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 30)))));
	public static final DeferredItem<Item> SOOTHING_BOUQUET = register("soothing_bouquet", () -> new SoothingBouquetItem(IS.of(1, Rarity.RARE).component(SpectrumDataComponentTypes.SLEEP_ALTERING_EFFECTS, List.of(new MobEffectInstance(MobEffects.REGENERATION, 20 * 15)))));
	public static final DeferredItem<Item> CONCEALING_OILS = register("concealing_oils", () -> new ConcealingOilsItem(IS.of(16)));
	public static final DeferredItem<Item> BITTER_OILS = register("bitter_oils", () -> new DrinkItem(IS.of(16).food(SpectrumFoodComponents.BITTER_OILS).craftRemainder(GLASS_BOTTLE)));
	
	public static final DeferredItem<Item> INCANDESCENT_ESSENCE = register("incandescent_essence", () -> new CloakedItem(IS.of().fireResistant(), SpectrumAdvancements.REVEAL_INCANDESCENT_RESOURCES, ORANGE_DYE));
	public static final DeferredItem<Item> FROSTBITE_ESSENCE = register("frostbite_essence", () -> new CloakedItem(IS.of(), SpectrumAdvancements.REVEAL_FROSTBITE_RESOURCES, LIGHT_BLUE_DYE));
	public static final DeferredItem<Item> MOONSTONE_CORE = register("moonstone_core", () -> new CloakedItem(IS.of(Rarity.RARE), SpectrumAdvancements.FIND_FORGOTTEN_CITY, WHITE_DYE));
	
	// Music discs
	public static final DeferredItem<Item> MUSIC_DISC_DISCOVERY = register("music_disc_discovery", () -> new Item(IS.of(1, Rarity.RARE).jukeboxPlayable(SpectrumJukeboxSongs.DISCOVERY)));
	public static final DeferredItem<Item> MUSIC_DISC_CREDITS = register("music_disc_credits", () -> new Item(IS.of(1, Rarity.RARE).jukeboxPlayable(SpectrumJukeboxSongs.CREDITS)));
	public static final DeferredItem<Item> MUSIC_DISC_DIVINITY = register("music_disc_divinity", () -> new Item(IS.of(1, Rarity.RARE).jukeboxPlayable(SpectrumJukeboxSongs.DIVINITY)));
	
	// Item Frames
	public static final DeferredItem<Item> PHANTOM_FRAME = register("phantom_frame", () -> new PhantomFrameItem(SpectrumEntityTypes.PHANTOM_FRAME.get(), IS.of()));
	public static final DeferredItem<Item> GLOW_PHANTOM_FRAME = register("glow_phantom_frame", () -> new PhantomGlowFrameItem(SpectrumEntityTypes.GLOW_PHANTOM_FRAME.get(), IS.of()));
	
	// Specialty Magical Tools
	public static final DeferredItem<KnowledgeGemItem> KNOWLEDGE_GEM = register("knowledge_gem", () -> new KnowledgeGemItem(IS.of(1, Rarity.UNCOMMON), 10000));
	public static final DeferredItem<Item> CELESTIAL_POCKETWATCH = register("celestial_pocketwatch", () -> new CelestialPocketWatchItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> ARTISANS_ATLAS = register("artisans_atlas", () -> new ArtisansAtlasItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> GILDED_BOOK = register("gilded_book", () -> new GildedBookItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> ENCHANTMENT_CANVAS = register("enchantment_canvas", () -> new EnchantmentCanvasItem(IS.of(Rarity.UNCOMMON)));
	public static final DeferredItem<Item> EVERPROMISE_RIBBON = register("everpromise_ribbon", () -> new EverpromiseRibbonItem(IS.of()));
	
	// Lore
	public static final DeferredItem<Item> MYSTERIOUS_LOCKET = register("mysterious_locket", () -> new MysteriousLocketItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> MYSTERIOUS_COMPASS = register("mysterious_compass", () -> new MysteriousCompassItem(IS.of(1, Rarity.RARE)));
	
	// Trinkets
	public static final DeferredItem<Item> FANCIFUL_TUFF_RING = register("fanciful_tuff_ring", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> FANCIFUL_BELT = register("fanciful_belt", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> FANCIFUL_PENDANT = register("fanciful_pendant", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> FANCIFUL_CIRCLET = register("fanciful_circlet", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> FANCIFUL_GLOVES = register("fanciful_gloves", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> FANCIFUL_BISMUTH_RING = register("fanciful_bismuth_ring", () -> new Item(IS.of(16, Rarity.UNCOMMON)));
	
	public static final DeferredItem<Item> GLOW_VISION_GOGGLES = register("glow_vision_goggles", () -> new GlowVisionGogglesItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> JEOPARDANT = register("jeopardant", () -> new AttackRingItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<SevenLeagueBootsItem> SEVEN_LEAGUE_BOOTS = register("seven_league_boots", () -> new SevenLeagueBootsItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> COTTON_CLOUD_BOOTS = register("cotton_cloud_boots", () -> new CottonCloudBootsItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> RADIANCE_PIN = register("radiance_pin", () -> new RadiancePinItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> TOTEM_PENDANT = register("totem_pendant", () -> new TotemPendantItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<TakeOffBeltItem> TAKE_OFF_BELT = register("take_off_belt", () -> new TakeOffBeltItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> AZURE_DIKE_BELT = register("azure_dike_belt", () -> new AzureDikeBeltItem(IS.of(1, Rarity.UNCOMMON), SpectrumAdvancements.UNLOCK_AZURE_DIKE_BELT));
	public static final DeferredItem<Item> AZURE_DIKE_RING = register("azure_dike_ring", () -> new AzureDikeRingItem(IS.of(1, Rarity.UNCOMMON), SpectrumAdvancements.UNLOCK_AZURE_DIKE_RING));
	public static final DeferredItem<Item> AZURESQUE_DIKE_CORE = register("azuresque_dike_core", () -> new AzureDikeCoreItem(IS.of(1, Rarity.EPIC), SpectrumAdvancements.UNLOCK_AZURESQUE_DIKE_CORE));
	public static final DeferredItem<InkDrainCurioItem> SHIELDGRASP_AMULET = register("shieldgrasp_amulet", () -> new AzureDikeAmuletItem(IS.of(1, Rarity.UNCOMMON), SpectrumAdvancements.UNLOCK_SHIELDGRASP_AMULET));
	public static final DeferredItem<InkDrainCurioItem> HEARTSINGERS_REWARD = register("heartsingers_reward", () -> new ExtraHealthRingItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<InkDrainCurioItem> GLOVES_OF_DAWNS_GRASP = register("gloves_of_dawns_grasp", () -> new ExtraReachGlovesItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<InkDrainCurioItem> RING_OF_PURSUIT = register("ring_of_pursuit", () -> new ExtraMiningSpeedRingItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<InkDrainCurioItem> RING_OF_DENSER_STEPS = register("ring_of_denser_steps", () -> new RingOfDenserStepsItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<InkDrainCurioItem> RING_OF_AERIAL_GRACE = register("ring_of_aerial_grace", () -> new RingOfAerialGraceItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<InkDrainCurioItem> LAURELS_OF_SERENITY = register("laurels_of_serenity", () -> new LaurelsOfSerenityItem(IS.of(1, Rarity.UNCOMMON)));
	
	// Ink storage
	public static final DeferredItem<InkFlaskItem> INK_FLASK = register("ink_flask", () -> new InkFlaskItem(IS.of(1), 64 * 64 * 100)); // 64 stacks of pigments (1 pigment => 100 energy)
	public static final DeferredItem<InkAssortmentItem> INK_ASSORTMENT = register("ink_assortment", () -> new InkAssortmentItem(IS.of(1), 64 * 100));
	public static final DeferredItem<PigmentPaletteItem> PIGMENT_PALETTE = register("pigment_palette", () -> new PigmentPaletteItem(IS.of(1, Rarity.UNCOMMON), 64 * 64 * 100));
	public static final DeferredItem<ArtistsPaletteItem> ARTISTS_PALETTE = register("artists_palette", () -> new ArtistsPaletteItem(IS.of(1, Rarity.UNCOMMON), 64 * 64 * 64 * 64 * 100));
	public static final DeferredItem<CreativeInkAssortmentItem> CREATIVE_INK_ASSORTMENT = register("creative_ink_assortment", () -> new CreativeInkAssortmentItem(IS.of(1, Rarity.EPIC)));
	
	public static final DeferredItem<GleamingPinItem> GLEAMING_PIN = register("gleaming_pin", () -> new GleamingPinItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> LESSER_POTION_PENDANT = register("lesser_potion_pendant", () -> new PotionPendantItem(IS.of(1, Rarity.UNCOMMON), 1, SpectrumConfig.CONFIG.MaxLevelForEffectsInLesserPotionPendant.get() - 1, SpectrumAdvancements.UNLOCK_LESSER_POTION_PENDANT));
	public static final DeferredItem<Item> GREATER_POTION_PENDANT = register("greater_potion_pendant", () -> new PotionPendantItem(IS.of(1, Rarity.UNCOMMON), 3, SpectrumConfig.CONFIG.MaxLevelForEffectsInGreaterPotionPendant.get() - 1, SpectrumAdvancements.UNLOCK_GREATER_POTION_PENDANT));
	public static final DeferredItem<Item> ASHEN_CIRCLET = register("ashen_circlet", () -> new AshenCircletItem(IS.of(1, Rarity.UNCOMMON).fireResistant()));
	public static final DeferredItem<Item> WEEPING_CIRCLET = register("weeping_circlet", () -> new WeepingCircletItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> PUFF_CIRCLET = register("puff_circlet", () -> new PuffCircletItem(IS.of(1, Rarity.UNCOMMON), SpectrumAdvancements.UNLOCK_PUFF_CIRCLET));
	public static final DeferredItem<Item> WHISPY_CIRCLET = register("whispy_circlet", () -> new WhispyCircletItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> CIRCLET_OF_ARROGANCE = register("circlet_of_arrogance", () -> new CircletOfArroganceItem(IS.of(1, Rarity.UNCOMMON)));
	public static final DeferredItem<Item> NEAT_RING = register("neat_ring", () -> new NeatRingItem(IS.of(1, Rarity.EPIC)));
	
	public static final DeferredItem<Item> AETHER_GRACED_NECTAR_GLOVES = register("aether_graced_nectar_gloves", () -> new AetherGracedNectarGlovesItem(IS.of(1, Rarity.EPIC), SpectrumAdvancements.UNLOCK_AETHER_GRACED_NECTAR_GLOVES));
	
	// Pure Clusters
	public static final DeferredItem<Item> PURE_COAL = register("pure_coal", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_IRON = register("pure_iron", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_GOLD = register("pure_gold", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_DIAMOND = register("pure_diamond", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_EMERALD = register("pure_emerald", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_REDSTONE = register("pure_redstone", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_LAPIS = register("pure_lapis", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_COPPER = register("pure_copper", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_QUARTZ = register("pure_quartz", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_GLOWSTONE = register("pure_glowstone", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_PRISMARINE = register("pure_prismarine", () -> new Item(IS.of()));
	public static final DeferredItem<Item> PURE_NETHERITE_SCRAP = register("pure_netherite_scrap", () -> new Item(IS.of().fireResistant()));
	public static final DeferredItem<Item> PURE_ECHO = register("pure_echo", () -> new Item(IS.of()));
	
	public static<I extends Item> DeferredItem<I> register(String name, Supplier<I> entry) {
		return REGISTRAR.register(name, entry);
	}
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
	public static class IS {
		
		public static Item.Properties of() {
			return new Item.Properties();
		}
		
		public static Item.Properties of(int maxCount) {
			return new Item.Properties().stacksTo(maxCount);
		}
		
		public static Item.Properties of(Rarity rarity) {
			return new Item.Properties().rarity(rarity);
		}
		
		public static Item.Properties of(int maxCount, Rarity rarity) {
			return new Item.Properties().stacksTo(maxCount).rarity(rarity);
		}
		
	}
	
	public static PairedFoodComponent teaSconeBonus(FoodProperties foodComponent) {
		return new PairedFoodComponent(SpectrumItems.SCONE.get(), true, foodComponent);
	}
	
}
