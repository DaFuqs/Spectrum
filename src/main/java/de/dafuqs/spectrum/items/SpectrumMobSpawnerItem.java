package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class SpectrumMobSpawnerItem extends BlockItem {
	
	public SpectrumMobSpawnerItem(Block block, FabricItemSettings fabricItemSettings) {
		super(block, fabricItemSettings);
	}
	
	public static ItemStack toItemStack(MobSpawnerBlockEntity mobSpawnerBlockEntity) {
		ItemStack itemStack = new ItemStack(SpectrumItems.SPAWNER, 1);
		
		NbtCompound blockEntityTag = mobSpawnerBlockEntity.createNbt();
		blockEntityTag.remove("x");
		blockEntityTag.remove("y");
		blockEntityTag.remove("z");
		blockEntityTag.remove("id");
		blockEntityTag.remove("delay");
		
		NbtCompound itemStackTag = new NbtCompound();
		itemStackTag.put("BlockEntityTag", blockEntityTag);
		itemStack.setNbt(itemStackTag);
		return itemStack;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if (itemStack.getNbt() != null && itemStack.getNbt().get("BlockEntityTag") != null) {
			Optional<EntityType<?>> entityType = Optional.empty();
			
			NbtCompound blockEntityTag = itemStack.getNbt().getCompound("BlockEntityTag");
			
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
					tooltip.add(new TranslatableText(entityType.get().getTranslationKey()));
				} else {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.unknown_mob"));
				}
				if (spawnCount > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_count", spawnCount).formatted(Formatting.GRAY));
				}
				if (minSpawnDelay > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.min_spawn_delay", minSpawnDelay).formatted(Formatting.GRAY));
				}
				if (maxSpawnDelay > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.max_spawn_delay", maxSpawnDelay).formatted(Formatting.GRAY));
				}
				if (spawnRange > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_range", spawnRange).formatted(Formatting.GRAY));
				}
				if (requiredPlayerRange > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.required_player_range", requiredPlayerRange).formatted(Formatting.GRAY));
				}
			} catch (Exception e) {
				tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.unknown_mob"));
			}
		}
	}
	
}
