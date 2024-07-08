package de.dafuqs.spectrum.api.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class DynamicRenderModel extends ForwardingBakedModel implements UnbakedModel {
    private static class WrappingOverridesList extends ModelOverrideList {
        private final ModelOverrideList wrapped;
        private WrappingOverridesList(ModelOverrideList orig) {
            super(null, null, List.of());
            this.wrapped = orig;
        }

        @Nullable
        @Override
        public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            BakedModel newModel = wrapped.apply(model, stack, world, entity, seed);
            return newModel == model ? model : new DynamicRenderModel(newModel);
        }
    }
    // only used pre-bake
    private UnbakedModel baseUnbaked;

    // could be used again if pre-bake model problems get figured out
    public DynamicRenderModel(UnbakedModel base) {
        this.baseUnbaked = base;
    }

    // post-bake post-override constructor
    public DynamicRenderModel(BakedModel base) {
        this.wrapped = base instanceof DynamicRenderModel fm ? fm.getWrappedModel() : base;
    }

    // avoid FAPI builtin model lookup
    @Override
    public boolean isBuiltin() {
        return false;
    }

    private DynamicRenderModel wrap(BakedModel model) {
        this.wrapped = model instanceof DynamicRenderModel fm ? fm.getWrappedModel() : model;
        return this;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return this.baseUnbaked.getModelDependencies();
    }

    // override so wrap persists over override
    // ensures that renderer is called
    @Override
    public ModelOverrideList getOverrides() {
        return new WrappingOverridesList(super.getOverrides());
    }

    // return empty transform to prevent double apply in render
    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        this.baseUnbaked.setParents(modelLoader);
    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return this.wrap(this.baseUnbaked.bake(baker, textureGetter, rotationContainer, modelId));
    }
}
