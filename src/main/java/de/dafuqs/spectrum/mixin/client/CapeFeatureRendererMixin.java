package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.compat.vanityslots.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(CapeLayer.class)
public abstract class CapeFeatureRendererMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	
	public CapeFeatureRendererMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> ctx) {
		super(ctx);
	}

    /**
     * Renders a custom flap on the front of the Bedrock Armor, as well as a custom cape render
     */
	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
	public void spectrum$renderBedrockCape(PoseStack ms, MultiBufferSource vertices, int light, AbstractClientPlayer player, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		// Check for the chestplate, and begin rendering the cape if equipped
		ItemStack chestStack = VanitySlotsCompat.getEquippedStack(player, EquipmentSlot.CHEST);
		if (chestStack.getItem() == SpectrumItems.BEDROCK_CHESTPLATE) {
			BedrockCapeRenderer.renderBedrockCapeAndCloth(ms, vertices, light, player, h, chestStack);
			ci.cancel();
		}	
	}
	
}
