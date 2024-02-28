package de.dafuqs.spectrum.mixin.compat.enchdesc.present;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(DescriptionManager.class)
public abstract class DescriptionManagerMixin {
    @ModifyReturnValue(method = "getDescription", at = @At("RETURN"), remap = false)
    private static MutableText spectrum$obfuscateDescription(MutableText original, Enchantment ench) {
        Entity player = MinecraftClient.getInstance().player; // that feels kinda risky, since the class is not annotated as EnvType.Client
        if (ench instanceof SpectrumEnchantment spectrumEnchantment && !spectrumEnchantment.canEntityUse(player)) {
            return original.copy().formatted(Formatting.OBFUSCATED);
        }
        return original;
    }
}
