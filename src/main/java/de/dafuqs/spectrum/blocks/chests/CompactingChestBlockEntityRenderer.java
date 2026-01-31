package de.dafuqs.spectrum.blocks.chests;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;


public class CompactingChestBlockEntityRenderer implements BlockEntityRenderer<CompactingChestBlockEntity> {
	
	private static final Material SPRITE_IDENTIFIER = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/compacting_chest"));
	private final ModelPart root;
	private final ModelPart driver;
	private final ModelPart piston;
	private final ModelPart cap;
	
	@SuppressWarnings("unused")
	public CompactingChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		LayerDefinition texturedModelData = getTexturedModelData();
		root = texturedModelData.bakeRoot();
		var fakeRoot = root.getChild("root");
		driver = fakeRoot.getChild("driver");
		piston = fakeRoot.getChild("piston");
		cap = fakeRoot.getChild("cap");
	}
	
	@SuppressWarnings("unused")
	public static @NotNull LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create().texOffs(81, 44).addBox(-1.5F, -10.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -11.0F, -7.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 60).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-7.5F, -2.0F, -7.5F, 15.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition driver = root.addOrReplaceChild("driver", CubeListBuilder.create().texOffs(53, 38).addBox(-3.5F, -36.0F, -3.5F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));
		
		PartDefinition piston = root.addOrReplaceChild("piston", CubeListBuilder.create().texOffs(89, 17).addBox(-3.5F, 7.0F, -3.5F, 7.0F, 14.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(45, 13).addBox(-5.5F, 7.0F, -5.5F, 11.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 0.0F));
		
		PartDefinition cap = root.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 24).addBox(-7.5F, -36.0F, -7.5F, 15.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(40, 65).addBox(-5.5F, -36.0F, -5.5F, 11.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void render(CompactingChestBlockEntity chest, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, int overlay) {
		if (chest.animator == null || chest.getLevel() == null)
			return;
		
		chest.animator.animate(tickDelta, chest.getLevel().getGameTime() % 100000);
		
		poseStack.pushPose();
		float f = chest.getBlockState().getValue(ChestBlock.FACING).toYRot();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(-f));
		poseStack.mulPose(Axis.XP.rotationDegrees(180));
		
		piston.y = -22 - chest.piston.get();
		driver.y = 21 - chest.driver.get();
		cap.y = 21 - chest.cap.get();
		
		VertexConsumer vertexConsumer = SPRITE_IDENTIFIER.buffer(vertexConsumers, RenderType::entityCutoutNoCull);
		root.render(poseStack, vertexConsumer, light, overlay);
		
		poseStack.popPose();
	}
	
}