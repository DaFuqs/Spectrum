package de.dafuqs.spectrum.api.render;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;


public class DynamicRenderModel extends ForwardingBakedModel implements UnbakedModel {
	
	public static final ObjectOpenHashSet<ModelResourceLocation> CUSTOM_ITEM_MODELS = new ObjectOpenHashSet<>();
	
	private static class WrappingOverridesList extends ItemOverrides {
		private final ItemOverrides wrapped;
		
		private WrappingOverridesList(ItemOverrides orig) {
			super(null, null, List.of());
			this.wrapped = orig;
		}
		
		@Nullable
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
			BakedModel newModel = wrapped.resolve(model, stack, world, entity, seed);
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
	public boolean isCustomRenderer() {
		return false;
	}
	
	private DynamicRenderModel wrap(BakedModel model) {
		this.wrapped = model instanceof DynamicRenderModel fm ? fm.getWrappedModel() : model;
		return this;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return this.baseUnbaked.getDependencies();
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
	
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {
		this.baseUnbaked.resolveParents(modelLoader);
	}
	
	@Override
	public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
		return this.wrap(this.baseUnbaked.bake(baker, textureGetter, rotationContainer));
	}
	
}
