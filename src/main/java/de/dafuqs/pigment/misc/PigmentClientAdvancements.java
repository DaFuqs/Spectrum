package de.dafuqs.pigment.misc;

import de.dafuqs.pigment.mixin.client.AccessorClientAdvancementManager;
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
public class PigmentClientAdvancements {

	private static boolean gotFirstAdvPacket = false;

	public static void onClientPacket(AdvancementUpdateS2CPacket packet) {
		PigmentBlockCloaker.checkBlockCloaksForNewAdvancements(packet, gotFirstAdvPacket);
		PigmentAltarRecipeUnlocker.checkAltarRecipesForNewAdvancements(packet, gotFirstAdvPacket);
		gotFirstAdvPacket = true;
	}

	public static boolean hasDone(Identifier identifier) {
		// If we never received the initial packet: assume false
		if(!gotFirstAdvPacket) {
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
		PigmentBlockCloaker.cloakAll();
		PigmentAltarRecipeUnlocker.removeRecipes();
		gotFirstAdvPacket = false;
	}

}