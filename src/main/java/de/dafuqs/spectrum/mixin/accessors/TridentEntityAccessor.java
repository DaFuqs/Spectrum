package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.data.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {

    @Accessor("LOYALTY")
    static TrackedData<Byte> spectrum$getLoyalty() {
        return null;
    }

    @Accessor("ENCHANTED")
    static TrackedData<Boolean> spectrum$getEnchanted() {
        return null;
    }

    @Accessor("tridentStack")
    ItemStack spectrum$getTridentStack();

    @Accessor("tridentStack")
    void spectrum$setTridentStack(ItemStack stack);

    @Accessor("dealtDamage")
    boolean spectrum$hasDealtDamage();

    @Accessor("dealtDamage")
    void spectrum$setDealtDamage(boolean dealtDamage);

}