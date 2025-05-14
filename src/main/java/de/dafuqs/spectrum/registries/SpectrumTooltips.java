package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.client.item.v1.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class SpectrumTooltips {
	
	public static void register() {
		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
			DataComponentMap components = stack.getComponents();
			if (!components.isEmpty()) {
				if (stack.is(Blocks.SCULK_SHRIEKER.asItem())) {
					addSculkShriekerTooltips(lines, components);
				} else if (stack.is(ItemTags.SIGNS)) {
					//addSignTooltips(lines, components);
				} else if (stack.is(Items.SPAWNER)) {
					//addSpawnerTooltips(lines, components);
				}
			}
		});
	}
	
	private static void addSculkShriekerTooltips(List<Component> lines, DataComponentMap components) {
		BlockItemStateProperties stateComponent = components.get(DataComponents.BLOCK_STATE);
		if (stateComponent != null && !stateComponent.isEmpty()) {
			if (Boolean.TRUE.equals(stateComponent.get(SculkShriekerBlock.CAN_SUMMON))) {
				lines.add(Component.translatable("spectrum.tooltip.able_to_summon_warden").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	/* TODO
	
	private static void addSignTooltips(List<Text> lines, ComponentMap components) {
		NbtComponent dataComponent = components.get(DataComponentTypes.BLOCK_ENTITY_DATA);
		if (dataComponent == null) {
			return;
		}
		NbtCompound blockEntityTag = dataComponent.getNbt().getCompound("BlockEntityTag");
		addSignText(lines, SignText.CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("front_text")));
		addSignText(lines, SignText.CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("back_text")));
	}
	
	private static void addSignText(List<Text> lines, DataResult<SignText> signText) {
		if (signText.result().isPresent()) {
			SignText st = signText.result().get();
			Style style = Style.EMPTY.withColor(st.getColor().getSignColor());
			for (Text text : st.getMessages(false)) {
				lines.addAll(text.getWithStyle(style));
			}
		}
	}
	
	public static void addSpawnerTooltips(List<Text> lines, ComponentMap components) {
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
			short maxNearbyEntities = blockEntityTag.getShort("MaxNearbyEntities");
			
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
			if (maxNearbyEntities > 0) {
				lines.add(Text.translatable("item.spectrum.spawner.tooltip.max_nearby_entities", maxNearbyEntities).formatted(Formatting.GRAY));
			}
		} catch (Exception e) {
			lines.add(Text.translatable("item.spectrum.spawner.tooltip.unknown_mob"));
		}
	}*/
	
	
}
