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
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
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
	public void render(CompactingChestBlockEntity chest, float tickDelta, PoseStack poseStack, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
		Level world = chest.getLevel();
		boolean bl = world != null;
		BlockState blockState = bl ? chest.getBlockState() : SpectrumBlocks.COMPACTING_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		poseStack.pushPose();
		float f = blockState.hasProperty(ChestBlock.FACING) ? blockState.getValue(ChestBlock.FACING).toYRot() : 0;
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(-f));
		poseStack.mulPose(Axis.XP.rotationDegrees(180));
		
		switch (chest.getState()) {
			case OPEN -> {
				chest.pistonTarget = 14;
				chest.driverTarget = 6;
				chest.capTarget = 5;
			}
			case CRAFTING -> {
				chest.pistonTarget = (float) (Math.sin((chest.activeTicks % 500000 + tickDelta) / 10F) * 5 + 4);
				chest.driverTarget = (float) (Math.sin(((chest.activeTicks + 13) % 500000 + tickDelta) / 10F) * 5 + 5);
				chest.capTarget = 0;
			}
			case CLOSED -> {
				chest.pistonTarget = 0;
				chest.driverTarget = 0;
				chest.capTarget = 0;
			}
		}
		
		var interp = Mth.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.pistonPos = Mth.lerp(interp, chest.lastPistonTarget, chest.pistonTarget);
		chest.driverPos = Mth.lerp(interp, chest.lastDriverTarget, chest.driverTarget);
		chest.capPos = Mth.lerp(interp, chest.lastCapTarget, chest.capTarget);
		piston.y = -22 - chest.pistonPos;
		driver.y = 21 - chest.driverPos;
		cap.y = 21 - chest.capPos;
		
		VertexConsumer vertexConsumer = SPRITE_IDENTIFIER.buffer(vertexConsumers, RenderType::entityCutoutNoCull);
		root.render(poseStack, vertexConsumer, light, overlay);
		
		poseStack.popPose();
	}
	
}