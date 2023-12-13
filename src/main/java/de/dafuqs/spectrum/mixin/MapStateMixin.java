package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.map.StructureMapState;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MapState.class)
public class MapStateMixin {

    // Caches the created state between the two mixins
    @Nullable
    private static StructureMapState structureMapState = null;

    @Inject(method = "fromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;<init>(IIBZZZLnet/minecraft/registry/RegistryKey;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void spectrum$fromNbt_newMapState(NbtCompound nbt, CallbackInfoReturnable<MapState> cir, RegistryKey<World> dimension, int centerX, int centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked) {
        if (nbt.contains("isSpectrumMap", NbtElement.BYTE_TYPE) && nbt.getBoolean("isSpectrumMap")) {
            structureMapState = new StructureMapState(centerX, centerZ, scale, showIcons, unlimitedTracking, locked, dimension, nbt);
        }
    }

    @ModifyVariable(
            method = "fromNbt",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;<init>(IIBZZZLnet/minecraft/registry/RegistryKey;)V"),
                    to = @At(value = "TAIL")
            ),
            at = @At(value = "STORE")
    )
    private static MapState spectrum$fromNbt_storeMapState(MapState vanillaState) {
        if (structureMapState != null) {
            StructureMapState state = structureMapState;
            structureMapState = null;
            return state;
        }
        return vanillaState;
    }

}
