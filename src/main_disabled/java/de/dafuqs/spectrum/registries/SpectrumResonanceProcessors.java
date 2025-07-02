package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import de.dafuqs.spectrum.data.*;
import de.dafuqs.spectrum.data_loaders.resonance_processors.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;

import java.util.function.*;

@SuppressWarnings("unused")
public class SpectrumResonanceProcessors {
	
	private static final DeferredRegistrar.Contextual<DatagenProxy.BootstrapContext<ResonanceProcessor>> REGISTRAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	
	public static final ResourceKey<ResonanceProcessor> PURE_RESONANCES_FROM_ORE = register("pure_resonances_from_ore", ctx -> ModifyDropsResonanceProcessor
			.builder(BrokenBlockPredicate.Builder.create().registryEntryList(ctx.blocks().getOrThrow(ConventionalBlockTags.ORES)).build())
			.addModifiedDrop(Ingredient.of(Items.COAL), SpectrumItems.PURE_COAL)
			.addModifiedDrop(Ingredient.of(Items.RAW_COPPER), SpectrumItems.PURE_COPPER)
			.addModifiedDrop(Ingredient.of(Items.DIAMOND), SpectrumItems.PURE_DIAMOND)
			.addModifiedDrop(Ingredient.of(Items.ECHO_SHARD), SpectrumItems.PURE_ECHO)
			.addModifiedDrop(Ingredient.of(Items.EMERALD), SpectrumItems.PURE_EMERALD)
			.addModifiedDrop(Ingredient.of(Items.GLOWSTONE_DUST), SpectrumItems.PURE_GLOWSTONE)
			.addModifiedDrop(Ingredient.of(Items.RAW_GOLD), SpectrumItems.PURE_GOLD)
			.addModifiedDrop(Ingredient.of(Items.RAW_IRON), SpectrumItems.PURE_IRON)
			.addModifiedDrop(Ingredient.of(Items.LAPIS_LAZULI), SpectrumItems.PURE_LAPIS)
			.addModifiedDrop(Ingredient.of(Items.PRISMARINE_CRYSTALS), SpectrumItems.PURE_PRISMARINE)
			.addModifiedDrop(Ingredient.of(Items.QUARTZ), SpectrumItems.PURE_QUARTZ)
			.addModifiedDrop(Ingredient.of(Items.REDSTONE), SpectrumItems.PURE_REDSTONE)
			.addModifiedDrop(Ingredient.of(Items.ANCIENT_DEBRIS), SpectrumItems.PURE_NETHERITE_SCRAP)
			.addModifiedDrop(Ingredient.of(Items.NETHERITE_SCRAP), SpectrumItems.PURE_NETHERITE_SCRAP)
			.build());
	
	public static final ResourceKey<ResonanceProcessor> BLACK_MATERIA = registerDropSelf("black_materia", SpectrumBlocks.BLACK_MATERIA, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> BRUSHABLE_BLOCKS = registerDropSelf("brushable_blocks", SpectrumBlockTags.C_BRUSHABLE_BLOCKS, builder -> builder
			.copyNbt("LootTable", "LootTableSeed", "item"));
	
	public static final ResourceKey<ResonanceProcessor> BUDDING_BLOCKS = registerDropSelf("budding_blocks", ConventionalBlockTags.BUDDING_BLOCKS, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> BUDS = registerDropSelf("buds", ConventionalBlockTags.BUDS, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> CAKE = registerDropSelf("cake", Blocks.CAKE, builder -> builder
			.copyState("bites"));
	
	public static final ResourceKey<ResonanceProcessor> CLUSTERS = registerDropSelf("clusters", ConventionalBlockTags.CLUSTERS, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> COMPOSTER = registerDropSelf("composter", Blocks.COMPOSTER, builder -> builder
			.copyState("level"));
	
	public static final ResourceKey<ResonanceProcessor> FROGSPAWN = registerDropSelf("frogspawn", Blocks.FROGSPAWN, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> GILDED_BLACKSTONE = registerDropSelf("gilded_blackstone", Blocks.GILDED_BLACKSTONE, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> INFESTED_BLOCKS = registerDropSelf("infested_blocks", SpectrumBlockTags.C_INFESTED_BLOCKS, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> REINFORCED_DEEPSLATE = registerDropSelf("reinforced_deepslate", Blocks.REINFORCED_DEEPSLATE, builder -> builder);
	
	public static final ResourceKey<ResonanceProcessor> RESPAWN_ANCHOR = registerDropSelf("respawn_anchor", Blocks.RESPAWN_ANCHOR, builder -> builder
			.copyState("charges"));
	
	public static final ResourceKey<ResonanceProcessor> SCULK_SHRIEKER = registerDropSelf("sculk_shrieker", Blocks.SCULK_SHRIEKER, builder -> builder
			.copyState("can_summon"));
	
	public static final ResourceKey<ResonanceProcessor> SIGNS = registerDropSelf("signs", BlockTags.ALL_SIGNS, builder -> builder
			.copyNbt("front_text", "back_text", "is_waxed"));
	
	public static final ResourceKey<ResonanceProcessor> SPAWNER = registerDropSelf("spawner", Blocks.SPAWNER, builder -> builder
			.copyNbt("SpawnData", "SpawnCount", "MinSpawnDelay", "MaxSpawnDelay", "SpawnRange", "RequiredPlayerRange", "SpawnPotentials", "MaxNearbyEntities"));
	
	public static ResourceKey<ResonanceProcessor> registerDropSelf(String id, Block block, UnaryOperator<DropSelfResonanceProcessor.Builder> builder) {
		return register(id, ctx -> builder.apply(DropSelfResonanceProcessor.builder(BrokenBlockPredicate.Builder.create().blocks(block).build())).build());
	}
	
	public static ResourceKey<ResonanceProcessor> registerDropSelf(String id, TagKey<Block> tag, UnaryOperator<DropSelfResonanceProcessor.Builder> builder) {
		return register(id, ctx -> builder.apply(DropSelfResonanceProcessor.builder(BrokenBlockPredicate.Builder.create().registryEntryList(ctx.blocks().getOrThrow(tag)).build())).build());
	}
	
	public static ResourceKey<ResonanceProcessor> register(String id, Function<DatagenProxy.BootstrapContext<ResonanceProcessor>, ResonanceProcessor> processor) {
		ResourceKey<ResonanceProcessor> key = ResourceKey.create(SpectrumRegistryKeys.RESONANCE_PROCESSOR, SpectrumCommon.locate(id));
		REGISTRAR.defer(ctx -> ctx.registerable().register(key, processor.apply(ctx)));
		return key;
	}
	
	public static void provideResonanceProcessors(DatagenProxy.BootstrapContext<ResonanceProcessor> ctx) {
		REGISTRAR.flush(ctx);
	}
	
}
