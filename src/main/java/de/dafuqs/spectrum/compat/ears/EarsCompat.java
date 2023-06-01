package de.dafuqs.spectrum.compat.ears;

import com.unascribed.ears.api.EarsAnchorPart;
import com.unascribed.ears.api.EarsStateType;
import com.unascribed.ears.api.registry.EarsInhibitorRegistry;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class EarsCompat {
    public static void register() {
        EarsInhibitorRegistry.register("yttr", (part, peer) -> {
            PlayerEntity player = (PlayerEntity)peer;
            if (part.isAnchoredTo(EarsAnchorPart.TORSO)
                    && player.getEquippedStack(EquipmentSlot.CHEST).getItem() == SpectrumItems.BEDROCK_CHESTPLATE
                    && EarsStateOverriderRegistry.isActive(EarsStateType.WEARING_CHESTPLATE, peer, true).getValue()) {
                return true;
            }
            return false;
        });
    }
}
