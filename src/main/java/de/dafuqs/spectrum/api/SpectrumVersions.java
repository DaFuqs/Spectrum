package de.dafuqs.spectrum.api;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;

public class SpectrumVersions {
	
	public static final int UNSET = -1;
	public static final int UNKNOWN = 0;
	public static final int PRE_COLORFUL_UPDATE = 1; // upgrading to 1.13.0 from prev version
	public static final int COLORFUL_UPDATE = 2; // 1.13.0
	
	public static final int CURRENT_VERSION = COLORFUL_UPDATE;
	
	/**
	 * `MiscPlayerDataAttachmentType`saves the last version the player used their guidebook in
	 * this way we can trigger advancements / code paths / guidebook entries based on the players update journey
	 * Also see `ModUpdatedCriterion`
	 * @param player the player
	 * @return the players previous version
	 */
	public static int updateLastPlayedVersion(ServerPlayer player) {
		PlayerAdvancements advancements = player.getAdvancements();
		
		int previousVersion = MiscPlayerDataAttachmentType.get(player).getLastPlayedSpectrumVersion();
		if(previousVersion == UNSET) {
			ServerAdvancementManager advancementManager = player.getServer().getAdvancements();
			AdvancementHolder advancement = advancementManager.get(SpectrumAdvancements.PLACED_PEDESTAL);
			if(advancement != null) {
				AdvancementProgress progress = advancements.getOrStartProgress(advancement);
				if (progress.isDone() && !progress.getCriterion("interacted_with_existing_pedestal").isDone()) {
					// upgrading from a pre 1.13 version to 1.13
					previousVersion = PRE_COLORFUL_UPDATE;
				}
			}
		}
		
		SpectrumAdvancementCriteria.MOD_UPDATED.trigger(player, previousVersion, CURRENT_VERSION);
		
		if(previousVersion != CURRENT_VERSION) {
			MiscPlayerDataAttachmentType.get(player).setLastPlayedSpectrumVersion(CURRENT_VERSION);
		}
		
		return previousVersion;
	}
	
}
