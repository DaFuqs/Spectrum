package de.dafuqs.spectrum.sar.modifier;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleDamageMultiplier implements Modifier {

    public SimpleDamageMultiplier(float baseMultiplier, Predicate<DamageSource> effectiveTypes, Predicate<Entity> effectiveAgainst) {

    }

    public float apply(DamageSource source, ItemStack stack, float amount) {
        var sarStack = stack;
        return amount;
    }

    @Override
    public Class getType() {
        return null;
    }

    @Override
    public Modifier merge(Modifier that) {
        return null;
    }

    //@Override
    //public void register(@NotNull ModifierMap modifierMap) {
//
    //}
}
