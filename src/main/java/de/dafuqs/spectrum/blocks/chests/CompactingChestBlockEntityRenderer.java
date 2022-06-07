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
public class CompactingChestBlockEntityRenderer<CompactingChestBlockEntity extends SpectrumChestBlockEntity> implements BlockEntityRenderer<CompactingChestBlockEntity> {
	
	private static final SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/compacting_chest"));
	private final ModelPart root;
	private final ModelPart lid;
	private final ModelPart column;
	private final ModelPart lock;
	
	public CompactingChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		root = texturedModelData.createModel();
		lid = root.getChild("lid");
		column = root.getChild("column");
		lock = root.getChild("lock");
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 17).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 11.0F, 14.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 3.0F, 14.0F), ModelTransform.pivot(0.0F, 13.0F, 0.0F));
		modelPartData.addChild("column", ModelPartBuilder.create().uv(18, 32).cuboid(7.0F, -7.0F, 7.0F, 2.0F, 7.0F, 2.0F), ModelTransform.pivot(0.0F, 13.0F, 0.0F));
		modelPartData.addChild("lock", ModelPartBuilder.create().uv(34, 4).cuboid(7.0F, -2.0F, 15.0F, 2.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 14.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	@Override
	public void render(CompactingChestBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? entity.getCachedState() : SpectrumBlocks.COMPACTING_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		
		matrixStack.push();
		float f = (blockState.get(ChestBlock.FACING)).asRotation();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
		matrixStack.translate(-0.5D, -0.5D, -0.5D);
		
		float openFactor = entity.getAnimationProgress(tickDelta);
		openFactor = 1.0F - openFactor;
		openFactor = 1.0F - openFactor * openFactor * openFactor;
		
		lid.pivotY = 11 + openFactor * 6;
		column.pivotY = 11 + openFactor * 6;
		lock.pivotY = 11 + openFactor * 6;
		
		VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
		
		root.render(matrixStack, vertexConsumer, light, overlay);
		
		matrixStack.pop();
	}
	
}