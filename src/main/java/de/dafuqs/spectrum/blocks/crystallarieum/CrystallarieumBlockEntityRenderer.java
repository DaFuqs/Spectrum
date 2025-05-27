package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

@Environment(EnvType.CLIENT)
public class CrystallarieumBlockEntityRenderer<T extends CrystallarieumBlockEntity> implements BlockEntityRenderer<T> {
	
	private static final SpriteIdentifier SPRITE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/crystallarieum"));
	
	private final ModelPart active;
	private final ModelPart inactive;
	private final ModelPart halo;
	private final ModelPart echo;
	private final ModelPart upperecho;
	
	@SuppressWarnings("unused")
    public CrystallarieumBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		var root = getTexturedModelData().createModel();
		this.active = root.getChild("active");
		this.inactive = root.getChild("inactive");
		this.halo = root.getChild("halo");
		this.echo = halo.getChild("echo");
		this.upperecho = echo.getChild("upperecho");
	}
	
	@Override
	public void render(CrystallarieumBlockEntity crystal, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (crystal.animator == null)
			return;
		
		crystal.animator.animate(tickDelta, 0);
		
		var vertices = SPRITE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent);
		
		renderHalo(crystal, tickDelta, matrices, vertexConsumers, light, overlay, vertices);
		
		var fluid = crystal.fluidStorage.variant;
		if (!fluid.isBlank()) {
		
			matrices.push();
			Sprite sprite = FluidVariantRendering.getSprite(fluid);
			assert sprite != null;
			
			var pos = crystal.getPos().up();
			var luminance = FluidVariantAttributes.getLuminance(fluid);
			var skylight = crystal.getWorld().getLightLevel(LightType.BLOCK, pos);
			var glow = LightmapTextureManager.pack(Math.max(luminance, skylight), crystal.getWorld().getLightLevel(LightType.SKY, pos));
			
			var full = crystal.fluidStorage.amount == FluidConstants.BUCKET;
			var y = full ? 0.975F : 0.94F;
			var rim = full ? 1 : 2;
			
			int[] colors = FluidRendering.unpackColorOf(fluid, crystal);
			FluidRendering.renderFluid(vertexConsumers.getBuffer(RenderLayer.getTranslucent()), matrices.peek().getPositionMatrix(), sprite, glow, overlay, rim, 16 - rim, y, rim, 16 - rim, colors);
			
			matrices.pop();
			
		}
		
		ItemStack catalystStack = crystal.getStack(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if (catalystStack.isEmpty())
			return;
		
		renderCatalysts(crystal, matrices, vertexConsumers, light, overlay, catalystStack);
	}
	
	private void renderHalo(CrystallarieumBlockEntity crystal, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, VertexConsumer vertices) {
		matrices.push();
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		
		var ink = InkColors.WHITE;
		var time = crystal.getWorld() != null ? crystal.getWorld().getTime() % 1000000 : 0;
		var bounce = (Math.sin((time + tickDelta) / 23) + 1) * crystal._bounce.get() / 2F;
		
		if (crystal.currentRecipe != null) {
			ink = crystal.currentRecipe.value().getInkColor();
			active.render(matrices, vertices, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, ink.getColorInt());
		}
		else {
			inactive.render(matrices, vertices, light, overlay);
		}
		
		crystal.rotation += crystal._speed.get();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(crystal.rotation));
		var argb = ColorHelper.Argb.withAlpha(Math.round(crystal._alpha.get() * 255), ink.getColorInt());
		halo.pivotY = (float) (-9 - bounce);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-crystal.rotation * 2));
		echo.pivotY = (float) (0.5 - (bounce / 3));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(crystal.rotation * 1.5F));
		upperecho.pivotY = (float) (-0.5 - (bounce / 3));
		halo.render(matrices, vertices, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, argb);
		
		//Fairly lazily placed ink storage. Due to be removed once ink update happens
		ItemStack inkStorageStack = crystal.getStack(CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID);
		if (!inkStorageStack.isEmpty()) {
			matrices.scale(0.65F, 0.65F, 0.65F);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.translate(0, 0.975 + bounce / 3, 0);
			MinecraftClient.getInstance().getItemRenderer().renderItem(inkStorageStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, crystal.getWorld(), 0);
			
		}
		
		matrices.pop();
	}
	
	private static void renderCatalysts(CrystallarieumBlockEntity crystal, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ItemStack catalystStack) {
		matrices.push();
		var stack = (int) Math.ceil(catalystStack.getCount() / 17.0);
		matrices.translate(0.5, 0.4, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(70));
		if (stack == 1) {
			MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, crystal.getWorld(), 0);
			matrices.pop();
			return;
		}
		
		for (int i = 0; i < stack; i++) {
			MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, crystal.getWorld(), 0);
			matrices.translate(0, 0, -0.0225);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(53));
		}
		
		matrices.pop();
	}
	
	@Override
	public int getRenderDistance() {
		return 48;
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData active = modelPartData.addChild("active", ModelPartBuilder.create().uv(40, 34).cuboid(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
		
		ModelPartData inactive = modelPartData.addChild("inactive", ModelPartBuilder.create().uv(80, 34).cuboid(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
		
		ModelPartData halo = modelPartData.addChild("halo", ModelPartBuilder.create().uv(77, 48).cuboid(-8.5F, 1.0F, -8.5F, 17.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -9.0F, 0.0F));
		
		ModelPartData diamond_r1 = halo.addChild("diamond_r1", ModelPartBuilder.create().uv(80, 65).cuboid(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		
		ModelPartData echo = halo.addChild("echo", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
		
		ModelPartData echoring_r1 = echo.addChild("echoring_r1", ModelPartBuilder.create().uv(80, 80).cuboid(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		
		ModelPartData upperecho = echo.addChild("upperecho", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.5F, 0.0F));
		
		ModelPartData echoring_r2 = upperecho.addChild("echoring_r2", ModelPartBuilder.create().uv(80, 95).cuboid(-7.5F, 0.0F, -7.5F, 15.0F, 0.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
}
