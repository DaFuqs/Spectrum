package de.dafuqs.spectrum.blocks.memory;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MemoryItem extends BlockItem {
	
	// There are a few entities in vanilla that do not have a corresponding spawn egg
	// therefore to make it nicer we specify custom colors for them here
	private static final HashMap<EntityType, Pair<Integer, Integer>> customColors = new HashMap<>() {{
		put(EntityType.BAT, new Pair<>(0x463d2b, 0x191307));
		put(EntityType.SNOW_GOLEM, new Pair<>(0xc9cbcf, 0xa26e28));
		put(EntityType.WITHER, new Pair<>(0x101211, 0x3e4140));
		put(EntityType.ILLUSIONER, new Pair<>(0x29578d, 0x4b4e4f));
		put(EntityType.ENDER_DRAGON, new Pair<>(0x111111, 0x856c8f));
		put(EntityType.IRON_GOLEM, new Pair<>(0x9a9a9a, 0x8b7464));
	}};
	
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
		if (nbtCompound != null && nbtCompound.contains("TicksToManifest", NbtElement.INT_TYPE)) {
			return nbtCompound.getInt("TicksToManifest");
		}
		return -1;
	}
	
	public static void setTicksToManifest(@NotNull ItemStack itemStack, int newTicksToManifest) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null) {
			nbtCompound.putInt("TicksToManifest", newTicksToManifest);
			itemStack.setNbt(nbtCompound);
		}
	}
	
	public static int getEggColor(NbtCompound nbtCompound, int tintIndex) {
		if (nbtCompound == null || isEntityTypeUnrecognizable(nbtCompound)) {
			if (tintIndex == 0) {
				return 0x222222;
			} else {
				return 0xDDDDDD;
			}
		}
		
		Optional<EntityType<?>> entityType = MemoryItem.getEntityType(nbtCompound);
		if (entityType.isPresent()) {
			EntityType type = entityType.get();
			if (customColors.containsKey(type)) {
				// statically defined: fetch from map
				return tintIndex == 0 ? customColors.get(type).getLeft() : customColors.get(type).getRight();
			} else {
				// dynamically defined: fetch from spawn egg
				SpawnEggItem spawnEggItem = SpawnEggItem.forEntity(entityType.get());
				if (spawnEggItem != null) {
					return spawnEggItem.getColor(tintIndex);
				}
			}
		}
		
		if (tintIndex == 0) {
			return 0x222222;
		} else {
			return 0xDDDDDD;
		}
	}
	
	public static boolean isEntityTypeUnrecognizable(@Nullable NbtCompound nbtCompound) {
		if (nbtCompound != null && nbtCompound.contains("Unrecognizable")) {
			return nbtCompound.getBoolean("Unrecognizable");
		}
		return false;
	}
	
	public static void makeUnrecognizable(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		nbtCompound.putBoolean("Unrecognizable", true);
		itemStack.setNbt(nbtCompound);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		NbtCompound nbt = stack.getNbt();
		Optional<EntityType<?>> entityType = getEntityType(nbt);
		int ticksToHatch = getTicksToManifest(nbt);
		
		if (entityType.isPresent()) {
			if (isEntityTypeUnrecognizable(nbt)) {
				tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.unrecognizable_entity_type").formatted(Formatting.GRAY));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.entity_type", entityType.get().getName()));
			}
		} else {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.unset_entity_type").formatted(Formatting.GRAY));
			return;
		}
		
		if (ticksToHatch <= 0) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.does_not_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 100) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.extra_long_time_to_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 20) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.long_time_to_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 5) {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.medium_time_to_manifest").formatted(Formatting.GRAY));
		} else {
			tooltip.add(new TranslatableText("item.spectrum.memory.tooltip.short_time_to_manifest").formatted(Formatting.GRAY));
		}
	}
	
}
