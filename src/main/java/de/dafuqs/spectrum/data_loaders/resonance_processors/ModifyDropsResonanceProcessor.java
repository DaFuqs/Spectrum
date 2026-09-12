package de.dafuqs.spectrum.data_loaders.resonance_processors;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ModifyDropsResonanceProcessor extends ResonanceProcessor {
	
	public static final MapCodec<ModifyDropsResonanceProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BrokenBlockPredicate.CODEC.fieldOf("block")
					.validate(block -> block.test(Blocks.AIR.defaultBlockState()) ? DataResult.error(() -> "Registering a Resonance Drop that matches on everything!") : DataResult.success(block))
					.forGetter(c -> c.blockPredicate),
			Codec.mapPair(Ingredient.CODEC_NONEMPTY.fieldOf("input"), BuiltInRegistries.ITEM.byNameCodec().fieldOf("output")).codec().listOf().xmap(
					pairs -> pairs.stream().collect(() -> (Map<Ingredient, Item>) new HashMap<Ingredient, Item>(), (map, pair) -> map.put(pair.getFirst(), pair.getSecond()), (map1, map2) -> map1.putAll(map2)),
					map -> map.entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).toList()
			).optionalFieldOf("modify_drops", Map.of()).forGetter(c -> c.modifiedDrops)
	).apply(i, ModifyDropsResonanceProcessor::new));
	
	public Map<Ingredient, Item> modifiedDrops;
	
	public ModifyDropsResonanceProcessor(BrokenBlockPredicate blockTarget, Map<Ingredient, Item> modifiedDrops) {
		super(blockTarget);
		this.modifiedDrops = modifiedDrops;
	}
	
	@Override
	public Optional<List<ItemStack>> process(BlockState state, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		if (blockPredicate.test(state)) {
			return Optional.of(modifyDrops(droppedStacks));
		}
		return Optional.empty();
	}
	
	private List<ItemStack> modifyDrops(List<ItemStack> droppedStacks) {
		List<ItemStack> results = new ArrayList<>();
		for (ItemStack stack : droppedStacks) {
			for (Map.Entry<Ingredient, Item> modifiedDrop : modifiedDrops.entrySet()) {
				if (modifiedDrop.getKey().test(stack)) {
					ItemStack convertedStack;
					convertedStack = modifiedDrop.getValue().getDefaultInstance();
					convertedStack.setCount(stack.getCount());
					results.add(convertedStack);
					break;
				}
			}
		}
		return results;
	}
	
	public MapCodec<? extends ResonanceProcessor> getCodec() {
		return CODEC;
	}
	
	public static Builder builder(BrokenBlockPredicate blockTarget) {
		return new Builder(blockTarget);
	}
	
	public static class Builder {
		private final BrokenBlockPredicate blockTarget;
		private final List<Map.Entry<Ingredient, Item>> modifiedDrops = new ArrayList<>();
		
		private Builder(BrokenBlockPredicate blockTarget) {
			this.blockTarget = blockTarget;
		}
		
		public Builder addModifiedDrop(Ingredient ingredient, Item item) {
			this.modifiedDrops.add(Map.entry(ingredient, item));
			return this;
		}
		
		public ModifyDropsResonanceProcessor build() {
			return new ModifyDropsResonanceProcessor(blockTarget, ImmutableMap.copyOf(modifiedDrops));
		}
		
	}
	
}
