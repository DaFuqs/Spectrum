package de.dafuqs.spectrum.compat.farmersdelight;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.conditional.amaranth.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;

public class FDCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	private static final ResourceLocation AMARANTH_LOOT_TABLE_ID = SpectrumCommon.locate("blocks/amaranth");
	private static final ResourceKey<Item> STRAW_KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("farmersdelight", "straw"));
	private static final TagKey<Item> KNIVES_KEY = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("farmersdelight", "tools/knives"));
	
	public void register() {
		LootTableEvents.REPLACE.register((registryKey, lootTable, lootTableSource, wrapperLookup) -> {
			if (lootTableSource.isBuiltin() && AMARANTH_LOOT_TABLE_ID.equals(registryKey.location())) {
				
				LootItemCondition.Builder kniveCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().of(KNIVES_KEY));
				LootItemCondition.Builder fullyGrownCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(SpectrumBlocks.AMARANTH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AmaranthCropBlock.AGE, 7));
				Holder<Enchantment> fortuneHolder = wrapperLookup.lookup(Registries.ENCHANTMENT).get().get(Enchantments.FORTUNE).get();
				
				LootPoolSingletonContainer.Builder<?> b1 = LootItem
						.lootTableItem(SpectrumItems.AMARANTH_GRAINS)
						.when(fullyGrownCondition)
						.when(kniveCondition)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortuneHolder, 0.5714286F, 1));
				
				LootPoolSingletonContainer.Builder<?> b2 = LootItem
						.lootTableItem(SpectrumItems.AMARANTH_GRAINS)
						.when(fullyGrownCondition)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
						.apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortuneHolder, 0.5714286F, 1));
				
				LootPoolSingletonContainer.Builder<?> b3 = LootItem.lootTableItem(SpectrumItems.AMARANTH_GRAINS);
				
				LootPool.Builder mainPool = new LootPool.Builder()
						.with(AlternativesEntry.alternatives(b1, b2, b3).build());
				
				LootPool.Builder strawPool = new LootPool.Builder()
						.when(kniveCondition)
						.with(LootItem.lootTableItem(wrapperLookup.asGetterLookup().lookupOrThrow(Registries.ITEM).get(STRAW_KEY).get().value()).build());
				
				return new LootTable.Builder().withPool(mainPool).withPool(strawPool).apply(ApplyExplosionDecay.explosionDecay()).build();
			}
			return null;
		});
	}
	
	
	@Override
	public void registerClient() {
	
	}
	
}