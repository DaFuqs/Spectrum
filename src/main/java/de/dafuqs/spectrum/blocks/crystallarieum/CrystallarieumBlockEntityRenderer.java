package de.dafuqs.spectrum.blocks.crystallarieum;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.render.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.client.textures.*;
import net.neoforged.neoforge.fluids.*;
import org.jetbrains.annotations.*;


public class CrystallarieumBlockEntityRenderer<T extends CrystallarieumBlockEntity> implements BlockEntityRenderer<T> {
	
	private static final Material SPRITE = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/crystallarieum"));
	
	private final ModelPart active;
	private final ModelPart inactive;
	private final ModelPart halo;
	private final ModelPart echo;
	private final ModelPart upperecho;
	
	@SuppressWarnings("unused")
	public CrystallarieumBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		var root = getTexturedModelData().bakeRoot();
		this.active = root.getChild("active");
		this.inactive = root.getChild("inactive");
		this.halo = root.getChild("halo");
		this.echo = halo.getChild("echo");
		this.upperecho = echo.getChild("upperecho");
	}
	
	@Override
	public void render(CrystallarieumBlockEntity crystal, float tickDelta, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
		if (crystal.animator == null)
			return;
		
		crystal.animator.animate(tickDelta, 0);
		
		var vertices = SPRITE.buffer(vertexConsumers, RenderType::entityTranslucent);
		
		renderHalo(crystal, tickDelta, matrices, vertexConsumers, light, overlay, vertices);
		
		FluidStack fluid = crystal.fluidStorage.getFluid();
		if (!fluid.isEmpty()) {
			
			matrices.pushPose();
			IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid());
			TextureAtlasSprite sprite = FluidSpriteCache.getSprite(fluidTypeExtensions.getStillTexture(fluid));
			
			BlockPos pos = crystal.getBlockPos().above();
			int luminance = fluid.getFluidType().getLightLevel(fluid);
			int skylight = crystal.getLevel().getBrightness(LightLayer.BLOCK, pos);
			int glow = LightTexture.pack(Math.max(luminance, skylight), crystal.getLevel().getBrightness(LightLayer.SKY, pos));
			
			boolean full = crystal.fluidStorage.getFluidAmount() == crystal.fluidStorage.getCapacity();
			float y = full ? 0.975F : 0.94F;
			int rim = full ? 1 : 2;
			
			int[] colors = FluidRendering.unpackColor(fluidTypeExtensions.getTintColor(fluid.getFluid().defaultFluidState(), crystal.getLevel(), crystal.getBlockPos()));
			FluidRendering.renderFluid(vertexConsumers.getBuffer(RenderType.translucent()), matrices.last().pose(), sprite, glow, overlay, rim, 16 - rim, y, rim, 16 - rim, colors);
			
			matrices.popPose();
			
		}
		
		ItemStack catalystStack = crystal.getItem(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if (catalystStack.isEmpty())
			return;
		
		renderCatalysts(crystal, matrices, vertexConsumers, light, overlay, catalystStack);
	}
	
	private void renderHalo(CrystallarieumBlockEntity crystal, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, VertexConsumer vertices) {
		matrices.pushPose();
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		
		var ink = InkColors.WHITE;
		var time = crystal.getLevel() != null ? crystal.getLevel().getGameTime() % 1000000 : 0;
		var bounce = (Math.sin((time + tickDelta) / 23) + 1) * crystal._bounce.get() / 2F;
		
		if (crystal.currentRecipe != null) {
			ink = crystal.currentRecipe.value().getInkColor();
			active.render(matrices, vertices, LightTexture.FULL_BRIGHT, overlay, ink.getColorInt());
		} else {
			inactive.render(matrices, vertices, light, overlay);
		}
		
		crystal.rotation += crystal._speed.get();
		matrices.mulPose(Axis.YP.rotationDegrees(crystal.rotation));
		var argb = FastColor.ARGB32.color(Math.round(crystal._alpha.get() * 255), ink.getColorInt());
		halo.y = (float) (-9 - bounce);
		matrices.mulPose(Axis.YP.rotationDegrees(-crystal.rotation * 2));
		echo.y = (float) (0.5 - (bounce / 3));
		matrices.mulPose(Axis.YP.rotationDegrees(crystal.rotation * 1.5F));
		upperecho.y = (float) (-0.5 - (bounce / 3));
		halo.render(matrices, vertices, LightTexture.FULL_BRIGHT, overlay, argb);
		
		//Fairly lazily placed ink storage. Due to be removed once ink update happens
		ItemStack inkStorageStack = crystal.getItem(CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID);
		if (!inkStorageStack.isEmpty()) {
			matrices.scale(0.65F, 0.65F, 0.65F);
			matrices.mulPose(Axis.XP.rotationDegrees(180));
			matrices.translate(0, 0.975 + bounce / 3, 0);
			Minecraft.getInstance().getItemRenderer().renderStatic(inkStorageStack, ItemDisplayContext.GROUND, light, overlay, matrices, vertexConsumers, crystal.getLevel(), 0);
			
		}
		
		matrices.popPose();
	}
	
	private static void renderCatalysts(CrystallarieumBlockEntity crystal, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, ItemStack catalystStack) {
		int renderedStackCount = (int) Math.ceil(catalystStack.getCount() / 17.0);
		
		matrices.pushPose();
		matrices.translate(0.5, 0.4, 0.5);
		matrices.mulPose(Axis.XP.rotationDegrees(270));
		matrices.mulPose(Axis.YP.rotationDegrees(180));
		matrices.mulPose(Axis.ZP.rotationDegrees(70));
		
		if (renderedStackCount == 1) {
			Minecraft.getInstance().getItemRenderer().renderStatic(catalystStack, ItemDisplayContext.GROUND, light, overlay, matrices, vertexConsumers, crystal.getLevel(), 0);
			matrices.popPose();
			return;
		}
		
		for (int i = 0; i < renderedStackCount; i++) {
			Minecraft.getInstance().getItemRenderer().renderStatic(catalystStack, ItemDisplayContext.GROUND, light, overlay, matrices, vertexConsumers, crystal.getLevel(), 0);
			matrices.translate(0, 0, -0.0225);
			matrices.mulPose(Axis.ZP.rotationDegrees(53));
		}
		
		matrices.popPose();
	}
	
	@Override
	public int getViewDistance() {
		return 48;
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition active = modelPartData.addOrReplaceChild("active", CubeListBuilder.create().texOffs(40, 34).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));
		
		PartDefinition inactive = modelPartData.addOrReplaceChild("inactive", CubeListBuilder.create().texOffs(80, 34).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));
		
		PartDefinition halo = modelPartData.addOrReplaceChild("halo", CubeListBuilder.create().texOffs(77, 48).addBox(-8.5F, 1.0F, -8.5F, 17.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));
		
		PartDefinition diamond_r1 = halo.addOrReplaceChild("diamond_r1", CubeListBuilder.create().texOffs(80, 65).addBox(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition echo = halo.addOrReplaceChild("echo", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));
		
		PartDefinition echoring_r1 = echo.addOrReplaceChild("echoring_r1", CubeListBuilder.create().texOffs(80, 80).addBox(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition upperecho = echo.addOrReplaceChild("upperecho", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
		
		PartDefinition echoring_r2 = upperecho.addOrReplaceChild("echoring_r2", CubeListBuilder.create().texOffs(80, 95).addBox(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
}
