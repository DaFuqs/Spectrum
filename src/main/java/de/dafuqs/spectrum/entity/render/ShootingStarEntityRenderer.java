package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {

	private final ItemRenderer itemRenderer;
	private final Random random = new Random();

	public ShootingStarEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
		this.shadowRadius = 0.15F;
		this.shadowOpacity = 0.75F;
	}

	public void render(ShootingStarEntity shootingStarEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		/*matrixStack.push();
		ShootingStarBlock.Type type = shootingStarEntity.getShootingStarType();
		ItemStack itemStack = type.getBlock().asItem().getDefaultStack();

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
		matrixStack.pop();

		matrixStack.pop();*/
		super.render(shootingStarEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(ShootingStarEntity entityIn) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}