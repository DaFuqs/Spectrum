package de.dafuqs.spectrum.api.render;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

// TODO: use
class ForwardingBakedModel implements BakedModel {
	protected @Nullable BakedModel wrapped;
	
	public BakedModel getWrappedModel() {
		return this.wrapped;
	}
	
	protected void setWrappedModel(BakedModel model) {
		this.wrapped = model;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return this.wrapped.getQuads(state, side, rand);
	}
	
	@Override
	public boolean useAmbientOcclusion() {
		return this.wrapped.useAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d() {
		return this.wrapped.isGui3d();
	}
	
	@Override
	public boolean usesBlockLight() {
		return this.wrapped.usesBlockLight();
	}
	
	@Override
	public boolean isCustomRenderer() {
		return this.wrapped.isCustomRenderer();
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.wrapped.getParticleIcon();
	}
	
	@Override
	public ItemTransforms getTransforms() {
		return this.wrapped.getTransforms();
	}
	
	@Override
	public ItemOverrides getOverrides() {
		return this.wrapped.getOverrides();
	}
}

public class DynamicRenderModel extends ForwardingBakedModel implements UnbakedModel {
	
	public static final ObjectOpenHashSet<ModelResourceLocation> CUSTOM_ITEM_MODELS = new ObjectOpenHashSet<>();
	
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
	
	// only used pre-bake; set to null after bake
	private @Nullable UnbakedModel baseUnbaked;
	
	// pre-bake constructor
	public DynamicRenderModel(UnbakedModel base) {
		this.baseUnbaked = base;
	}
	
	// post-bake / post-override constructor
	public DynamicRenderModel(BakedModel base) {
		this.wrapped = base instanceof DynamicRenderModel fm ? fm.getWrappedModel() : base;
	}
	
	// avoid FAPI builtin model lookup
	@Override
	public boolean isCustomRenderer() {
		// maintain behavior from original
		return false;
	}
	
	private DynamicRenderModel wrap(BakedModel model) {
		this.wrapped = model instanceof DynamicRenderModel fm ? fm.getWrappedModel() : model;
		return this;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		// If we no longer have an unbaked delegate, return empty to avoid NPEs
		return this.baseUnbaked != null ? this.baseUnbaked.getDependencies() : Collections.emptyList();
	}
	
	// override so wrap persists over override - ensures that renderer is called
	@Override
	public ItemOverrides getOverrides() {
		// When used pre-bake `wrapped` may be null — avoid calling super.getOverrides() in that case
		if (this.wrapped == null) {
			// Lightweight safe fallback overrides instance; NeoForge 1.21.1 may or may not expose ItemOverrides.EMPTY
			return new WrappingOverridesList(new ItemOverrides(null, null, List.of()));
		}
		return new WrappingOverridesList(super.getOverrides());
	}
	
	// return empty transform to prevent double apply in render
	@Override
	public ItemTransforms getTransforms() {
		return this.wrapped != null ? this.wrapped.getTransforms() : ItemTransforms.NO_TRANSFORMS;
	}
	
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {
		if (this.baseUnbaked != null) {
			this.baseUnbaked.resolveParents(modelLoader);
		}
	}
	
	@Override
	public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
		// If already baked / no unbaked delegate, return the current wrapped model
		if (this.baseUnbaked == null) {
			return this.wrapped;
		}
		BakedModel baked = this.baseUnbaked.bake(baker, textureGetter, rotationContainer);
		// drop pre-bake reference after bake
		this.baseUnbaked = null;
		return this.wrap(baked);
	}
	
}