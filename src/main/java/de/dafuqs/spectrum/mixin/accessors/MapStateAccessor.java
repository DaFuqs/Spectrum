package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.level.saveddata.maps.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(MapItemSavedData.class)
public interface MapStateAccessor {
	
	@Accessor
	boolean getTrackingPosition();
	
	@Accessor
	boolean getUnlimitedTracking();
	
	@Accessor
	Map<String, MapBanner> getBannerMarkers();
	
	@Accessor
	Map<String, MapDecoration> getDecorations();
	
	@Accessor
	int getTrackedDecorationCount();
	
	@Accessor
	void setTrackedDecorationCount(int decorationCount);
	
	@Invoker
	void invokeSetDecorationsDirty();
	
	@Invoker
	void invokeRemoveDecoration(String id);
	
}
