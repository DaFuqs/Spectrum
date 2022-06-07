package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class RestockingChestBlockEntityRenderer<RestockingChestBlockEntity extends SpectrumChestBlockEntity> implements BlockEntityRenderer<RestockingChestBlockEntity> {
	
	private static final SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/restocking_chest"));
	private final ModelPart root;
	private final ModelPart lid;
	
	public RestockingChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		root = texturedModelData.createModel();
		lid = root.getChild("lid");
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 16).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(3.0F, 0.0F, 3.0F, 10.0F, 6.0F, 10.0F), ModelTransform.pivot(0.0F, 11.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	@Override
	public void render(RestockingChestBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? entity.getCachedState() : SpectrumBlocks.RESTOCKING_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		
		matrixStack.push();
		float f = (blockState.get(ChestBlock.FACING)).asRotation();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
		matrixStack.translate(-0.5D, -0.5D, -0.5D);
		
		float openFactor = entity.getAnimationProgress(tickDelta);
		openFactor = 1.0F - openFactor;
		openFactor = 1.0F - openFactor * openFactor * openFactor;
		
		lid.pivotY = 5 + openFactor * 5;
		
		VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
		root.render(matrixStack, vertexConsumer, light, overlay);
		
		matrixStack.pop();
	}
	
}