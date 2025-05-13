package de.dafuqs.spectrum.mixin.client;

import net.minecraft.client.*;
import net.minecraft.client.resource.language.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {

    @Mutable
    @Shadow
    @Final
    private Map<String, String> translations;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addTranslations(Map<String, String> translations, boolean rightToLeft, CallbackInfo ci) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) != Calendar.APRIL || calendar.get(Calendar.DAY_OF_MONTH) != 1) return;

        Map<String, String> builder = new HashMap<>(translations);
        builder.put("block.spectrum.crystallarieum", getCrystallarieuaeuieueum());
        builder.put("item.spectrum.ring_of_pursuit", "Ring of Fursuit");
        builder.put("item.spectrum.draconic_twinsword", "Draconic Winblade");
        builder.put("item.spectrum.dragon_talon", "Sellsword Winblades");
        builder.put("effect.spectrum.fatal_slumber", "Fat Slumber");
		builder.put("item.spectrum.oblivion_pickaxe", "Oblivious Pickaxe");
		builder.put("item.spectrum.whispy_circlet", "Whisky Circlet");
		builder.put("item.spectrum.shimmerstone_gem", "Stimmerstone Gem");
		builder.put("block.spectrum.shimmerstone_block", "Block of Stimmerstone");
		
		builder.put("item.spectrum.mermaids_gem", translations.get("item.spectrum.storm_stone"));
		builder.put("item.spectrum.storm_stone", translations.get("item.spectrum.mermaids_gem"));
		
        this.translations = builder;
    }
	
	@Unique
	private static String getCrystallarieuaeuieueum() {
        List<String> possibilities = new ArrayList<>() {{
            add("Crystallarieum");
            add("Crystallareium");
            add("Crystallerium");
            add("Crystallarium");
            add("Crystallium");
            add("Crystalleium");
            add("Crystallum");
            add("Crystallarieium");
            add("Christalerium");
        }};
        char c = MinecraftClient.getInstance().getSession().getUsername().toCharArray()[0];
        return possibilities.get((int) c % possibilities.size());
    }

}