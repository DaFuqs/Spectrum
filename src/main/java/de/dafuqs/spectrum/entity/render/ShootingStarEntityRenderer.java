package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.blocks.decoration.ShootingStarBlock;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {

	public ShootingStarEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowOpacity = 0.75F;
	}

	public void render(ShootingStarEntity shootingStarEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		BlockState blockState = shootingStarEntity.getShootingStarType().getBlock().getDefaultState();
		
		if (blockState.getRenderType() == BlockRenderType.MODEL) {
			World world = shootingStarEntity.getWorld();
			
			if (blockState != world.getBlockState(new BlockPos(shootingStarEntity.getPos())) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStack.push();
				
				BlockPos blockpos = new BlockPos(shootingStarEntity.getX(), shootingStarEntity.getBoundingBox().maxY, shootingStarEntity.getZ());
				matrixStack.translate(-0.5, 0.0, -0.5);
				BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
				blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockpos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(shootingStarEntity.getBlockPos()), OverlayTexture.DEFAULT_UV);
				matrixStack.pop();
				super.render(shootingStarEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
			}
		}
		/*ItemStack itemStack = type.getBlock().asItem().getDefaultStack();

		int j = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + itemStack.getDamage();
		this.random.setSeed(j);
		BakedModel bakedModel = this.itemRenderer.getModel(itemStack, shootingStarEntity.world, null, shootingStarEntity.getId());
		boolean bl = bakedModel.hasDepth();
		int k = 1;
		float l = MathHelper.sin(((float)shootingStarEntity.getAge() + g) / 10.0F + shootingStarEntity.hoverHeight) * 0.1F + 0.1F;
		float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
		matrixStack.translate(0.0D, (l + 0.25F * m), 0.0D);
		float n = shootingStarEntity.method_27314(g);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(n));
		float o = bakedModel.getTransformation().ground.scale.getX();
		float p = bakedModel.getTransformation().ground.scale.getY();
		float q = bakedModel.getTransformation().ground.scale.getZ();
		float v;
		float w;
		if (!bl) {
			float r = -0.0F * (float)(0) * 0.5F * o;
			v = -0.0F * (float)(0) * 0.5F * p;
			w = -0.09375F * (float)(0) * 0.5F * q;
			matrixStack.translate(r, v, w);
		}

		matrixStack.push();
		this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
		
		matrixStack.pop();*/
		
		super.render(shootingStarEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}

	@Override
	public Identifier getTexture(ShootingStarEntity entityIn) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}