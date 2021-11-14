package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class SpawnerItem extends BlockItem {

	public SpawnerItem(Block block, FabricItemSettings fabricItemSettings) {
		super(block, fabricItemSettings);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if(itemStack.getNbt() != null && itemStack.getNbt().get("BlockEntityTag") != null) {
			Optional<EntityType<?>> entityType;

			NbtCompound blockEntityTag = itemStack.getNbt().getCompound("BlockEntityTag");

			String spawningEntityType = blockEntityTag.getCompound("SpawnData").getString("id");
			entityType = EntityType.get(spawningEntityType);
			short spawnCount = blockEntityTag.getShort("SpawnCount");
			short minSpawnDelay = blockEntityTag.getShort("MinSpawnDelay");
			short maxSpawnDelay = blockEntityTag.getShort("MaxSpawnDelay");
			short spawnRange = blockEntityTag.getShort("SpawnRange");
			short requiredPlayerRange = blockEntityTag.getShort("RequiredPlayerRange");

			if(entityType.isPresent()) {
				tooltip.add(new TranslatableText(entityType.get().getTranslationKey()));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.unknown_mob"));
			}
			tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_count", spawnCount));
			tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.min_spawn_delay", minSpawnDelay));
			tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.max_spawn_delay", maxSpawnDelay));
			tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_range", spawnRange));
			tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.required_player_range", requiredPlayerRange));
		}
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

}
