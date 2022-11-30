package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelOverrideList.BakedOverride.class)
public interface BakedOverrideAccessor {
	
	@Accessor()
	BakedModel getModel();
}
