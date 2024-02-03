package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.render.capes.*;
import net.fabricmc.api.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;


@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientEntityMixin {
    @Inject(
            method = "getCapeTexture",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        var cape = WorthinessChecker.getCapeType(((Entity) (Object) (this)).getUuid());
        if (cape.render) {
            cir.setReturnValue(cape.capePath);
            cir.cancel();
        }
    }
}
