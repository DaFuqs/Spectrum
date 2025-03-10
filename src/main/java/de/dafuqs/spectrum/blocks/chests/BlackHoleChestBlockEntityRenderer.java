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
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class BlackHoleChestBlockEntityRenderer implements BlockEntityRenderer<BlackHoleChestBlockEntity> {
	
	private static final SpriteIdentifier defaultSprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/black_hole_chest"));
	private static final SpriteIdentifier experienceSprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/black_hole_chest_experience"));

	private final ModelPart root;
	private final ModelPart shell;
	private final ModelPart cap;
	private final ModelPart storage;
	private final ModelPart orb;
	
	public BlackHoleChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		this.root = texturedModelData.createModel();
		this.shell = root.getChild("shell");
		this.cap = root.getChild("cap");
		this.storage = root.getChild("storage");
		this.orb = root.getChild("orb");
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData shell = modelPartData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -9.0F, -7.0F, 14.0F, 9.0F, 14.0F, new Dilation(0.0F))
				.uv(0, 39).cuboid(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cap = modelPartData.addChild("cap", ModelPartBuilder.create().uv(40, 39).cuboid(-5.0F, -6.0F, -5.0F, 10.0F, 4.0F, 10.0F, new Dilation(0.0F))
				.uv(82, 2).cuboid(-4.0F, -5.0F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData storage = modelPartData.addChild("storage", ModelPartBuilder.create().uv(42, 0).cuboid(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new Dilation(0.0F))
				.uv(56, 13).cuboid(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData orb = modelPartData.addChild("orb", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
				.uv(30, 39).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.4F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(BlackHoleChestBlockEntity chest, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrixStack.push();

		var sprite = chest.hasXPStorage() ? experienceSprite : defaultSprite;

		boolean bl = chest.getWorld() != null;
		BlockState blockState = bl ? chest.getCachedState() : SpectrumBlocks.BLACK_HOLE_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		float f = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING).asRotation() : 0;
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		var time = chest.getRenderTime();

		final double orbTransform = Math.sin((time + tickDelta) / 9F);
		final float potentialYaw = (time + tickDelta) / 6F;
		switch (chest.getState()) {
			case OPEN_ACTIVE -> {
				chest.storageTarget = 9;
				chest.capTarget = 12;
				chest.orbTarget = (float) (15 + orbTransform);
				chest.yawTarget = potentialYaw;
			}
			case OPEN_INACTIVE -> {
				chest.storageTarget = 9;
				chest.capTarget = 12;
				chest.orbTarget = chest.capTarget;
			}
			case CLOSED_ACTIVE -> {
				chest.capTarget = 0;
				chest.storageTarget = 0;
				chest.orbTarget = (float) (12.5 + orbTransform * 2.5F);
				chest.yawTarget = potentialYaw;

			}
			case CLOSED_INACTIVE -> {
				chest.capTarget = 0;
				chest.storageTarget = chest.capTarget;
				chest.orbTarget = chest.capTarget;
			}
			case FULL -> {
				chest.capTarget = 9;
				chest.storageTarget = chest.capTarget;
				chest.orbTarget = chest.capTarget;
			}
		}

		var interp = MathHelper.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.capPos = MathHelper.lerp(interp, chest.lastCapTarget, chest.capTarget);
		chest.storagePos = MathHelper.lerp(interp, chest.lastStorageTarget, chest.storageTarget);
		chest.orbPos = MathHelper.lerp(interp, chest.lastOrbTarget, chest.orbTarget);
		chest.orbYaw = MathHelper.lerp(interp, chest.lastYawTarget, chest.yawTarget);

		cap.pivotY = 24 - chest.capPos;
		storage.pivotY = 24 - chest.storagePos;
		orb.pivotY = 15.4F - chest.orbPos;
		orb.yaw = chest.yawTarget;

		storage.hidden = storage.pivotY > 23.99F;
		VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent);
		cap.render(matrixStack, vertexConsumer, light, overlay);
		shell.render(matrixStack, vertexConsumer, light, overlay);
		storage.render(matrixStack, vertexConsumer, light, overlay);

		int orbLight;

		if (chest.hasXPStorage()) {
			var xpDelta = (float) chest.storedXP / chest.maxStoredXP;
			var altLight = Math.round(MathHelper.clampedLerp(0, 15, xpDelta));
			orbLight = LightmapTextureManager.pack(altLight, altLight);
		} else {
            orbLight = light;
        }

        orb.forEachCuboid(matrixStack, ((matrix, path, index, cuboid) -> {
			cuboid.renderCuboid(matrixStack.peek(), vertexConsumer, index == 0 ? orbLight : light, overlay, 1, 1, 1, 1);
		}));


		matrixStack.pop();
	}
	
}