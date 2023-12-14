package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.compat.biome_makeover.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class SpectrumTooltips {
	
	public static void register() {
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			NbtCompound nbt = stack.getNbt();
			if (nbt != null) {
				
				if (stack.isOf(Blocks.SCULK_SHRIEKER.asItem())) {
					addSculkShriekerTooltips(lines, nbt);
				} else if (stack.isIn(ItemTags.SIGNS)) {
					addSignTooltips(lines, nbt);
				} else if (stack.isOf(Items.SPAWNER)) {
					addSpawnerTooltips(lines, nbt);
				}
				
				if (nbt.getBoolean(BiomeMakeoverCompat.CURSED_TAG)) {
					lines.add(Text.translatable("spectrum.tooltip.biomemakeover_cursed").formatted(Formatting.GRAY));
				}
			}
		});
	}
	
	private static void addSculkShriekerTooltips(List<Text> lines, NbtCompound nbt) {
		if (!nbt.contains("BlockStateTag", NbtElement.COMPOUND_TYPE)) {
			return;
		}
		NbtCompound blockStateTag = nbt.getCompound("BlockStateTag");
		if (Boolean.parseBoolean(blockStateTag.getString("can_summon"))) {
			lines.add(Text.translatable("spectrum.tooltip.able_to_summon_warden").formatted(Formatting.GRAY));
		}
	}
	
	private static void addSignTooltips(List<Text> lines, NbtCompound nbt) {
		if (!nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
			return;
		}
		NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
		addSignText(lines, SignText.CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("front_text")));
		addSignText(lines, SignText.CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("back_text")));
	}

	private static void addSignText(List<Text> lines, DataResult<SignText> signText) {
		if(signText.result().isPresent()) {
			SignText st = signText.result().get();
			Style style = Style.EMPTY.withColor(st.getColor().getSignColor());
			for (Text text : st.getMessages(false)) {
				lines.addAll(text.getWithStyle(style));
			}
		}
	}

	public static void addSpawnerTooltips(List<Text> lines, NbtCompound nbt) {
		if (!nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
			return;
		}
		
		Optional<EntityType<?>> entityType = Optional.empty();
		NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
		
		if (blockEntityTag.contains("SpawnData", NbtElement.COMPOUND_TYPE)
				&& blockEntityTag.getCompound("SpawnData").contains("entity", NbtElement.COMPOUND_TYPE)
				&& blockEntityTag.getCompound("SpawnData").getCompound("entity").contains("id", NbtElement.STRING_TYPE)) {
			String spawningEntityType = blockEntityTag.getCompound("SpawnData").getCompound("entity").getString("id");
			entityType = EntityType.get(spawningEntityType);
		}
		
		try {
			short spawnCount = blockEntityTag.getShort("SpawnCount");
			short minSpawnDelay = blockEntityTag.getShort("MinSpawnDelay");
			short maxSpawnDelay = blockEntityTag.getShort("MaxSpawnDelay");
			short spawnRange = blockEntityTag.getShort("SpawnRange");
			short requiredPlayerRange = blockEntityTag.getShort("RequiredPlayerRange");
			
			if (entityType.isPresent()) {
				lines.add(Text.translatable(entityType.get().getTranslationKey()));
			} else {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.unknown_mob"));
			}
			if (spawnCount > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.spawn_count", spawnCount).formatted(Formatting.GRAY));
			}
			if (minSpawnDelay > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.min_spawn_delay", minSpawnDelay).formatted(Formatting.GRAY));
			}
			if (maxSpawnDelay > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.max_spawn_delay", maxSpawnDelay).formatted(Formatting.GRAY));
			}
			if (spawnRange > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.spawn_range", spawnRange).formatted(Formatting.GRAY));
			}
			if (requiredPlayerRange > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.required_player_range", requiredPlayerRange).formatted(Formatting.GRAY));
			}
		} catch (Exception e) {
			lines.add(Text.translatable("item.spectrum.spawner.tooltip.unknown_mob"));
		}
	}
	
	
}
