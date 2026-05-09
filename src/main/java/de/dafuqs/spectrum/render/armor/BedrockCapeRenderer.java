package de.dafuqs.spectrum.render.armor;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.*;
import javax.annotation.*;

public class BedrockCapeRenderer {
	
	private static <T extends Entity> void registerCapeLayer(@Nullable EntityRenderer<T> baseRenderer) {
		if (!(baseRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) {
			return;
		}
		
		if (!(livingRenderer.getModel() instanceof HumanoidModel<?>)) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		var humanoidRenderer = (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) livingRenderer;
		
		humanoidRenderer.addLayer(new BedrockCapeLayer<>(humanoidRenderer));
	}
	
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		for (PlayerSkin.Model skin : event.getSkins()) {
			EntityRenderer<? extends Player> renderer = event.getSkin(skin);
			
			registerCapeLayer(renderer);
		}
		
		for (EntityType<?> entityType : event.getEntityTypes()) {
			EntityRenderer<?> renderer = event.getRenderer(entityType);
			
			registerCapeLayer(renderer);
		}
	}
	
	private static class BedrockCapeLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
		
		private static final Tuple<Float, Float> DEFAULT_CAPE_ROTATION = new Tuple<>(0F, 0F);
		
		public BedrockCapeLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}
		
		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity livingEntity,
						   float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			
			ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
			if(!(chestStack.getItem() instanceof BedrockArmorItem bedrockArmorItem)) {
				return;
			}
			
			var capeRotations = DEFAULT_CAPE_ROTATION;
			if(livingEntity instanceof Player player) {
				// Transform front cloth
				capeRotations = BedrockArmorModel.computeFrontClothRotation(player, partialTick);
			}
			float capeZOffset = capeRotations.getB();
			
			ResourceLocation armorTexture = bedrockArmorItem.getArmorTexture(chestStack, livingEntity, EquipmentSlot.CHEST, BedrockArmorItem.ARMOR_MATERIAL_LAYER, true);
			VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entitySolid(armorTexture));
			poseStack.pushPose();
			poseStack.translate(0, 0.5, 0);
			poseStack.mulPose(Axis.XP.rotationDegrees(Mth.clamp(capeRotations.getA(), -25, 0)));
			if (!livingEntity.isCrouching()) {
				poseStack.mulPose(Axis.ZP.rotationDegrees(capeZOffset / 2.0F));
			}
			
			// Make some space for your legs if crouching
			poseStack.translate(0, -0.5, -0.025);
			if (livingEntity.isCrouching()) {
				poseStack.translate(0, 0.05, 0.35);
			}
			BedrockArmorCapeModel.FRONT_CLOTH.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
			
			if (RenderingContext.isElytraRendered) {
				return;
			}
			
			// The front and back cape are almost matching, but inverted
			float backCapeRotation = Mth.clamp(-capeRotations.getA(), -30, 45);
			
			// Transform and render the custom cape
			poseStack.pushPose();
			poseStack.translate(0, -0.05, 0.0); // Push up and backwards, then rotate
			poseStack.mulPose(Axis.XP.rotationDegrees(backCapeRotation));
			poseStack.mulPose(Axis.ZP.rotationDegrees(capeZOffset / 2.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - capeZOffset / 3.5F));
			poseStack.translate(0, 0.05, -0.325); // Move back down
			if (livingEntity.isCrouching()) {
				poseStack.translate(0, 0.15, 0.125);
			}
			
			BedrockArmorCapeModel.CAPE_MODEL.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}
	
}