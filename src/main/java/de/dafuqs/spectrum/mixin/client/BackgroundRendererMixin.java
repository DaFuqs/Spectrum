package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.items.trinkets.AshenCircletItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
	
	@ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 0.25F, ordinal = 1))
	private static float modifyLavaVisibilityMinWithoutFireResistance(float original, Camera camera) {
		if(SpectrumTrinketItem.hasEquipped(camera.getFocusedEntity(), SpectrumItems.ASHEN_CIRCLET)) {
			return AshenCircletItem.LAVA_VIEW_DISTANCE_MIN;
		}
		return original;
	}
	
	@ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 1.0F, ordinal = 0))
	private static float modifyLavaVisibilityMaxWithoutFireResistance(float original, Camera camera) {
		if(SpectrumTrinketItem.hasEquipped(camera.getFocusedEntity(), SpectrumItems.ASHEN_CIRCLET)) {
			return AshenCircletItem.LAVA_VIEW_DISTANCE_MAX;
		}
		return original;
	}
	
	@ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 0.0F, ordinal = 0))
	private static float modifyLavaVisibilityMinFireResistance(float original, Camera camera) {
		if(SpectrumTrinketItem.hasEquipped(camera.getFocusedEntity(), SpectrumItems.ASHEN_CIRCLET)) {
			return AshenCircletItem.LAVA_VIEW_DISTANCE_MIN_POTION;
		}
		return original;
	}
	
	@ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 3.0F, ordinal = 0))
	private static float modifyLavaVisibilityMaxWithFireResistance(float original, Camera camera) {
		if(SpectrumTrinketItem.hasEquipped(camera.getFocusedEntity(), SpectrumItems.ASHEN_CIRCLET)) {
			return AshenCircletItem.LAVA_VIEW_DISTANCE_MAX_POTION;
		}
		return original;
	}
	
}