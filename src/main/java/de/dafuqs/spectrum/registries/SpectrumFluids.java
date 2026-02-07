package de.dafuqs.spectrum.registries;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.util.function.*;

public class SpectrumFluids {
	
	// TODO: add sensible FluidType.Properties for each fluid type
	// TODO: can ItemColors.FLUID_COLORS be moved to use FluidType instead of Fluid?
	
	private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTRAR = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, SpectrumCommon.MOD_ID);
	private static final DeferredRegister<Fluid> FLUID_REGISTRAR = DeferredRegister.create(Registries.FLUID, SpectrumCommon.MOD_ID);
	
	// LIQUID CRYSTAL
	public static final DeferredHolder<FluidType, FluidType> LIQUID_CRYSTAL_TYPE = registerFluidType("liquid_crystal",
			() -> new FluidType(FluidType.Properties.create().canExtinguish(true).supportsBoating(true).canHydrate(true)));
	public static final DeferredHolder<Fluid, SpectrumFluid> LIQUID_CRYSTAL = registerFluid("liquid_crystal", LiquidCrystalFluid.Still::new, InkColors.LIGHT_GRAY);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_LIQUID_CRYSTAL = registerFluid("flowing_liquid_crystal", LiquidCrystalFluid.Flowing::new, InkColors.LIGHT_GRAY);
	public static final int LIQUID_CRYSTAL_TINT = 0xFFcbbbcb;
	public static final Vector3f LIQUID_CRYSTAL_COLOR_VEC = SpectrumColorHelper.colorIntToVec(LIQUID_CRYSTAL_TINT);
	public static final float LIQUID_CRYSTAL_OVERLAY_ALPHA = 0.6F;
	
	// SLUDGE
	public static final DeferredHolder<FluidType, FluidType> SLUDGE_TYPE = registerFluidType("sludge", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> SLUDGE = registerFluid("sludge", SludgeFluid.StillSludge::new, InkColors.BROWN);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_SLUDGE = registerFluid("flowing_sludge", SludgeFluid.FlowingSludge::new, InkColors.BROWN);
	public static final int SLUDGE_TINT = 0xFF4e2e0a;
	public static final Vector3f SLUDGE_COLOR_VEC = SpectrumColorHelper.colorIntToVec(SLUDGE_TINT);
	public static final float SLUDGE_OVERLAY_ALPHA = 0.995F;
	
	// MIDNIGHT SOLUTION
	public static final DeferredHolder<FluidType, FluidType> MIDNIGHT_SOLUTION_TYPE = registerFluidType("midnight_solution", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> MIDNIGHT_SOLUTION = registerFluid("midnight_solution", MidnightSolutionFluid.Still::new, InkColors.LIGHT_GRAY);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_MIDNIGHT_SOLUTION = registerFluid("flowing_midnight_solution", MidnightSolutionFluid.Flowing::new, InkColors.LIGHT_GRAY);
	public static final int MIDNIGHT_SOLUTION_TINT = 0xFF11183b;
	public static final Vector3f MIDNIGHT_SOLUTION_COLOR_VEC = SpectrumColorHelper.colorIntToVec(MIDNIGHT_SOLUTION_TINT);
	public static final float MIDNIGHT_SOLUTION_OVERLAY_ALPHA = 0.995F;
	
	// DRAGONROT
	public static final DeferredHolder<FluidType, FluidType> DRAGONROT_TYPE = registerFluidType("dragonrot", () -> new FluidType(FluidType.Properties.create()));
	public static final DeferredHolder<Fluid, SpectrumFluid> DRAGONROT = registerFluid("dragonrot", DragonrotFluid.Still::new, InkColors.GRAY);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_DRAGONROT = registerFluid("flowing_dragonrot", DragonrotFluid.Flowing::new, InkColors.GRAY);
	public static final int DRAGONROT_TINT = 0xFFe3772f;
	public static final Vector3f DRAGONROT_COLOR_VEC = SpectrumColorHelper.colorIntToVec(DRAGONROT_TINT);
	public static final float DRAGONROT_OVERLAY_ALPHA = 0.98F;
	
	public static void register(IEventBus eventBus) {
		FLUID_REGISTRAR.register(eventBus);
		FLUID_TYPE_REGISTRAR.register(eventBus);
	}
	
	private static DeferredHolder<FluidType, FluidType> registerFluidType(String name, Supplier<FluidType> supplier) {
		return FLUID_TYPE_REGISTRAR.register(name, supplier);
	}
	
	private static DeferredHolder<Fluid, SpectrumFluid> registerFluid(String name, Supplier<SpectrumFluid> supplier, InkColor color) {
		DeferredHolder<Fluid, SpectrumFluid> fluid = FLUID_REGISTRAR.register(name, supplier);
		ItemColors.FLUID_COLORS.registerColorMapping(supplier.get(), color);
		return fluid;
	}
	
	public static void registerClient(RegisterClientExtensionsEvent event) {
		setupFluidRendering(event, LIQUID_CRYSTAL_TYPE.get(), "liquid_crystal", LIQUID_CRYSTAL_TINT, LIQUID_CRYSTAL_OVERLAY_ALPHA);
		setupFluidRendering(event, SLUDGE_TYPE.get(), "sludge", SLUDGE_TINT, SLUDGE_OVERLAY_ALPHA);
		setupFluidRendering(event, MIDNIGHT_SOLUTION_TYPE.get(), "midnight_solution", MIDNIGHT_SOLUTION_TINT, MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
		setupFluidRendering(event, DRAGONROT_TYPE.get(), "dragonrot", DRAGONROT_TINT, DRAGONROT_OVERLAY_ALPHA);
	}
	
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
