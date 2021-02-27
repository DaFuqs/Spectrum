package de.dafuqs.pigment.registries;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PigmentItemStackDamageImmunities {

    private static final HashMap<String, List<Item>> damageSourceImmunities = new HashMap<>();

    public static void registerDefaultItemStackImmunities() {
        PigmentItemStackDamageImmunities.addImmunity(PigmentBlocks.CRACKED_END_PORTAL_FRAME.asItem(), "explosion");
        PigmentItemStackDamageImmunities.addImmunity(PigmentBlocks.CRACKED_END_PORTAL_FRAME.asItem(), DamageSource.IN_FIRE);
        PigmentItemStackDamageImmunities.addImmunity(PigmentBlocks.CRACKED_END_PORTAL_FRAME.asItem(), DamageSource.LAVA);
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
