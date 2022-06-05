package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fluid.LiquidCrystalFluid;
import de.dafuqs.spectrum.blocks.fluid.MidnightSolutionFluid;
import de.dafuqs.spectrum.blocks.fluid.MudFluid;
import de.dafuqs.spectrum.registries.color.ItemColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.function.Function;

public class SpectrumFluids {
	
	// LIQUID CRYSTAL
	public static final FlowableFluid LIQUID_CRYSTAL = new LiquidCrystalFluid.StillLiquidCrystal();
	public static final FlowableFluid FLOWING_LIQUID_CRYSTAL = new LiquidCrystalFluid.FlowingLiquidCrystal();
	// MUD
	public static final FlowableFluid MUD = new MudFluid.StillMud();
	public static final FlowableFluid FLOWING_MUD = new MudFluid.FlowingMud();
	// MIDNIGHT SOLUTION
	public static final FlowableFluid MIDNIGHT_SOLUTION = new MidnightSolutionFluid.StillMidnightSolution();
	public static final FlowableFluid FLOWING_MIDNIGHT_SOLUTION = new MidnightSolutionFluid.FlowingMidnightSolution();
	
	private static void registerFluid(String name, Fluid fluid, DyeColor dyeColor) {
		Registry.register(Registry.FLUID, new Identifier(SpectrumCommon.MOD_ID, name), fluid);
		ItemColors.FLUID_COLORS.registerColorMapping(fluid, dyeColor);
	}
	
	public static void register() {
		registerFluid("liquid_crystal", LIQUID_CRYSTAL, DyeColor.LIGHT_GRAY);
		registerFluid("flowing_liquid_crystal", FLOWING_LIQUID_CRYSTAL, DyeColor.LIGHT_GRAY);
		registerFluid("mud", MUD, DyeColor.BROWN);
		registerFluid("flowing_mud", FLOWING_MUD, DyeColor.BROWN);
		registerFluid("midnight_solution", MIDNIGHT_SOLUTION, DyeColor.GRAY);
		registerFluid("flowing_midnight_solution", FLOWING_MIDNIGHT_SOLUTION, DyeColor.GRAY);
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		setupFluidRendering(LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL, new Identifier(SpectrumCommon.MOD_ID, "liquid_crystal"), 0xcbbbcb);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL);
		
		setupFluidRendering(MUD, FLOWING_MUD, new Identifier(SpectrumCommon.MOD_ID, "mud"), 0x4e2e0a);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), MUD, FLOWING_MUD);
		
		setupFluidRendering(MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION, new Identifier(SpectrumCommon.MOD_ID, "midnight_solution"), 0x11183b);
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION);
	}
	
	@Environment(EnvType.CLIENT)
	private static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color) {
		final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
		final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");
		
		// If they're not already present, add the sprites to the block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});
		
		final Identifier fluidId = Registry.FLUID.getId(still);
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
