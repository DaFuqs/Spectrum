package de.dafuqs.spectrum.compat.ears;

import com.unascribed.ears.api.*;
import com.unascribed.ears.api.registry.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

public class EarsCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
	public void register() {
	}
	
	@Override
	public void registerClient() {
		EarsInhibitorRegistry.register(SpectrumCommon.MOD_ID, (part, peer) -> {
			Player player = (Player) peer;
			if (part.isAnchoredTo(EarsAnchorPart.TORSO) && EarsStateOverriderRegistry.isActive(EarsStateType.WEARING_CHESTPLATE, peer, true).getValue()) {
				Item equippedItem = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
				return equippedItem == SpectrumItems.BEDROCK_CHESTPLATE || equippedItem == SpectrumItems.FEROCIOUS_CHESTPLATE;
			}
			return false;
		});
	}
	
}
