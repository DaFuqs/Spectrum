package de.dafuqs.spectrum.items.magic_items;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StructureCompassItem extends CompassItem {
	
	protected final TagKey<Structure> locatedStructures;
	
	public StructureCompassItem(Properties settings, TagKey<Structure> locatedStructures) {
		super(settings);
		this.locatedStructures = locatedStructures;
	}
	
	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide() && world.getGameTime() % 200 == 0) {
			locateStructure(stack, world, entity);
		}
	}
	
	protected void locateStructure(@NotNull ItemStack stack, @NotNull Level world, Entity entity) {
		Pair<BlockPos, Holder<Structure>> foundStructure = locateStructure((ServerLevel) world, entity.blockPosition());
		if (foundStructure != null) {
			saveStructurePos(stack, world.dimension(), foundStructure.getFirst());
		} else {
			removeStructurePos(stack);
		}
	}
	
	public @Nullable Pair<BlockPos, Holder<Structure>> locateStructure(@NotNull ServerLevel world, @NotNull BlockPos pos) {
		Optional<HolderSet.Named<Structure>> registryEntryList = SpectrumStructureTags.entriesOf(world, locatedStructures);
		return registryEntryList.map(registryEntries ->
						world.getChunkSource().getGenerator().findNearestMapStructure(world, registryEntries, pos, 100, false))
				.orElse(null);
	}
	
	public static @Nullable GlobalPos getStructurePos(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.TARGETED_STRUCTURE, null);
	}
	
	protected void saveStructurePos(ItemStack stack, @NotNull ResourceKey<Level> worldKey, @NotNull BlockPos pos) {
		stack.set(SpectrumDataComponentTypes.TARGETED_STRUCTURE, new GlobalPos(worldKey, pos));
	}
	
	protected void removeStructurePos(@NotNull ItemStack stack) {
		stack.remove(SpectrumDataComponentTypes.TARGETED_STRUCTURE);
	}
	
}
