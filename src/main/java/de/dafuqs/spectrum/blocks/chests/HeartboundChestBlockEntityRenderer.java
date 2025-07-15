package de.dafuqs.spectrum.blocks.chests;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class HeartboundChestBlockEntityRenderer implements BlockEntityRenderer<HeartboundChestBlockEntity> {
	
	private static final Material SPRITE = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/heartbound_chest"));
	
	private final ModelPart root;
	private final ModelPart bottomLock;
	private final ModelPart cap;
	private final ModelPart topLock;
	
	public HeartboundChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		var texturedModelData = getTexturedModelData();
		this.root = texturedModelData.bakeRoot();
		this.bottomLock = root.getChild("bottomlock");
		this.cap = root.getChild("cap");
		this.topLock = cap.getChild("toplock");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition bottomlock = modelPartData.addOrReplaceChild("bottomlock", CubeListBuilder.create().texOffs(6, 5).addBox(1.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 5).addBox(-3.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -7.5F));
		
		PartDefinition cap = modelPartData.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -5.0F, -14.5F, 15.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 7.0F));
		
		PartDefinition toplock = cap.addOrReplaceChild("toplock", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -14.5F));
		
		PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 20).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
	
	@Override
	public void render(HeartboundChestBlockEntity chest, float tickDelta, PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.pushPose();
		var vertexConsumer = SPRITE.buffer(vertexConsumers, RenderType::entitySolid);
		
		boolean bl = chest.getLevel() != null;
		BlockState blockState = bl ? chest.getBlockState() : SpectrumBlocks.HEARTBOUND_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		float f = blockState.hasProperty(ChestBlock.FACING) ? blockState.getValue(ChestBlock.FACING).toYRot() : 0;
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.mulPose(Axis.YP.rotationDegrees(-f));
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		
		float openFactor = chest.getOpenNess(tickDelta);
		openFactor = 1.0F - openFactor;
		openFactor = 1.0F - openFactor * openFactor * openFactor;
		
		cap.xRot = -(openFactor * 1.5707964F);
		
		root.render(matrices, vertexConsumer, light, overlay);
		matrices.popPose();
		
	}
}