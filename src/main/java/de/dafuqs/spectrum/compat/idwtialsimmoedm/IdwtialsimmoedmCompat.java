package de.dafuqs.spectrum.compat.idwtialsimmoedm;

import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import io.wispforest.idwtialsimmoedm.api.DefaultDescriptions;
import io.wispforest.idwtialsimmoedm.api.GatherDescriptionCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class IdwtialsimmoedmCompat {
    public static void register() {
        GatherDescriptionCallback.ENCHANTMENT.register(ench -> {
            Entity player = MinecraftClient.getInstance().player;
            Text original = DefaultDescriptions.forEnchantmentRaw(ench);
            if (original == null) return null;
            if (ench instanceof SpectrumEnchantment spectrumEnchantment && !spectrumEnchantment.canEntityUse(player)) {
                return GatherDescriptionCallback.wrapDescription(original.copy().formatted(Formatting.OBFUSCATED));
            }
            return DefaultDescriptions.forEnchantmentFormatted(ench);
        });
    }
}
