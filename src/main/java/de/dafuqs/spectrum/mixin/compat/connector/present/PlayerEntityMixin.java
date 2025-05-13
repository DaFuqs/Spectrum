package de.dafuqs.spectrum.mixin.compat.connector.present;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "attack", name = "entityReachSq", at = @At(value = "STORE"))
    protected double spectrum$increaseSweepMaxDistance(double original) {
        var stack = this.getStackInHand(Hand.MAIN_HAND);
        if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD) {
			int channeling = SpectrumEnchantmentHelper.getLevel(getWorld().getRegistryManager(), Enchantments.CHANNELING, stack);
			return original * 3 * ((channeling + 1) * 1.5);
		}
        return original;
    }
}
