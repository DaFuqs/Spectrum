package de.dafuqs.spectrum.api.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.village.poi.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface POIMemorized {
	
	String POI_POS_KEY = "POIPos";
	
	TagKey<PoiType> getPOITag();
	
	@Nullable BlockPos getPOIPos();
	
	void setPOIPos(@Nullable BlockPos blockPos);
	
	default void writePOIPosToNbt(CompoundTag nbt) {
		@Nullable BlockPos poiPos = getPOIPos();
		if (poiPos != null) {
			nbt.put(POI_POS_KEY, NbtUtils.writeBlockPos(poiPos));
		}
	}
	
	default void readPOIPosFromNbt(CompoundTag nbt) {
		if (nbt.contains(POI_POS_KEY)) {
			setPOIPos(NbtUtils.readBlockPos(nbt, POI_POS_KEY).orElse(null));
		}
	}
	
	default boolean isPOIValid(ServerLevel world) {
		@Nullable BlockPos poiPos = getPOIPos();
		if (poiPos == null) {
			return false;
		}
		Optional<Holder<PoiType>> type = world.getPoiManager().getType(poiPos);
		return type.map(pointOfInterestTypeRegistryEntry -> pointOfInterestTypeRegistryEntry.is(SpectrumPointOfInterestTypeTags.LIZARD_DENS)).orElse(false);
	}
	
	default @Nullable BlockPos findNearestPOI(ServerLevel world, BlockPos pos, int maxDistance) {
		PoiManager pointOfInterestStorage = world.getPoiManager();
		
		return pointOfInterestStorage.findClosest(
				(poiType) -> poiType.is(getPOITag()),
				pos, maxDistance, PoiManager.Occupancy.ANY).orElse(null);
	}
	
}
