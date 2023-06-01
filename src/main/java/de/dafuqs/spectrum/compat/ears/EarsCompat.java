package de.dafuqs.spectrum.compat.ears;

import com.unascribed.ears.api.*;
import com.unascribed.ears.api.registry.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public class EarsCompat {
    public static void register() {
        EarsInhibitorRegistry.register(SpectrumCommon.MOD_ID, (part, peer) -> {
            PlayerEntity player = (PlayerEntity) peer;
            if (part.isAnchoredTo(EarsAnchorPart.TORSO) && EarsStateOverriderRegistry.isActive(EarsStateType.WEARING_CHESTPLATE, peer, true).getValue()) {
                Item equippedItem = player.getEquippedStack(EquipmentSlot.CHEST).getItem();
                return equippedItem == SpectrumItems.BEDROCK_CHESTPLATE || equippedItem == SpectrumItems.FEROCIOUS_CHESTPLATE;
            }
            return false;
        });
    }
}
