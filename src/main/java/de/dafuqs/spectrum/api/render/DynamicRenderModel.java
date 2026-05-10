package de.dafuqs.spectrum.api.render;

import net.fabricmc.api.*;
import net.fabricmc.fabric.api.renderer.v1.model.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class DynamicRenderModel extends ForwardingBakedModel {
	private static class WrappingOverridesList extends ItemOverrides {
		private final ItemOverrides wrapped;
		
		private WrappingOverridesList(ItemOverrides orig) {
            super(null, null, List.of());
            this.wrapped = orig;
        }

		@Override
		public @Nullable BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
			BakedModel newModel = wrapped.resolve(model, stack, world, entity, seed);
            return newModel == model ? model : new DynamicRenderModel(newModel);
        }
    }

    // post-bake post-override constructor
    public DynamicRenderModel(BakedModel base) {
        this.wrapped = base instanceof DynamicRenderModel fm ? fm.getWrappedModel() : base;
    }

    // avoid FAPI builtin model lookup
    @Override
	public boolean isCustomRenderer() {
        return false;
    }

    private DynamicRenderModel wrap(BakedModel model) {
        this.wrapped = model instanceof DynamicRenderModel fm ? fm.getWrappedModel() : model;
        return this;
    }

    // override so wrap persists over override
    // ensures that renderer is called
    @Override
	public ItemOverrides getOverrides() {
        return new WrappingOverridesList(super.getOverrides());
    }

    // return empty transform to prevent double apply in render
    @Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
    }
}
