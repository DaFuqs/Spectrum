package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class PedestalBlockEntityRenderer<C extends PedestalBlockEntity> implements BlockEntityRenderer<C> {
	
	private final Identifier GROUND_MARK = SpectrumCommon.locate("textures/misc/circle.png");
	private final ModelPart circle;
	
	private static final int RECIPE_RECALCULATION_TICKS = 4;
	private @Nullable Recipe<?> cachedRecipe;
	private long cachedRecipeTime = 0;
	private ItemStack cachedRecipeOutput = ItemStack.EMPTY;
	
	public PedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		super();
		this.circle = getTexturedModelData().createModel().getChild("circle");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild("circle", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 0.1F, 8.0F));
		modelPartData.getChild("circle").addChild("circle2", ModelPartBuilder.create().uv(0, 0).cuboid(-32.0F, 0.0F, -29F, 64.0F, 0.0F, 64.0F), ModelTransform.rotation(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 256, 256);
	}
	
	@Override
	public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		if (entity.getWorld() == null) {
			return;
		}
		
		// render floating item stacks
		Recipe<?> currentRecipe = entity.getCurrentRecipe();
		if (currentRecipe instanceof PedestalRecipe pedestalRecipe) {
			float time = entity.getWorld().getTime() % 50000 + tickDelta;
			this.circle.yaw = time / 25.0F;
			this.circle.render(matrixStack, vertexConsumerProvider.getBuffer(SpectrumRenderLayers.GlowInTheDarkRenderLayer.get(GROUND_MARK)), light, overlay);
			
			long currentTime = entity.getWorld().getTime();
			if (this.cachedRecipeTime + RECIPE_RECALCULATION_TICKS < currentTime || this.cachedRecipe != pedestalRecipe) {
				this.cachedRecipeOutput = pedestalRecipe.craft(entity, entity.getWorld().getRegistryManager());
				this.cachedRecipe = pedestalRecipe;
				this.cachedRecipeTime = currentTime;
			}
			
			matrixStack.push();
			double height = Math.sin((time) / 8.0) / 6.0; // item height
			matrixStack.translate(0.5F, 1.3 + height, 0.5F); // position offset
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((time) * 2)); // item stack rotation
			
			// fixed lighting because:
			// 1. light variable would always be 0 anyway (the pedestal is opaque, making the inside black)
			// 2. the floating item looks like a hologram
			MinecraftClient.getInstance().getItemRenderer().renderItem(this.cachedRecipeOutput, ModelTransformationMode.GROUND, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, matrixStack, vertexConsumerProvider, entity.getWorld(), 0);
			matrixStack.pop();
		}
	}
	
}
