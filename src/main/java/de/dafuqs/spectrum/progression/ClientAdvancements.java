package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.mixin.client.accessors.AccessorClientAdvancementManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientAdvancements {

	private static boolean receivedFirstAdvancementPacket = false;

	public static void onClientPacket(AdvancementUpdateS2CPacket packet) {
		boolean showToast = receivedFirstAdvancementPacket;
		receivedFirstAdvancementPacket = true;

		List<Identifier> doneAdvancements = getDoneAdvancements(packet);

		ClientBlockCloaker.process(doneAdvancements, showToast);
		RecipeUnlockToastManager.processAdvancements(doneAdvancements, showToast);
	}

	public static boolean hasDone(Identifier identifier) {
		// If we never received the initial packet: assume false
		if(!receivedFirstAdvancementPacket) {
			return false;
		}

		if (identifier != null) {
			ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
			if (conn != null) {
				ClientAdvancementManager cm = conn.getAdvancementHandler();
				Advancement adv = cm.getManager().get(identifier);
				if (adv != null) {
					Map<Advancement, AdvancementProgress> progressMap = ((AccessorClientAdvancementManager) cm).getAdvancementProgresses();
					AdvancementProgress progress = progressMap.get(adv);
					return progress != null && progress.isDone();
				}
			}
		}
		return false;
	}

	public static List<Identifier> getDoneAdvancements(AdvancementUpdateS2CPacket packet) {
		List<Identifier> doneAdvancements = new ArrayList<>();

		for(Identifier earnedAdvancementIdentifier : packet.getAdvancementsToEarn().keySet()) {
			if(ClientAdvancements.hasDone(earnedAdvancementIdentifier)) {
				doneAdvancements.add(earnedAdvancementIdentifier);
			}
		}
		for(Identifier progressedAdvancementIdentifier : packet.getAdvancementsToProgress().keySet()) {
			if(ClientAdvancements.hasDone(progressedAdvancementIdentifier)) {
				if(!doneAdvancements.contains(progressedAdvancementIdentifier)) {
					doneAdvancements.add(progressedAdvancementIdentifier);
				}
			}
		}

		return doneAdvancements;
	}

	public static void playerLogout() {
		ClientBlockCloaker.cloakAll();
		receivedFirstAdvancementPacket = false;
	}

}