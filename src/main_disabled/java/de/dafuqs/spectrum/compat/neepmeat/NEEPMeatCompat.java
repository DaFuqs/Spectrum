package de.dafuqs.spectrum.compat.neepmeat;

import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;
import net.minecraft.world.entity.*;

public class NEEPMeatCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	public void register() {

	}

	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {

	}
	
	// TODO - Reenable compat when up-to-date
	public static void sedateEnlightenment(LivingEntity user) {
//		if (user.isPlayer()) {
//			EnlightenmentManager manager = NMComponents.ENLIGHTENMENT_MANAGER.get(user);
//			double acuteEnlightenment = manager.getAcute();
//			double chronicEnlightenment = manager.getChronic();
//			if (acuteEnlightenment > 0) {
//				manager.setAcute(Math.max(0, acuteEnlightenment * 0.75 - 1));
//			}
//			if (chronicEnlightenment > 0 && (Math.random() > 0.9)) {
//				manager.setChronic(Math.max(0, chronicEnlightenment - 5));
//			}
//		}
	}

}
