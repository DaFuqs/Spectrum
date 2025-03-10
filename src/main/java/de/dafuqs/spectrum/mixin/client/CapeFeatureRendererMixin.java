package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.compat.vanityslots.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public CapeFeatureRendererMixin(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> ctx) {
		super(ctx);
	}

    /**
     * Renders a custom flap on the front of the Bedrock Armor, as well as a custom cape render
     */
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
	public void spectrum$renderBedrockCape(MatrixStack ms, VertexConsumerProvider vertices, int light, AbstractClientPlayerEntity player, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		// Check for the chestplate, and begin rendering the cape if equipped
		ItemStack chestStack = VanitySlotsCompat.getEquippedStack(player, EquipmentSlot.CHEST);
		if (chestStack.getItem() == SpectrumItems.BEDROCK_CHESTPLATE) {
			BedrockCapeRenderer.renderBedrockCapeAndCloth(ms, vertices, light, player, h, chestStack);
			// TODO - Cancel for now, as the new armor tailoring system is not implemented yet
			ci.cancel();
		}	
	}
	
}
