package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.items.SpectrumItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class Spawner extends BlockItem {

    public Spawner(Block block, FabricItemSettings fabricItemSettings) {
        super(block, fabricItemSettings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if(itemStack.getTag() != null && itemStack.getTag().get("BlockEntityTag") != null) {
            Optional<EntityType<?>> entityType;

            CompoundTag blockEntityTag = itemStack.getTag().getCompound("BlockEntityTag");

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

    public static ItemStack fromBlockEntity(BlockEntity blockEntity) {
        ItemStack itemStack = new ItemStack(SpectrumItems.SPAWNER, 1);

        CompoundTag blockEntityTag = new CompoundTag();
        blockEntity.toTag(blockEntityTag);

        blockEntityTag.remove("x");
        blockEntityTag.remove("y");
        blockEntityTag.remove("z");
        blockEntityTag.remove("id");
        blockEntityTag.remove("delay");

        CompoundTag itemStackTag = new CompoundTag();
        itemStackTag.put("BlockEntityTag", blockEntityTag);
        itemStack.setTag(itemStackTag);
        return itemStack;
    }

}
