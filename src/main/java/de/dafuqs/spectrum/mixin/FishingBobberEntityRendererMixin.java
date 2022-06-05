package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The fishing bobber renderer checks the hand of the players fishing rod
 * Since the fishing rod item is hardcoded we have to add a check for the bedrock fishing rod
 * Otherwise the fishing line would always render like it was in the offhand
 */
@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
	
	private PlayerEntity bobberOwner;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void storeContext(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		bobberOwner = fishingBobberEntity.getPlayerOwner();
	}
	
	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHandSwingProgress(F)F"), index = 12)
	private int render(int i) {
		ItemStack itemStack = bobberOwner.getMainHandStack();
		
		if (itemStack.getItem() != Items.FISHING_ROD) {
			if (itemStack.getItem().equals(SpectrumItems.BEDROCK_FISHING_ROD)) {
				return -i;
			}
		}
		
		return i;
	}
}