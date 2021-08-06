package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.mixin.client.AccessorClientAdvancementManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientAdvancements {

	private static boolean receivedFirstAdvancementPacket = false;

	public static void onClientPacket(AdvancementUpdateS2CPacket packet) {
		boolean showToast = receivedFirstAdvancementPacket;
		receivedFirstAdvancementPacket = true;
		ClientBlockCloaker.checkCloaksForNewAdvancements(packet, showToast);
		ClientAltarRecipeToastManager.checkAltarRecipesForNewAdvancements(packet, showToast);
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

	public static void playerLogout() {
		ClientBlockCloaker.cloakAll();
		receivedFirstAdvancementPacket = false;
	}

}