package de.dafuqs.spectrum.mixin;

import com.mojang.serialization.DataResult;
import de.dafuqs.spectrum.items.map.StructureMapState;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MapState.class)
public class MapStateMixin {

    @Inject(method = "fromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;<init>(IIBZZZLnet/minecraft/registry/RegistryKey;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void spectrum$fromNbt(NbtCompound nbt, CallbackInfoReturnable<MapState> cir, RegistryKey<World> dimension, int centerX, int centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked) {
        if (nbt.contains("isSpectrumMap", NbtElement.BYTE_TYPE) && nbt.getBoolean("isSpectrumMap")) {
            cir.setReturnValue(new StructureMapState(centerX, centerZ, scale, showIcons, unlimitedTracking, locked, dimension, nbt));
        }
    }

}
