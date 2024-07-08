package de.dafuqs.spectrum.api.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.poi.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface POIMemorized {
	
	String POI_POS_KEY = "POIPos";
	
	TagKey<PointOfInterestType> getPOITag();
	
	@Nullable BlockPos getPOIPos();
	
	void setPOIPos(@Nullable BlockPos blockPos);
	
	default void writePOIPosToNbt(NbtCompound nbt) {
		@Nullable BlockPos poiPos = getPOIPos();
		if (poiPos != null) {
			nbt.put(POI_POS_KEY, NbtHelper.fromBlockPos(poiPos));
		}
	}
	
	default void readPOIPosFromNbt(NbtCompound nbt) {
		if (nbt.contains(POI_POS_KEY)) {
			setPOIPos(NbtHelper.toBlockPos(nbt.getCompound(POI_POS_KEY)));
		}
	}
	
	default boolean isPOIValid(ServerWorld world) {
		@Nullable BlockPos poiPos = getPOIPos();
		if (poiPos == null) {
			return false;
		}
		Optional<RegistryEntry<PointOfInterestType>> type = world.getPointOfInterestStorage().getType(poiPos);
		return type.map(pointOfInterestTypeRegistryEntry -> pointOfInterestTypeRegistryEntry.isIn(SpectrumPointOfInterestTypeTags.LIZARD_DENS)).orElse(false);
	}
	
	default @Nullable BlockPos findNearestPOI(ServerWorld world, BlockPos pos, int maxDistance) {
		PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
		
		return pointOfInterestStorage.getNearestPosition(
				(poiType) -> poiType.isIn(getPOITag()),
				pos, maxDistance, PointOfInterestStorage.OccupationStatus.ANY).orElse(null);
	}
	
}
