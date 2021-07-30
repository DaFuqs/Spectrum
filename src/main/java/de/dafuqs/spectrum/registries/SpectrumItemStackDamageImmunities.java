package de.dafuqs.spectrum.registries;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpectrumItemStackDamageImmunities {

    private static final HashMap<String, List<Item>> damageSourceImmunities = new HashMap<>();

    public static void registerDefaultItemStackImmunities() {
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem(), "explosion");
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem(), DamageSource.CACTUS);
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem(), DamageSource.IN_FIRE);
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem(), DamageSource.LAVA);
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageSource.LAVA);
        SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageSource.LAVA);
    }

    public static void addImmunity(Item item, DamageSource damageSource) {
        addImmunity(item, damageSource.name);
    }

    public static void addImmunity(Item item, String damageSourceName) {
        if(damageSourceImmunities.containsKey(damageSourceName)) {
            damageSourceImmunities.get(damageSourceName).add(item);
        } else {
            ArrayList<Item> newList = new ArrayList<>();
            newList.add(item);
            damageSourceImmunities.put(damageSourceName, newList);
        }
    }

    public static boolean isDamageImmune(Item item, DamageSource damageSource) {
        if(damageSourceImmunities.containsKey(damageSource.getName())) {
            return damageSourceImmunities.get(damageSource.getName()).contains(item);
        } else {
            return false;
        }
    }

}
