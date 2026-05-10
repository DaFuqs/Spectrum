package de.dafuqs.spectrum.blocks.chests;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class BlackHoleChestBlockEntityRenderer implements BlockEntityRenderer<BlackHoleChestBlockEntity> {
	
	private static final Material defaultSprite = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/black_hole_chest"));
	private static final Material experienceSprite = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/black_hole_chest_experience"));

	private final ModelPart root;
	private final ModelPart shell;
	private final ModelPart cap;
	private final ModelPart storage;
	private final ModelPart orb;
	
	public BlackHoleChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		LayerDefinition texturedModelData = getTexturedModelData();
		this.root = texturedModelData.bakeRoot();
		this.shell = root.getChild("shell");
		this.cap = root.getChild("cap");
		this.storage = root.getChild("storage");
		this.orb = root.getChild("orb");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition shell = modelPartData.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 9.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 39).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition cap = modelPartData.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(40, 39).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(82, 2).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition storage = modelPartData.addOrReplaceChild("storage", CubeListBuilder.create().texOffs(42, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(56, 13).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition orb = modelPartData.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 39).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.4F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}

	@Override
	public void render(BlackHoleChestBlockEntity chest, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, int overlay) {
		poseStack.pushPose();

		var sprite = chest.hasXPStorage() ? experienceSprite : defaultSprite;
		
		boolean bl = chest.getLevel() != null;
		BlockState blockState = bl ? chest.getBlockState() : SpectrumBlocks.BLACK_HOLE_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		float f = blockState.hasProperty(ChestBlock.FACING) ? blockState.getValue(ChestBlock.FACING).toYRot() : 0;
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(-f));
		poseStack.mulPose(Axis.XP.rotationDegrees(180));
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
		
		var interp = Mth.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.capPos = Mth.lerp(interp, chest.lastCapTarget, chest.capTarget);
		chest.storagePos = Mth.lerp(interp, chest.lastStorageTarget, chest.storageTarget);
		chest.orbPos = Mth.lerp(interp, chest.lastOrbTarget, chest.orbTarget);
		chest.orbYaw = Mth.lerp(interp, chest.lastYawTarget, chest.yawTarget);
		
		cap.y = 24 - chest.capPos;
		storage.y = 24 - chest.storagePos;
		orb.y = 15.4F - chest.orbPos;
		orb.yRot = chest.yawTarget;
		
		storage.skipDraw = storage.y > 23.99F;
		VertexConsumer vertexConsumer = sprite.buffer(vertexConsumers, RenderType::entityTranslucent);
		cap.render(poseStack, vertexConsumer, light, overlay);
		shell.render(poseStack, vertexConsumer, light, overlay);
		storage.render(poseStack, vertexConsumer, light, overlay);

		int orbLight;

		if (chest.hasXPStorage()) {
			var xpDelta = (float) chest.storedXP / chest.maxStoredXP;
			var altLight = Math.round(Mth.clampedLerp(0, 15, xpDelta));
			orbLight = LightTexture.pack(altLight, altLight);
		} else {
            orbLight = light;
        }
		
		orb.visit(poseStack, (matrix, path, index, cuboid) -> cuboid.compile(
				poseStack.last(),
				vertexConsumer,
				index == 0 ? orbLight : light,
				overlay,
				-1
		));
		
		poseStack.popPose();
	}
	
}
