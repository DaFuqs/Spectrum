package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.items.map.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.saveddata.maps.*;
import org.jspecify.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MapItemSavedData.class)
public class MapStateMixin {
	
	// Caches the created state between the two mixins
	@Unique
	@Nullable
	private static ArtisansAtlasState atlasState = null;
	
	@Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;<init>(IIBZZZLnet/minecraft/resources/ResourceKey;)V"))
	private static void spectrum$fromNbt_newMapState(CompoundTag nbt, HolderLookup.Provider registryLookup, CallbackInfoReturnable<MapItemSavedData> cir, @Local ResourceKey<Level> registryKey, @Local(ordinal = 0) int centerX, @Local(ordinal = 1) int centerZ, @Local byte scale, @Local(ordinal = 0) boolean showIcons, @Local(ordinal = 1) boolean unlimitedTracking, @Local(ordinal = 2) boolean locked) {
		if (nbt.contains("isArtisansAtlas", Tag.TAG_BYTE) && nbt.getBoolean("isArtisansAtlas")) {
			atlasState = new ArtisansAtlasState(centerX, centerZ, scale, showIcons, unlimitedTracking, locked, registryKey, nbt);
		}
	}
	
	@ModifyVariable(
			method = "load",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;<init>(IIBZZZLnet/minecraft/resources/ResourceKey;)V"),
					to = @At(value = "TAIL")
			),
			at = @At(value = "STORE")
	)
	private static MapItemSavedData spectrum$fromNbt_storeMapState(MapItemSavedData vanillaState) {
		if (atlasState != null) {
			ArtisansAtlasState state = atlasState;
			atlasState = null;
			return state;
		}
		return vanillaState;
	}
	
}
