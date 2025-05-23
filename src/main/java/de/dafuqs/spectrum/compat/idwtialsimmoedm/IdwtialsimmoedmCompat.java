package de.dafuqs.spectrum.compat.idwtialsimmoedm;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import io.wispforest.idwtialsimmoedm.api.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.*;

@Environment(EnvType.CLIENT)
public class IdwtialsimmoedmCompat {
	public static void register() {
		GatherDescriptionCallback.ENCHANTMENT.register(ench -> {
			LocalPlayer player = Minecraft.getInstance().player;
			Component original = DefaultDescriptions.forEnchantmentRaw(ench);
			if (original == null) return null;
			if (ench.description().getContents() instanceof TranslatableContents translatable) {
				String idString = translatable.getKey().substring(translatable.getKey().indexOf('.') + 1).replace('.', ':');
				if (idString.startsWith(SpectrumCommon.MOD_ID) && !SpectrumEnchantmentHelper.canEntityUse(player, idString)) {
					return GatherDescriptionCallback.wrapDescription(original.copy().withStyle(ChatFormatting.OBFUSCATED));
				}
			}
			return null;
		});
	}
}
