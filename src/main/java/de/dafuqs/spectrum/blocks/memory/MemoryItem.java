package de.dafuqs.spectrum.blocks.memory;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class MemoryItem extends BlockItem {
	
	public MemoryItem(Block block, Settings settings) {
		super(block, settings);
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
	
	public static void setTicksToManifest(ItemStack itemStack, int newTicksToManifest) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if(nbtCompound != null) {
			nbtCompound.putInt("TicksToManifest", newTicksToManifest);
			itemStack.setNbt(nbtCompound);
		}
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
