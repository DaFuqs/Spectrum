package de.dafuqs.spectrum.blocks.memory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MemoryItem extends BlockItem {
	
	public MemoryItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		MinecraftServer minecraftServer = world.getServer();
		if (minecraftServer != null && !world.isClient) {
			NbtCompound nbtCompound = getBlockEntityNbt(stack);
			if (nbtCompound != null) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
					memoryBlockEntity.setData(player, stack);
					return true;
				}
			}
		}
		return false;
	}
	
	public static Optional<EntityType<?>> getEntityType(@Nullable NbtCompound nbt) {
		if (nbt != null && nbt.contains("EntityTag", 10)) {
			NbtCompound nbtCompound = nbt.getCompound("EntityTag");
			if (nbtCompound.contains("id", NbtElement.STRING_TYPE)) {
				return EntityType.get(nbtCompound.getString("id"));
			}
		}
		return Optional.empty();
	}
	
	// Same nbt format as SpawnEggs
	// That way we can reuse entityType.spawnFromItemStack()
	public static int getTicksToManifest(@Nullable NbtCompound nbtCompound) {
		if(nbtCompound != null && nbtCompound.contains("TicksToManifest", NbtElement.INT_TYPE)) {
			return nbtCompound.getInt("TicksToManifest");
		}
		return -1;
	}
	
	public static boolean isEntityTypeUnrecognizable(@Nullable NbtCompound nbtCompound) {
		if(nbtCompound != null && nbtCompound.contains("Unrecognizable", NbtElement.INT_TYPE)) {
			return nbtCompound.getBoolean("Unrecognizable");
		}
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		NbtCompound nbt = stack.getNbt();
		Optional<EntityType<?>> entityType = getEntityType(nbt);
		int ticksToHatch = getTicksToManifest(nbt);
		
		if(entityType.isPresent()) {
			if(isEntityTypeUnrecognizable(nbt)) {
				tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.unrecognizable_entity_type").formatted(Formatting.GRAY));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.entity_type", entityType.get().getName()));
			}
		} else {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.unset_entity_type").formatted(Formatting.GRAY));
		}
		
		if(ticksToHatch <= 0) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.does_not_manifest").formatted(Formatting.GRAY));
		} else if(ticksToHatch > 100) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.extra_long_time_to_manifest").formatted(Formatting.GRAY));
		} else if(ticksToHatch > 20) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.long_time_to_manifest").formatted(Formatting.GRAY));
		} else if(ticksToHatch > 5) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.medium_time_to_manifest").formatted(Formatting.GRAY));
		} else {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.short_time_to_manifest").formatted(Formatting.GRAY));
		}
	}
	
}
