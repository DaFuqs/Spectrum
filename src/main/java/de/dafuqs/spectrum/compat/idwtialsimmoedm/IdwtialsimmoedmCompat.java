package de.dafuqs.spectrum.compat.idwtialsimmoedm;

import de.dafuqs.spectrum.helpers.*;
import io.wispforest.idwtialsimmoedm.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.entity.Entity;
import net.minecraft.text.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class IdwtialsimmoedmCompat {
    public static void register() {
        GatherDescriptionCallback.ENCHANTMENT.register(ench -> {
            Entity player = MinecraftClient.getInstance().player;
            Text original = DefaultDescriptions.forEnchantmentRaw(ench);
            if (original == null) return null;
			if (ench.description().getContent() instanceof TranslatableTextContent translatable) {
				String idString = translatable.getKey().substring(translatable.getKey().indexOf('.') + 1).replace('.', ':');
				if (idString.startsWith("spectrum") && !SpectrumEnchantmentHelper.canEntityUse(player, idString)) {
					return GatherDescriptionCallback.wrapDescription(original.copy().formatted(Formatting.OBFUSCATED));
				}
			}
            return null;
        });
    }
}
