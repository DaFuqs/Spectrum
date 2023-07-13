package de.dafuqs.spectrum.blocks.memory;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MemoryItem extends BlockItem {
	
	// There are a few entities in vanilla that do not have a corresponding spawn egg
	// therefore to make it nicer we specify custom colors for them here
	private static final HashMap<EntityType<?>, Pair<Integer, Integer>> customColors = new HashMap<>() {{
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
	
	public static ItemStack getMemoryForEntity(LivingEntity entity) {
		NbtCompound tag = new NbtCompound();
		entity.saveSelfNbt(tag);
		tag.remove("Pos"); // yeet everything that we don't need and could interfere when spawning
		tag.remove("OnGround");
		tag.remove("Rotation");
		tag.remove("Motion");
		tag.remove("FallDistance");
		tag.remove("InLove");
		tag.remove("UUID");
		tag.remove("Health");
		tag.remove("Fire");
		tag.remove("HurtByTimestamp");
		tag.remove("DeathTime");
		tag.remove("AbsorptionAmount");
		tag.remove("Air");
		tag.remove("FallFlying");
		tag.remove("PortalCooldown");
		tag.remove("HurtTime");
		
		ItemStack stack = SpectrumBlocks.MEMORY.asItem().getDefaultStack();
		NbtCompound stackNbt = stack.getOrCreateNbt();
		stackNbt.put("EntityTag", tag);
		stack.setNbt(stackNbt);
		
		return stack;
	}
	
	public static Optional<EntityType<?>> getEntityType(@Nullable NbtCompound nbt) {
		if (nbt != null && nbt.contains("EntityTag", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("EntityTag");
			if (nbtCompound.contains("id", NbtElement.STRING_TYPE)) {
				return EntityType.get(nbtCompound.getString("id"));
			}
		}
		return Optional.empty();
	}
	
	public static @Nullable Text getMemoryEntityCustomName(@Nullable NbtCompound nbt) {
		if (nbt != null && nbt.contains("EntityTag", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("EntityTag");
			if (nbtCompound.contains("CustomName", NbtElement.STRING_TYPE)) {
				return Text.Serializer.fromJson(nbtCompound.getString("CustomName"));
			}
		}
		return null;
	}
	
	public static boolean isBrokenPromise(@Nullable NbtCompound nbt) {
		return nbt != null && nbt.getBoolean("BrokenPromise");
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
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		nbtCompound.putInt("TicksToManifest", newTicksToManifest);
		itemStack.setNbt(nbtCompound);
	}
	
	public static void setSpawnAsAdult(@NotNull ItemStack itemStack, boolean spawnAsAdult) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		if (spawnAsAdult) {
			nbtCompound.putBoolean("SpawnAsAdult", true);
		} else {
			nbtCompound.remove("SpawnAsAdult");
		}
		itemStack.setNbt(nbtCompound);
	}
	
	public static void markAsBrokenPromise(ItemStack itemStack, boolean isBrokenPromise) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		if (isBrokenPromise) {
			nbtCompound.putBoolean("BrokenPromise", true);
		} else {
			nbtCompound.remove("BrokenPromise");
		}
		itemStack.setNbt(nbtCompound);
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
			EntityType<?> type = entityType.get();
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
				tooltip.add(Text.translatable("item.spectrum.memory.tooltip.unrecognizable_entity_type").formatted(Formatting.GRAY));
			} else {
				boolean isBrokenPromise = isBrokenPromise(nbt);
				Text customName = getMemoryEntityCustomName(nbt);
				if (isBrokenPromise) {
					if (customName == null) {
						tooltip.add(Text.translatable("item.spectrum.memory.tooltip.entity_type_broken_promise", entityType.get().getName()));
					} else {
						tooltip.add(Text.translatable("item.spectrum.memory.tooltip.named_broken_promise").append(customName).formatted(Formatting.WHITE, Formatting.ITALIC));
					}
				} else {
					if (customName == null) {
						tooltip.add(Text.translatable("item.spectrum.memory.tooltip.entity_type", entityType.get().getName()));
					} else {
						tooltip.add(Text.translatable("item.spectrum.memory.tooltip.named").append(customName).formatted(Formatting.WHITE, Formatting.ITALIC));
					}
				}
			}
		} else {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.unset_entity_type").formatted(Formatting.GRAY));
			return;
		}
		
		if (ticksToHatch <= 0) {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.does_not_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 100) {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.extra_long_time_to_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 20) {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.long_time_to_manifest").formatted(Formatting.GRAY));
		} else if (ticksToHatch > 5) {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.medium_time_to_manifest").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.memory.tooltip.short_time_to_manifest").formatted(Formatting.GRAY));
		}
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		
		// adding all memories that have spirit instiller recipes
		if (this.isIn(group) && SpectrumCommon.minecraftServer != null) {
			Item memoryItem = SpectrumBlocks.MEMORY.asItem();
			var drm = SpectrumCommon.minecraftServer.getRegistryManager();
			for (SpiritInstillerRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.SPIRIT_INSTILLING)) {
				if (recipe.getOutput(drm).isOf(memoryItem)) {
					stacks.add(recipe.getOutput(drm));
				}
			}
		}
	}
	
}
