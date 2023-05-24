package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.color.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.client.render.fluid.v1.*;
import net.fabricmc.fabric.api.event.client.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.*;
import net.minecraft.fluid.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.joml.Vector3f;

import java.util.function.*;

public class SpectrumFluids {
	
	// LIQUID CRYSTAL
	public static final FlowableFluid LIQUID_CRYSTAL = new LiquidCrystalFluid.StillLiquidCrystal();
	public static final FlowableFluid FLOWING_LIQUID_CRYSTAL = new LiquidCrystalFluid.FlowingLiquidCrystal();
	public static final int LIQUID_CRYSTAL_COLOR = 0xcbbbcb;
	public static final Vector3f LIQUID_CRYSTAL_COLOR_VEC = new Vector3f(0.7f, 0.67f, 0.81f);
	public static final Identifier LIQUID_CRYSTAL_OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/liquid_crystal_overlay.png");
	public static final float LIQUID_CRYSTAL_OVERLAY_ALPHA = 0.6F;
	
	// MUD
	public static final FlowableFluid MUD = new MudFluid.StillMud();
	public static final FlowableFluid FLOWING_MUD = new MudFluid.FlowingMud();
	public static final int MUD_COLOR = 0x4e2e0a;
	public static final Vector3f MUD_COLOR_VEC = new Vector3f(0.26f, 0.14f, 0.01f);
	public static final Identifier MUD_OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/mud_overlay.png");
	public static final float MUD_OVERLAY_ALPHA = 0.995F;
	
	// MIDNIGHT SOLUTION
	public static final FlowableFluid MIDNIGHT_SOLUTION = new MidnightSolutionFluid.StillMidnightSolution();
	public static final FlowableFluid FLOWING_MIDNIGHT_SOLUTION = new MidnightSolutionFluid.FlowingMidnightSolution();
	public static final int MIDNIGHT_SOLUTION_COLOR = 0x11183b;
	public static final Vector3f MIDNIGHT_SOLUTION_COLOR_VEC = new Vector3f(0.07f, 0.07f, 0.2f);
	public static final Identifier MIDNIGHT_SOLUTION_OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/midnight_solution_overlay.png");
	public static final float MIDNIGHT_SOLUTION_OVERLAY_ALPHA = 0.995F;
	
	// DRAGONROT
	public static final FlowableFluid DRAGONROT = new DragonrotFluid.StillDragonrot();
	public static final FlowableFluid FLOWING_DRAGONROT = new DragonrotFluid.FlowingDragonrot();
	public static final int DRAGONROT_COLOR = 0xe3772f;
	public static final Vector3f DRAGONROT_COLOR_VEC = ColorHelper.colorIntToVec(DRAGONROT_COLOR);
	public static final Identifier DRAGONROT_OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/dragonrot_overlay.png");
	public static final float DRAGONROT_OVERLAY_ALPHA = 0.98F;
	
	private static void registerFluid(String name, Fluid fluid, DyeColor dyeColor) {
		Registry.register(Registries.FLUID, SpectrumCommon.locate(name), fluid);
		ItemColors.FLUID_COLORS.registerColorMapping(fluid, dyeColor);
	}
	
	public static void register() {
		registerFluid("liquid_crystal", LIQUID_CRYSTAL, DyeColor.LIGHT_GRAY);
		registerFluid("flowing_liquid_crystal", FLOWING_LIQUID_CRYSTAL, DyeColor.LIGHT_GRAY);
		registerFluid("mud", MUD, DyeColor.BROWN);
		registerFluid("flowing_mud", FLOWING_MUD, DyeColor.BROWN);
		registerFluid("midnight_solution", MIDNIGHT_SOLUTION, DyeColor.GRAY);
		registerFluid("flowing_midnight_solution", FLOWING_MIDNIGHT_SOLUTION, DyeColor.GRAY);
		registerFluid("dragonrot", DRAGONROT, DyeColor.PURPLE);
		registerFluid("flowing_dragonrot", FLOWING_DRAGONROT, DyeColor.PURPLE);
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		setupFluidRendering(LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL, "liquid_crystal", LIQUID_CRYSTAL_COLOR);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL);
		
		setupFluidRendering(MUD, FLOWING_MUD, "mud", MUD_COLOR);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), MUD, FLOWING_MUD);
		
		setupFluidRendering(MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION, "midnight_solution", MIDNIGHT_SOLUTION_COLOR);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION);
		
		setupFluidRendering(DRAGONROT, FLOWING_DRAGONROT, "dragonrot", DRAGONROT_COLOR);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), DRAGONROT, FLOWING_DRAGONROT);
	}
	
	@Environment(EnvType.CLIENT)
	private static void setupFluidRendering(final Fluid still, final Fluid flowing, final String textureFluidId, final int color) {
		final Identifier stillSpriteId = SpectrumCommon.locate("block/" + textureFluidId + "_still");
		final Identifier flowingSpriteId = SpectrumCommon.locate("block/" + textureFluidId + "_flow");
		
		// If they're not already present, add the sprites to the block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});
		
		final Identifier fluidId = Registries.FLUID.getId(still);
		final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
		final Sprite[] fluidSprites = {null, null};
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			
			/**
			 * Get the sprites from the block atlas when resources are reloaded
			 */
			@Override
			public void reload(ResourceManager manager) {
				final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
				fluidSprites[0] = atlas.apply(stillSpriteId);
				fluidSprites[1] = atlas.apply(flowingSpriteId);
			}
			
			@Override
			public Identifier getFabricId() {
				return listenerId;
			}
		});
		
		// The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
		final FluidRenderHandler renderHandler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return fluidSprites;
			}
			
			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				return color;
			}
		};
		
		FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
	}
	
}
