package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.client.render.fluid.v1.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.material.*;
import org.joml.*;


public class SpectrumFluids {
	
	// RenderHandler storage for compatibility purposes
	public static final Object2ObjectArrayMap<FluidRenderHandler, Fluid[]> HANDLER_MAP = new Object2ObjectArrayMap<>(4);
	
	// LIQUID CRYSTAL
	public static final SpectrumFluid LIQUID_CRYSTAL = new LiquidCrystalFluid.Still();
	public static final SpectrumFluid FLOWING_LIQUID_CRYSTAL = new LiquidCrystalFluid.Flowing();
	public static final int LIQUID_CRYSTAL_TINT = 0xcbbbcb;
	public static final Vector3f LIQUID_CRYSTAL_COLOR_VEC = SpectrumColorHelper.colorIntToVec(LIQUID_CRYSTAL_TINT);
	public static final ResourceLocation LIQUID_CRYSTAL_OVERLAY_TEXTURE = SpectrumCommon.locate("textures/misc/liquid_crystal_overlay.png");
	public static final float LIQUID_CRYSTAL_OVERLAY_ALPHA = 0.6F;
	
	// SLUDGE
	public static final SpectrumFluid SLUDGE = new SludgeFluid.StillSludge();
	public static final SpectrumFluid FLOWING_SLUDGE = new SludgeFluid.FlowingSludge();
	public static final int SLUDGE_TINT = 0x4e2e0a;
	public static final Vector3f SLUDGE_COLOR_VEC = SpectrumColorHelper.colorIntToVec(SLUDGE_TINT);
	public static final ResourceLocation SLUDGE_OVERLAY_TEXTURE = SpectrumCommon.locate("textures/misc/sludge_overlay.png");
	public static final float SLUDGE_OVERLAY_ALPHA = 0.995F;
	
	// MIDNIGHT SOLUTION
	public static final SpectrumFluid MIDNIGHT_SOLUTION = new MidnightSolutionFluid.Still();
	public static final SpectrumFluid FLOWING_MIDNIGHT_SOLUTION = new MidnightSolutionFluid.Flowing();
	public static final int MIDNIGHT_SOLUTION_TINT = 0x11183b;
	public static final Vector3f MIDNIGHT_SOLUTION_COLOR_VEC = SpectrumColorHelper.colorIntToVec(MIDNIGHT_SOLUTION_TINT);
	public static final ResourceLocation MIDNIGHT_SOLUTION_OVERLAY_TEXTURE = SpectrumCommon.locate("textures/misc/midnight_solution_overlay.png");
	public static final float MIDNIGHT_SOLUTION_OVERLAY_ALPHA = 0.995F;
	
	// DRAGONROT
	public static final SpectrumFluid DRAGONROT = new DragonrotFluid.Still();
	public static final SpectrumFluid FLOWING_DRAGONROT = new DragonrotFluid.Flowing();
	public static final int DRAGONROT_TINT = 0xe3772f;
	public static final Vector3f DRAGONROT_COLOR_VEC = SpectrumColorHelper.colorIntToVec(DRAGONROT_TINT);
	public static final ResourceLocation DRAGONROT_OVERLAY_TEXTURE = SpectrumCommon.locate("textures/misc/dragonrot_overlay.png");
	public static final float DRAGONROT_OVERLAY_ALPHA = 0.98F;
	
	public static void register() {
		registerFluid("liquid_crystal", LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL, InkColors.LIGHT_GRAY);
		registerFluid("sludge", SLUDGE, FLOWING_SLUDGE, InkColors.BROWN);
		registerFluid("midnight_solution", MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION, InkColors.GRAY);
		registerFluid("dragonrot", DRAGONROT, FLOWING_DRAGONROT, InkColors.GRAY);
	}
	
	private static void registerFluid(String name, Fluid stillFluid, Fluid flowingFluid, InkColor color) {
		Registry.register(BuiltInRegistries.FLUID, SpectrumCommon.locate(name), stillFluid);
		Registry.register(BuiltInRegistries.FLUID, SpectrumCommon.locate("flowing_" + name), flowingFluid);
		ItemColors.FLUID_COLORS.registerColorMapping(stillFluid, color);
		ItemColors.FLUID_COLORS.registerColorMapping(flowingFluid, color);
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		setupFluidRendering(LIQUID_CRYSTAL, FLOWING_LIQUID_CRYSTAL, "liquid_crystal", LIQUID_CRYSTAL_TINT);
		setupFluidRendering(SLUDGE, FLOWING_SLUDGE, "sludge", SLUDGE_TINT);
		setupFluidRendering(MIDNIGHT_SOLUTION, FLOWING_MIDNIGHT_SOLUTION, "midnight_solution", MIDNIGHT_SOLUTION_TINT);
		setupFluidRendering(DRAGONROT, FLOWING_DRAGONROT, "dragonrot", DRAGONROT_TINT);
	}

	@Environment(EnvType.CLIENT)
	private static void setupFluidRendering(final Fluid stillFluid, final Fluid flowingFluid, final String name, int tint) {
		var handler = new SimpleFluidRenderHandler(
				SpectrumCommon.locate("block/" + name + "_still"),
				SpectrumCommon.locate("block/" + name + "_flow"),
				tint
		);
		
		HANDLER_MAP.put(handler, new Fluid[]{stillFluid, flowingFluid});
		FluidRenderHandlerRegistry.INSTANCE.register(stillFluid, flowingFluid, handler);
		
		BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), stillFluid, flowingFluid);
	}
	
}
