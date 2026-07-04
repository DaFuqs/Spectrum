package de.dafuqs.spectrum.registries;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.registries.*;
import org.joml.*;

import java.util.function.*;

public class SpectrumFluids {
	
	private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTRAR = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, SpectrumCommon.MOD_ID);
	private static final DeferredRegister<Fluid> FLUID_REGISTRAR = DeferredRegister.create(Registries.FLUID, SpectrumCommon.MOD_ID);
	
	// LIQUID CRYSTAL
	public static final int LIQUID_CRYSTAL_LIGHT_LEVEL = 11;
	public static final DeferredHolder<FluidType, FluidType> LIQUID_CRYSTAL_TYPE = registerFluidType("liquid_crystal", () -> new SpectrumFluidType(true, SpectrumGameRules.RULE_LIQUID_CRYSTAL_SOURCE_CONVERSION, FluidType.Properties.create()
			.descriptionId("block.spectrum.liquid_crystal")
			.fallDistanceModifier(0F)
			.canDrown(true).canExtinguish(true).supportsBoating(true).canHydrate(true).lightLevel(LIQUID_CRYSTAL_LIGHT_LEVEL)
			.pathType(PathType.WATER).adjacentPathType(PathType.WATER_BORDER)
			.density(800).viscosity(1000).temperature(200)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.addDripstoneDripping(SpectrumConfig.CONFIG.LiquidCrystalDripstoneDripChance.get().floatValue(), SpectrumParticleTypes.DRIPPING_LIQUID_CRYSTAL, SpectrumBlocks.LIQUID_CRYSTAL_CAULDRON.get(), SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
	});
	public static final DeferredHolder<Fluid, SpectrumFluid> LIQUID_CRYSTAL = registerFluid("liquid_crystal", LiquidCrystalFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_LIQUID_CRYSTAL = registerFluid("flowing_liquid_crystal", LiquidCrystalFluid.Flowing::new);
	public static final int LIQUID_CRYSTAL_COLOR = 0xFFcbbbcb;
	public static final Vector3f LIQUID_CRYSTAL_COLOR_VEC = SpectrumColorHelper.colorIntToVec(LIQUID_CRYSTAL_COLOR);
	public static final float LIQUID_CRYSTAL_OVERLAY_ALPHA = 0.6F;
	
	// SLUDGE
	public static final int SLUDGE_LIGHT_LEVEL = 0;
	public static final DeferredHolder<FluidType, FluidType> SLUDGE_TYPE = registerFluidType("sludge", () -> new SpectrumFluidType(true, SpectrumGameRules.RULE_SLUDGE_SOURCE_CONVERSION, FluidType.Properties.create()
			.descriptionId("block.spectrum.sludge")
			.fallDistanceModifier(0F)
			.canDrown(true).canExtinguish(true).supportsBoating(false).canHydrate(false).lightLevel(SLUDGE_LIGHT_LEVEL)
			.pathType(PathType.WATER).adjacentPathType(PathType.WATER_BORDER)
			.density(5000).viscosity(8000).temperature(350)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.addDripstoneDripping(SpectrumConfig.CONFIG.SludgeDripstoneDripChance.get().floatValue(), SpectrumParticleTypes.DRIPPING_SLUDGE, SpectrumBlocks.SLUDGE_CAULDRON.get(), SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
	});
	public static final DeferredHolder<Fluid, SpectrumFluid> SLUDGE = registerFluid("sludge", SludgeFluid.StillSludge::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_SLUDGE = registerFluid("flowing_sludge", SludgeFluid.FlowingSludge::new);
	public static final int SLUDGE_COLOR = 0xFF4e2e0a;
	public static final Vector3f SLUDGE_COLOR_VEC = SpectrumColorHelper.colorIntToVec(SLUDGE_COLOR);
	public static final float SLUDGE_OVERLAY_ALPHA = 1.0F;
	
	// MIDNIGHT SOLUTION
	public static final int MIDNIGHT_SOLUTION_LIGHT_LEVEL = 0;
	public static final DeferredHolder<FluidType, FluidType> MIDNIGHT_SOLUTION_TYPE = registerFluidType("midnight_solution", () -> new SpectrumFluidType(true, SpectrumGameRules.RULE_MIDNIGHT_SOLUTION_SOURCE_CONVERSION,
			FluidType.Properties.create()
			.descriptionId("block.spectrum.midnight_solution")
			.fallDistanceModifier(0F)
			.canDrown(true).canExtinguish(true).supportsBoating(true).canHydrate(false).lightLevel(MIDNIGHT_SOLUTION_LIGHT_LEVEL)
			.pathType(PathType.LAVA).adjacentPathType(null)
			.density(1500).viscosity(2000).temperature(-100)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.addDripstoneDripping(SpectrumConfig.CONFIG.MidnightSolutionDripstoneDripChance.get().floatValue(), SpectrumParticleTypes.DRIPPING_MIDNIGHT_SOLUTION, SpectrumBlocks.MIDNIGHT_SOLUTION_CAULDRON.get(), SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
	});
	public static final DeferredHolder<Fluid, SpectrumFluid> MIDNIGHT_SOLUTION = registerFluid("midnight_solution", MidnightSolutionFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_MIDNIGHT_SOLUTION = registerFluid("flowing_midnight_solution", MidnightSolutionFluid.Flowing::new);
	public static final int MIDNIGHT_SOLUTION_COLOR = 0xFF11183b;
	public static final Vector3f MIDNIGHT_SOLUTION_COLOR_VEC = SpectrumColorHelper.colorIntToVec(MIDNIGHT_SOLUTION_COLOR);
	public static final float MIDNIGHT_SOLUTION_OVERLAY_ALPHA = 1.0F;
	
	// DRAGONROT
	public static final int DRAGONROT_LIGHT_LEVEL = 15;
	public static final DeferredHolder<FluidType, FluidType> DRAGONROT_TYPE = registerFluidType("dragonrot", () -> new SpectrumFluidType(false, SpectrumGameRules.RULE_DRAGONROT_SOURCE_CONVERSION, FluidType.Properties.create()
			.descriptionId("block.spectrum.dragonrot")
			.fallDistanceModifier(0F)
			.canDrown(true).canExtinguish(true).supportsBoating(true).canHydrate(false).lightLevel(DRAGONROT_LIGHT_LEVEL)
			.pathType(PathType.LAVA).adjacentPathType(null)
			.density(2000).viscosity(3000).temperature(650)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.addDripstoneDripping(SpectrumConfig.CONFIG.DragonrotDripstoneDripChance.get().floatValue(), SpectrumParticleTypes.DRIPPING_DRAGONROT, SpectrumBlocks.DRAGONROT_CAULDRON.get(), SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
	});
	public static final DeferredHolder<Fluid, SpectrumFluid> DRAGONROT = registerFluid("dragonrot", DragonrotFluid.Still::new);
	public static final DeferredHolder<Fluid, SpectrumFluid> FLOWING_DRAGONROT = registerFluid("flowing_dragonrot", DragonrotFluid.Flowing::new);
	public static final int DRAGONROT_COLOR = 0xFFe3772f;
	public static final Vector3f DRAGONROT_COLOR_VEC = SpectrumColorHelper.colorIntToVec(DRAGONROT_COLOR);
	public static final float DRAGONROT_OVERLAY_ALPHA = 1.0F;
	
	public static void register(IEventBus eventBus) {
		FLUID_REGISTRAR.register(eventBus);
		FLUID_TYPE_REGISTRAR.register(eventBus);
	}
	
	private static DeferredHolder<FluidType, FluidType> registerFluidType(String name, Supplier<FluidType> supplier) {
		return FLUID_TYPE_REGISTRAR.register(name, supplier);
	}
	
	private static DeferredHolder<Fluid, SpectrumFluid> registerFluid(String name, Supplier<SpectrumFluid> supplier) {
		return FLUID_REGISTRAR.register(name, supplier);
	}
	
	public static void registerClient(RegisterClientExtensionsEvent event) {
		setupFluidRendering(event, LIQUID_CRYSTAL_TYPE.get(), "liquid_crystal", LIQUID_CRYSTAL_OVERLAY_ALPHA);
		setupFluidRendering(event, SLUDGE_TYPE.get(), "sludge", SLUDGE_OVERLAY_ALPHA);
		setupFluidRendering(event, MIDNIGHT_SOLUTION_TYPE.get(), "midnight_solution", MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
		setupFluidRendering(event, DRAGONROT_TYPE.get(), "dragonrot", DRAGONROT_OVERLAY_ALPHA);
		
		ItemBlockRenderTypes.setRenderLayer(LIQUID_CRYSTAL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FLOWING_LIQUID_CRYSTAL.get(), RenderType.translucent());
	}
	
	private static void setupFluidRendering(RegisterClientExtensionsEvent event, final FluidType fluidType, final String name, float overlayAlpha) {
		ResourceLocation overlay = SpectrumCommon.locate("textures/misc/" + name + "_overlay.png");
		ResourceLocation still = SpectrumCommon.locate("block/" + name + "_still");
		ResourceLocation flowing = SpectrumCommon.locate("block/" + name + "_flow");
		
		
		event.registerFluidType(new IClientFluidTypeExtensions() {
			@Override
			public ResourceLocation getStillTexture() {
				return still;
			}
			
			@Override
			public ResourceLocation getFlowingTexture() {
				return flowing;
			}
			
			@Override
			public void renderOverlay(Minecraft mc, PoseStack stack) {
				renderFluidOverlay(mc, stack, overlay, overlayAlpha);
			}
			
			@Override
			public int getTintColor() {
				return -1;
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
