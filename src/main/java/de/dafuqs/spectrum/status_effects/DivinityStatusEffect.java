package de.dafuqs.spectrum.status_effects;

import com.mojang.authlib.GameProfile;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;

public class DivinityStatusEffect extends SpectrumStatusEffect {
	
	public DivinityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.getHealth() < entity.getMaxHealth()) {
			entity.heal(amplifier / 2F);
		}
		if(entity instanceof PlayerEntity playerEntity) {
			playerEntity.getHungerManager().add(1, 0.25F);
		}
		if (!entity.world.isClient) {
				WhispyCircletItem.removeSingleHarmfulStatusEffect(entity);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 80 >> amplifier;
		if(i > 0) {
			return duration % i == 0;
		}
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		entity.playSound(SpectrumSoundEvents.ENCHANTER_DING, 1.0F, 1.0F); // TODO: test & customize
	}
	
	public static final String HARDCORE_DEATH_REASON = "Death in Hardcore";
	
	public static boolean hasHardcorePlayerDeath(GameProfile gameProfile) {
		BannedPlayerList userBanList = SpectrumCommon.minecraftServer.getPlayerManager().getUserBanList();
		return userBanList.contains(gameProfile);
	}
	
	public static boolean unbanDeadHardcorePlayer(GameProfile gameProfile) {
		BannedPlayerList userBanList = SpectrumCommon.minecraftServer.getPlayerManager().getUserBanList();
		if(userBanList.contains(gameProfile)) {
			BannedPlayerEntry entry = userBanList.get(gameProfile);
			if(entry != null && entry.getReason().equals(HARDCORE_DEATH_REASON)) {
				userBanList.remove(gameProfile);
				return true;
			}
		}
		return false;
	}
	
}