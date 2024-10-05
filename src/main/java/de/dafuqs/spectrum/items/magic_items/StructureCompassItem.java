package de.dafuqs.spectrum.items.magic_items;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.structure.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StructureCompassItem extends CompassItem {
	
	protected final TagKey<Structure> locatedStructures;
	
	public StructureCompassItem(Settings settings, TagKey<Structure> locatedStructures) {
		super(settings);
		this.locatedStructures = locatedStructures;
	}
	
	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient && world.getTime() % 200 == 0) {
			locateStructure(stack, world, entity);
		}
	}

	protected void locateStructure(@NotNull ItemStack stack, @NotNull World world, Entity entity) {
		Pair<BlockPos, RegistryEntry<Structure>> foundStructure = locateStructure((ServerWorld) world, entity.getBlockPos());
		if (foundStructure != null) {
			saveStructurePos(stack, world.getRegistryKey(), foundStructure.getFirst());
		} else {
			removeStructurePos(stack);
		}
	}

	public @Nullable Pair<BlockPos, RegistryEntry<Structure>> locateStructure(@NotNull ServerWorld world, @NotNull BlockPos pos) {
		Optional<RegistryEntryList.Named<Structure>> registryEntryList = SpectrumStructureTags.entriesOf(world, locatedStructures);
		if (registryEntryList.isPresent()) {
			return world.getChunkManager().getChunkGenerator().locateStructure(world, registryEntryList.get(), pos, 100, false);
		} else {
			return null;
		}
	}
	
	public static boolean hasStructure(@NotNull ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		return nbtCompound != null && (nbtCompound.contains("StructureDimension") && nbtCompound.contains("StructurePos"));
	}
	
	public static @Nullable GlobalPos getStructurePos(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null) {
			return null;
		}
		boolean bl = nbt.contains("StructurePos");
		boolean bl2 = nbt.contains("StructureDimension");
		if (bl && bl2) {
			Optional<RegistryKey<World>> optional = World.CODEC.parse(NbtOps.INSTANCE, nbt.get("StructureDimension")).result();
			if (optional.isPresent()) {
				BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("StructurePos"));
				return GlobalPos.create(optional.get(), blockPos);
			}
		}
		return null;
	}
	
	protected void saveStructurePos(ItemStack stack, @NotNull RegistryKey<World> worldKey, @NotNull BlockPos pos) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.put("StructurePos", NbtHelper.fromBlockPos(pos));
		nbt.putString("StructureDimension", worldKey.getValue().toString());
	}
	
	protected void removeStructurePos(@NotNull ItemStack stack) {
		stack.removeSubNbt("StructurePos");
		stack.removeSubNbt("StructureDimension");
	}
	
}
