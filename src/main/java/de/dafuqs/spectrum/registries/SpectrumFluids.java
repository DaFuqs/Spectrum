package de.dafuqs.spectrum.registries;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.material.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public class SpectrumFluids {
	
	// RenderHandler storage for compatibility purposes
	private static final DeferredRegister<Fluid> FLUID_REGISTRAR = DeferredRegister.create(Registries.FLUID, SpectrumCommon.MOD_ID);
	private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTRAR = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, SpectrumCommon.MOD_ID);
	
	
	// LIQUID CRYSTAL
	public static final DeferredHolder<FluidType, FluidType> LIQUID_CRYSTAL_TYPE = FLUID_TYPE_REGISTRAR.register("liquid_crystal", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> LIQUID_CRYSTAL = FLUID_REGISTRAR.register("liquid_crystal", LiquidCrystalFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_LIQUID_CRYSTAL = FLUID_REGISTRAR.register("flowing_liquid_crystal", LiquidCrystalFluid.Flowing::new);
	public static final int LIQUID_CRYSTAL_TINT = 0xFFcbbbcb;
	public static final Vector3f LIQUID_CRYSTAL_COLOR_VEC = SpectrumColorHelper.colorIntToVec(LIQUID_CRYSTAL_TINT);
	public static final float LIQUID_CRYSTAL_OVERLAY_ALPHA = 0.6F;
	
	// SLUDGE
	public static final DeferredHolder<FluidType, FluidType> SLUDGE_TYPE = FLUID_TYPE_REGISTRAR.register("sludge", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> SLUDGE = FLUID_REGISTRAR.register("sludge", SludgeFluid.StillSludge::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_SLUDGE = FLUID_REGISTRAR.register("flowing_sludge", SludgeFluid.FlowingSludge::new);
	public static final int SLUDGE_TINT = 0xFF4e2e0a;
	public static final Vector3f SLUDGE_COLOR_VEC = SpectrumColorHelper.colorIntToVec(SLUDGE_TINT);
	public static final float SLUDGE_OVERLAY_ALPHA = 0.995F;
	
	// MIDNIGHT SOLUTION
	public static final DeferredHolder<FluidType, FluidType> MIDNIGHT_SOLUTION_TYPE = FLUID_TYPE_REGISTRAR.register("midnight_solution", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> MIDNIGHT_SOLUTION = FLUID_REGISTRAR.register("midnight_solution", MidnightSolutionFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_MIDNIGHT_SOLUTION = FLUID_REGISTRAR.register("flowing_midnight_solution", MidnightSolutionFluid.Flowing::new);
	public static final int MIDNIGHT_SOLUTION_TINT = 0xFF11183b;
	public static final Vector3f MIDNIGHT_SOLUTION_COLOR_VEC = SpectrumColorHelper.colorIntToVec(MIDNIGHT_SOLUTION_TINT);
	public static final float MIDNIGHT_SOLUTION_OVERLAY_ALPHA = 0.995F;
	
	// DRAGONROT
	public static final DeferredHolder<FluidType, FluidType> DRAGONROT_TYPE = FLUID_TYPE_REGISTRAR.register("dragonrot", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> DRAGONROT = FLUID_REGISTRAR.register("dragonrot", DragonrotFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_DRAGONROT = FLUID_REGISTRAR.register("flowing_dragonrot", DragonrotFluid.Flowing::new);
	public static final int DRAGONROT_TINT = 0xFFe3772f;
	public static final Vector3f DRAGONROT_COLOR_VEC = SpectrumColorHelper.colorIntToVec(DRAGONROT_TINT);
	public static final float DRAGONROT_OVERLAY_ALPHA = 0.98F;
	
	public static void register(IEventBus eventBus) {
		FLUID_REGISTRAR.register(eventBus);
		FLUID_TYPE_REGISTRAR.register(eventBus);
		
		ItemColors.FLUID_COLORS.registerColorMapping(LIQUID_CRYSTAL.get().getSource(), InkColors.LIGHT_GRAY);
		ItemColors.FLUID_COLORS.registerColorMapping(LIQUID_CRYSTAL.get().getFlowing(), InkColors.LIGHT_GRAY);
		ItemColors.FLUID_COLORS.registerColorMapping(SLUDGE.get().getSource(), InkColors.BROWN);
		ItemColors.FLUID_COLORS.registerColorMapping(SLUDGE.get().getFlowing(), InkColors.BROWN);
		ItemColors.FLUID_COLORS.registerColorMapping(MIDNIGHT_SOLUTION.get().getSource(), InkColors.LIGHT_GRAY);
		ItemColors.FLUID_COLORS.registerColorMapping(MIDNIGHT_SOLUTION.get().getFlowing(), InkColors.LIGHT_GRAY);
		ItemColors.FLUID_COLORS.registerColorMapping(DRAGONROT.get().getSource(), InkColors.GRAY);
		ItemColors.FLUID_COLORS.registerColorMapping(DRAGONROT.get().getFlowing(), InkColors.GRAY);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void registerClient(RegisterClientExtensionsEvent event) {
		setupFluidRendering(event, LIQUID_CRYSTAL_TYPE.get(), "liquid_crystal", LIQUID_CRYSTAL_TINT, LIQUID_CRYSTAL_OVERLAY_ALPHA);
		setupFluidRendering(event, SLUDGE_TYPE.get(), "sludge", SLUDGE_TINT, SLUDGE_OVERLAY_ALPHA);
		setupFluidRendering(event, MIDNIGHT_SOLUTION_TYPE.get(), "midnight_solution", MIDNIGHT_SOLUTION_TINT, MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
		setupFluidRendering(event, DRAGONROT_TYPE.get(), "dragonrot", DRAGONROT_TINT, DRAGONROT_OVERLAY_ALPHA);
	}
	
	@OnlyIn(Dist.CLIENT)
	private static void setupFluidRendering(RegisterClientExtensionsEvent event, final FluidType fluidType, final String name, int tint, float overlayAlpha) {
		ResourceLocation overlay = SpectrumCommon.locate("textures/misc/" + name + "_overlay.png");
		ResourceLocation still = SpectrumCommon.locate("block/" + name + "_still");
		ResourceLocation flowing = SpectrumCommon.locate("block/" + name + "_flow");
		
		event.registerFluidType(new IClientFluidTypeExtensions() {
			@Override
			public @NotNull ResourceLocation getStillTexture() {
				return still;
			}
			
			@Override
			public @NotNull ResourceLocation getFlowingTexture() {
				return flowing;
			}
			
			@Override
			public void renderOverlay(@NotNull Minecraft mc, @NotNull PoseStack stack) {
				renderFluidOverlay(mc, stack, overlay, overlayAlpha);
			}
			
			@Override
			public int getTintColor() {
				return tint;
			}
		}, fluidType);
	}
	
	public static void renderFluidOverlay(Minecraft minecraft, PoseStack stack, ResourceLocation texture, float alpha) {
		var player = minecraft.player;
		if (player == null) return;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		BlockPos blockPos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
		float f = LightTexture.getBrightness(player.level().dimensionType(), player.level().getMaxLocalRawBrightness(blockPos));
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(f, f, f, alpha);
		
		float m = -player.getYRot() / 64.0F;
		float n = player.getXRot() / 64.0F;
		Matrix4f matrix4f = stack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(4.0F + m, 4.0F + n);
		bufferBuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F + m, 4.0F + n);
		bufferBuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F + m, 0.0F + n);
		bufferBuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(4.0F + m, 0.0F + n);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
	
}
