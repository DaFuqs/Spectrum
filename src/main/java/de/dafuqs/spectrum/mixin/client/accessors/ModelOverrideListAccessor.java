package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.render.model.json.ModelOverrideList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelOverrideList.class)
public interface ModelOverrideListAccessor {

    @Accessor()
    ModelOverrideList.BakedOverride[] getOverrides();
}
