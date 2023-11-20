package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SimpleDefaultedRegistry.class)
public class DefaultedRegistryMixin {

    // TODO - Consider removing in the future, or finding some better way of fixing data than hacking DefaultedRegistry
    @ModifyVariable(at = @At("HEAD"), method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", ordinal = 0, argsOnly = true)
    Identifier spectrum$datafixerAtHome(@Nullable Identifier id) {
        if (id != null && id.getNamespace().equals("spectrum")) {
            if (id.getPath().equals("vanilla_semi_permeable_glass")) {
                return SpectrumCommon.locate("semi_permeable_glass");
            }
        }
        return id;
    }
}
