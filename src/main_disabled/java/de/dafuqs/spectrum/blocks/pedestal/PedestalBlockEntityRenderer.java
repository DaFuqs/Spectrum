package de.dafuqs.spectrum.blocks.pedestal;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class PedestalBlockEntityRenderer<C extends PedestalBlockEntity> implements BlockEntityRenderer<C> {
	
	private final ResourceLocation GROUND_MARK = SpectrumCommon.locate("textures/misc/circle.png");
	private final ModelPart circle;
	
	private static final int RECIPE_RECALCULATION_TICKS = 4;
	private @Nullable Recipe<?> cachedRecipe;
	private long cachedRecipeTime = 0;
	private ItemStack cachedRecipeOutput = ItemStack.EMPTY;
	
	public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		super();
		this.circle = getTexturedModelData().bakeRoot().getChild("circle");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild("circle", CubeListBuilder.create(), PartPose.offset(8.0F, 0.1F, 8.0F));
		modelPartData.getChild("circle").addOrReplaceChild("circle2", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, 0.0F, -29F, 64.0F, 0.0F, 64.0F), PartPose.rotation(0.0F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 256, 256);
	}
	
	@Override
	public void render(PedestalBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		if (entity.getLevel() == null) {
			return;
		}
		
		// render floating item stacks
		Recipe<?> currentRecipe = entity.getCurrentRecipe();
		if (currentRecipe instanceof PedestalRecipe pedestalRecipe) {
			float time = entity.getLevel().getGameTime() % 50000 + tickDelta;
			this.circle.yRot = time / 25.0F;
			this.circle.render(poseStack, vertexConsumerProvider.getBuffer(SpectrumRenderLayers.GlowInTheDarkRenderLayer.get(GROUND_MARK)), light, overlay);
			
			long currentTime = entity.getLevel().getGameTime();
			if (this.cachedRecipeTime + RECIPE_RECALCULATION_TICKS < currentTime || this.cachedRecipe != pedestalRecipe) {
				this.cachedRecipeOutput = pedestalRecipe.assemble(entity.createRecipeInput(), entity.getLevel().registryAccess());
				this.cachedRecipe = pedestalRecipe;
				this.cachedRecipeTime = currentTime;
			}
			
			poseStack.pushPose();
			double height = Math.sin((time) / 8.0) / 6.0; // item height
			poseStack.translate(0.5F, 1.3 + height, 0.5F); // position offset
			poseStack.mulPose(Axis.YP.rotationDegrees((time) * 2)); // item stack rotation
			
			// fixed lighting because:
			// 1. light variable would always be 0 anyway (the pedestal is opaque, making the inside black)
			// 2. the floating item looks like a hologram
			Minecraft.getInstance().getItemRenderer().renderStatic(this.cachedRecipeOutput, ItemDisplayContext.GROUND, LightTexture.FULL_BRIGHT, overlay, poseStack, vertexConsumerProvider, entity.getLevel(), 0);
			poseStack.popPose();
		}
	}
	
}
