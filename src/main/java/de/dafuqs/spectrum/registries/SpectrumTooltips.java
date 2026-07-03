package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.event.entity.player.*;

import java.util.*;

public class SpectrumTooltips {
	
	public static void register(ItemTooltipEvent event) {
		var stack = event.getItemStack();
		
		DataComponentMap components = stack.getComponents();
		if (!components.isEmpty()) {
			if (stack.is(Blocks.SCULK_SHRIEKER.asItem())) {
				addSculkShriekerTooltips(event.getToolTip(), components);
			} else if (stack.is(ItemTags.SIGNS)) {
				addSignTooltips(event.getToolTip(), components);
			} else if (stack.is(Items.SPAWNER)) {
				addSpawnerTooltips(event.getToolTip(), components);
			}
		}
	}
	
	private static void addSculkShriekerTooltips(List<Component> lines, DataComponentMap components) {
		BlockItemStateProperties stateComponent = components.get(DataComponents.BLOCK_STATE);
		if (stateComponent != null && !stateComponent.isEmpty()) {
			if (Boolean.TRUE.equals(stateComponent.get(SculkShriekerBlock.CAN_SUMMON))) {
				lines.add(Component.translatable("spectrum.tooltip.able_to_summon_warden").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	private static void addSignTooltips(List<Component> lines, DataComponentMap components) {
		CustomData data = components.get(DataComponents.BLOCK_ENTITY_DATA);
		if (data == null) {
			return;
		}
		CompoundTag blockEntityTag = data.getUnsafe();
		addSignText(lines, SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("front_text")));
		addSignText(lines, SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, blockEntityTag.getCompound("back_text")));
	}
	
	private static void addSignText(List<Component> lines, DataResult<SignText> signText) {
		if (signText.result().isPresent()) {
			SignText st = signText.result().get();
			lines.addAll(Arrays.asList(st.getMessages(false)));
		}
	}
	
	public static void addSpawnerTooltips(List<Component> lines, DataComponentMap components) {
		CustomData data = components.get(DataComponents.BLOCK_ENTITY_DATA);
		if (data == null) {
			return;
		}
		CompoundTag blockEntityTag = data.getUnsafe();
		Optional<EntityType<?>> entityType = Optional.empty();
		
		if (blockEntityTag.contains("SpawnData")
				&& blockEntityTag.getCompound("SpawnData").contains("entity")
				&& blockEntityTag.getCompound("SpawnData").getCompound("entity").contains("id")) {
			String spawningEntityType = blockEntityTag.getCompound("SpawnData").getCompound("entity").getString("id");
			entityType = EntityType.byString(spawningEntityType);
		}
		
		short spawnCount = blockEntityTag.getShort("SpawnCount");
		short minSpawnDelay = blockEntityTag.getShort("MinSpawnDelay");
		short maxSpawnDelay = blockEntityTag.getShort("MaxSpawnDelay");
		short spawnRange = blockEntityTag.getShort("SpawnRange");
		short requiredPlayerRange = blockEntityTag.getShort("RequiredPlayerRange");
		short maxNearbyEntities = blockEntityTag.getShort("MaxNearbyEntities");
		
		if (entityType.isPresent()) {
			lines.add(entityType.get().getDescription());
		} else {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.unknown_mob"));
		}
		if (spawnCount > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.spawn_count", spawnCount).withStyle(ChatFormatting.GRAY));
		}
		if (minSpawnDelay > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.min_spawn_delay", minSpawnDelay).withStyle(ChatFormatting.GRAY));
		}
		if (maxSpawnDelay > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.max_spawn_delay", maxSpawnDelay).withStyle(ChatFormatting.GRAY));
		}
		if (spawnRange > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.spawn_range", spawnRange).withStyle(ChatFormatting.GRAY));
		}
		if (requiredPlayerRange > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.required_player_range", requiredPlayerRange).withStyle(ChatFormatting.GRAY));
		}
		if (maxNearbyEntities > 0) {
			lines.add(Component.translatable("item.spectrum.spawner.tooltip.max_nearby_entities", maxNearbyEntities).withStyle(ChatFormatting.GRAY));
		}
	}
	
	
}
