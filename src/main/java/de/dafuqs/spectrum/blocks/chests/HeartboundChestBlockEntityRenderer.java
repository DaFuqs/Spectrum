package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class HeartboundChestBlockEntityRenderer implements BlockEntityRenderer<HeartboundChestBlockEntity> {

	private static final SpriteIdentifier SPRITE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/heartbound_chest"));

	private final ModelPart root;
	private final ModelPart bottomLock;
	private final ModelPart cap;
	private final ModelPart topLock;

	public HeartboundChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		var texturedModelData = getTexturedModelData();
		this.root = texturedModelData.createModel();
		this.bottomLock = root.getChild("bottomlock");
		this.cap = root.getChild("cap");
		this.topLock = cap.getChild("toplock");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bottomlock = modelPartData.addChild("bottomlock", ModelPartBuilder.create().uv(6, 5).cuboid(1.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 5).cuboid(-3.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 14.0F, -7.5F));

		ModelPartData cap = modelPartData.addChild("cap", ModelPartBuilder.create().uv(0, 0).cuboid(-7.5F, -5.0F, -14.5F, 15.0F, 5.0F, 15.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, 7.0F));

		ModelPartData toplock = cap.addChild("toplock", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -14.5F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 20).cuboid(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(HeartboundChestBlockEntity chest, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		var vertexConsumer = SPRITE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);

		boolean bl = chest.getWorld() != null;
		BlockState blockState = bl ? chest.getCachedState() : SpectrumBlocks.HEARTBOUND_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		float f = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING).asRotation() : 0;
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

		float openFactor = chest.getAnimationProgress(tickDelta);
		openFactor = 1.0F - openFactor;
		openFactor = 1.0F - openFactor * openFactor * openFactor;

		cap.pitch = -(openFactor * 1.5707964F);

		root.render(matrices, vertexConsumer, light, overlay);
		matrices.pop();

	}
}