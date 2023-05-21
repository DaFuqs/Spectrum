package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.render.model.json.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ModelOverrideList.class)
public interface ModelOverrideListAccessor {
	
	@Accessor()
	ModelOverrideList.BakedOverride[] getOverrides();
}
